package com.tomtop.website.migration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public interface ICommand {

	String commandName();

	Options extraOptions();

	void execute(CommandLine args);

}
