package com.tomtop.website.migration;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tomtop.website.migration.category.CategoryExportCommand;
import com.tomtop.website.migration.comment.CommentExport;
import com.tomtop.website.migration.filter.ProductSkuFilterCommand;
import com.tomtop.website.migration.keyword.KeywordExport;
import com.tomtop.website.migration.member.MemberExportCommand;
import com.tomtop.website.migration.order.OrderExportCommand;
import com.tomtop.website.migration.product.MultiProductExportCommand;
import com.tomtop.website.migration.product.ProductCostPriceExportCommand;
import com.tomtop.website.migration.product.ProductExportCommand;
import com.tomtop.website.migration.product.ProductGroupPriceExportCommand;
import com.tomtop.website.migration.product.SkuExportCommand;
import com.tomtop.website.migration.product.SpecialProductExportCommand;
import com.tomtop.website.migration.product.UnActiveProductExportCommand;
import com.tomtop.website.migration.relation.CategoryRelationExportCommand;
import com.tomtop.website.migration.relation.RelationExportCommand;
import com.tomtop.website.mybatis.MyBatisService;
import com.tomtop.website.translation.TranslatorCommand;
import com.tomtop.website.upload.ImportCommand;

public class Main {

	Set<ICommand> commands;
	Map<String, ICommand> commandMap;
	MyBatisService mybatis;
	Injector injector;

	Main() {
		commands = Sets.newHashSet(new MemberExportCommand(),
				new OrderExportCommand(), new ProductExportCommand(),
				new CategoryExportCommand(), new TranslatorCommand(),
				new ImportCommand(), new RelationExportCommand(),
				new CategoryRelationExportCommand(),
				new ProductCostPriceExportCommand(),
				new ProductGroupPriceExportCommand(),
				new MultiProductExportCommand(), new KeywordExport(),
				new SkuExportCommand(), new CommentExport(),
				new ProductSkuFilterCommand(),
				new UnActiveProductExportCommand(),
				new SpecialProductExportCommand());
		commandMap = Maps.uniqueIndex(commands, c -> c.commandName());
	}

	public void execute(String command, String[] args) throws ParseException {
		ICommand cmd = commandMap.get(command);
		if (cmd == null) {
			throw new RuntimeException("Command '" + command + "' not found");
		}
		mybatis.configure(cmd);
		mybatis.initialize();
		injector = Guice.createInjector(mybatis.getModules().values());
		injector.injectMembers(cmd);

		CommandLineParser parser = new BasicParser();
		CommandLine cmdLine = parser.parse(cmd.extraOptions(), args);
		cmd.execute(cmdLine);
	}

	public void help(String command) {
		ICommand cmd = commandMap.get(command);
		if (cmd == null) {
			throw new RuntimeException("Command '" + command + "' not found");
		}
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(command, cmd.extraOptions());
	}

	public Set<String> getCommands() {
		return commandMap.keySet();
	}

	/**
	 * Setup Magento Database
	 * 
	 * @param mysqlHost
	 * @param username
	 * @param password
	 */
	protected void setup(String mysqlHost, String dbname, String username,
			String password) {
		Map<String, Properties> configs = Maps.newHashMap();
		configs.put(
				"magento",
				MyBatisService
						.getProperties(
								"com.mysql.jdbc.Driver",
								"jdbc:mysql://"
										+ mysqlHost
										+ "/"
										+ dbname
										+ "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
								username, password));
		mybatis = new MyBatisService(configs);
	}

	// ----------- Entry Point -----------------
	public static void main(String[] args) throws ParseException {

		Main m = new Main();

		Options options = new Options();
		options.addOption("H", true,
				"Magento MySQL Server Address [192.168.7.13]");
		options.addOption("D", true, "Magento MySQL Database Name [magento]");
		options.addOption("U", true, "Magento MySQL Username [magento]");
		options.addOption("P", true, "Magento MySQL Password [magento]");
		options.addOption("h", true, "Help on Command Options");

		if (args.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("migration", options);
			System.err.println();
			System.err.println("Possible Command: " + m.getCommands());
			System.exit(2);
		}

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args, true);

		if (cmd.hasOption("h")) {
			String cmdName = cmd.getOptionValue("h");
			m.help(cmdName);
			System.exit(2);
		}

		// db setup
		String mysql = cmd.hasOption("H") ? cmd.getOptionValue("H")
				: "192.168.7.13";
		String db = cmd.hasOption("D") ? cmd.getOptionValue("D") : "magento";
		String user = cmd.hasOption("U") ? cmd.getOptionValue("U") : "magento";
		String pass = cmd.hasOption("P") ? cmd.getOptionValue("P") : "magento";
		m.setup(mysql, db, user, pass);

		@SuppressWarnings("unchecked")
		List<String> remains = cmd.getArgList();

		Optional<String> command = FluentIterable.from(remains).first();
		if (command.isPresent()) {
			m.execute(command.get(), FluentIterable.from(remains).skip(1)
					.toArray(String.class));
		} else {
			System.err.println("Command not specified");
			System.err.println("Possible Command: " + m.getCommands());
			System.exit(1);
		}
	}
}
