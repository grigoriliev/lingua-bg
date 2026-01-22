package eu.ideya.lingua.bg.audit;

import eu.ideya.lingua.bg.audit.internal.DefaultAuditRegistry;
import eu.ideya.lingua.bg.audit.rdf.RdfAudit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class LinguaBGAudit {
	public static final String APP_NAME = "lingua-bg-audit";
	public static final String APP_VERSION = "0.1.0-SNAPSHOT";

	public static void main(String[] args) {
		final Options options = new Options();
		options.addOption(
			Option.builder("i")
				.longOpt("input")
				.hasArg()
				.desc("Input file, directory, or URL")
				.get()
		);

		options.addOption(
			Option.builder("f")
				.longOpt("format")
				.hasArg()
				.desc("Input format (ttl/freedict)")
				.get()
		);

		options.addOption(
			Option.builder("a")
				.longOpt("audit")
				.hasArg()
				.desc("Comma-separated list of audits to run")
				.get()
		);

		options.addOption("h", "help", false, "Show help");
		options.addOption("v", "version", false, "Show version");

		if (args.length == 0) {
			printHelpAndExit(options);
		}

		final CommandLineParser parser = new DefaultParser();

		try {
			final CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help") || cmd.getOptions().length == 0) {
				printHelpAndExit(options);
			}

			if (cmd.hasOption("version")) {
				System.out.println(APP_NAME + " " + APP_VERSION);
				System.exit(0);
			}

			final String inputPath = cmd.getOptionValue("input");

			if (inputPath == null) {
				System.err.println("Missing input. Specify file, directory, or URL");
				System.err.println();
				printHelpAndExit(options);
			}

			final AuditRegistry registry = new DefaultAuditRegistry();

			final List<Audit> audits = Optional.ofNullable(cmd.getOptionValue("audit"))
				.map(auditList -> auditList.split(","))
				.map(a -> Stream.of(a).map(registry::getAuditByID))
				.<List<Audit>>map(
					stream -> stream.map(
						audit -> {
							if (audit.isPresent()) {
								return audit.get();
							}
							System.err.println("Unknown audit: " + audit);
							return null;
						}
					).filter(Objects::nonNull).collect(Collectors.toList())
				).orElse(registry.stream().collect(Collectors.toList()));

			Model model = null;

			for (Audit<?> audit : audits) {
				if (audit instanceof RdfAudit rdfAudit) {
					if (model == null) {
						model = loadRdf(inputPath, cmd.getOptionValue("format"));
					}
					final AuditResult result = rdfAudit.run(model);
					System.out.println("Audit: " + rdfAudit.id().getName());
					System.out.println();
					result.getMetrics().forEach(
						(text, count) -> System.out.println(text + ": " + count)
					);
				}
			}


		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private static void printHelpAndExit(Options options) {
		final HelpFormatter formatter = HelpFormatter.builder().get();
		try {
			formatter.printHelp(APP_NAME, null, options, null, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.exit(0);
	}

	private static Model loadRdf(String inputPath, String format) {
		if ("ttl".equals(format) || (format != null && format.startsWith("ttl/"))) {
			final Model model = ModelFactory.createDefaultModel();
			try {
				model.read(new FileInputStream(inputPath), null, "TURTLE");
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}

			return model;
		} else {
			System.err.println("Unknown format");
			System.exit(3);
		}

		throw new UnsupportedOperationException();
	}
}
