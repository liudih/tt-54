package controllers.common;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import liquibase.Liquibase;
import liquibase.LiquibaseService;
import liquibase.exception.LiquibaseException;
import mybatis.MyBatisService;
import play.Logger;
import play.Play;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class LiquibaseController extends Controller {

	@Inject
	LiquibaseService service;

	@Inject
	MyBatisService mybatis;

	public Result update(final boolean doit, final boolean fixChecksum, final String context)
			throws LiquibaseException {
		final Set<String> names = mybatis.getNames();
		if (Play.isProd() && !"production".equals(context)) {
			return badRequest("Cannot apply context: " + context);
		}
		if (doit) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			// tag before update, good for rollback
			String lasttag = "last";
			String tag = df.format(new Date());
			Map<String, String> output = Maps.toMap(names,
					new Function<String, String>() {
						@Override
						public String apply(String name) {
							try {
								Liquibase liquibase = service
										.getLiquibaseInstance(name);
								if (fixChecksum) {
									liquibase.clearCheckSums();
								}
								liquibase.tag(lasttag);
								liquibase.update(context);
								liquibase.tag(tag);
								return "OK";
							} catch (LiquibaseException e) {
								Logger.error("Database Change Error", e);
								return e.getMessage();
							}
						}
					});
			return ok(views.html.common.liquibase.done.render(output));
		} else {
			Map<String, StringWriter> output = Maps.toMap(names,
					new Function<String, StringWriter>() {
						@Override
						public StringWriter apply(String name) {
							try {
								Liquibase liquibase = service
										.getLiquibaseInstance(name);
								if (fixChecksum) {
									liquibase.clearCheckSums();
								}
								StringWriter writer = new StringWriter();
								liquibase.update(context, writer);
								return writer;
							} catch (LiquibaseException e) {
								Logger.error("Database Change Preview Error", e);
								StringWriter sw = new StringWriter();
								sw.append(e.getMessage());
								return sw;
							}
						}
					});
			return ok(views.html.common.liquibase.preview.render(context, true,
					output));
		}
	}

	public Result rollback(final String tag, final boolean doit,
			final String context) throws Exception {
		final Set<String> names = mybatis.getNames();
		if (doit) {
			Map<String, String> output = Maps.toMap(names,
					new Function<String, String>() {
						@Override
						public String apply(String name) {
							try {
								Liquibase liquibase = service
										.getLiquibaseInstance(name);
								liquibase.rollback(tag, context);
								return "OK";
							} catch (LiquibaseException e) {
								Logger.error("Database Change Error", e);
								return e.getMessage();
							}
						}
					});
			return ok(views.html.common.liquibase.done.render(output));
		} else {
			Map<String, StringWriter> output = Maps.toMap(names,
					new Function<String, StringWriter>() {
						@Override
						public StringWriter apply(String name) {
							try {
								Liquibase liquibase = service
										.getLiquibaseInstance(name);
								StringWriter writer = new StringWriter();
								liquibase.rollback(tag, context, writer);
								return writer;
							} catch (LiquibaseException e) {
								Logger.error("Database Change Preview Error", e);
								StringWriter sw = new StringWriter();
								sw.append(e.getMessage());
								return sw;
							}
						}
					});
			return ok(views.html.common.liquibase.preview.render(context,
					false, output));
		}
	}

	public Result destructiveUpdate() throws Exception {
		boolean dropOnly = request().queryString().containsKey("dropOnly");
		if (Play.isProd()) {
			if (!Play.application().configuration()
					.getBoolean("liquibase.reset.allowed", false)) {
				return badRequest("DB Reset not allowed in Production");
			}
		}
		final Set<String> names = mybatis.getNames();
		Map<String, F.Option<Exception>> dropResults = Maps.toMap(names, (
				String n) -> {
			try {
				service.getLiquibaseInstance(n).dropAll();
			} catch (Exception e) {
				Logger.error("Error Dropping Table in DB: " + n, e);
				return F.Option.Some(e);
			}
			return F.Option.<Exception> None();
		});

		Map<String, F.Option<Exception>> updateResults = Maps.toMap(names, (
				String n) -> {
			if (!dropOnly) {
				try {
					service.getLiquibaseInstance(n).update("test");
				} catch (Exception e) {
					return F.Option.Some(e);
				}
			}
			return F.Option.<Exception> None();
		});
		return ok(views.html.common.liquibase.reset.render(dropResults,
				updateResults));
	}
}
