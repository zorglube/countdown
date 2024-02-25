/**
 *
 */
package org.bcrm.countdown;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zorglube
 *
 */
public class CountDown implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(CountDown.class);

	private final Option hour = new Option("h", "hour", true, "Number of hour(s) to count down");
	private final Option minute = new Option("m", "minute", true, "Number of minute(s) to count down");
	private final Option second = new Option("s", "second", true, "Number of second(s) to count down");
	private final Option help = new Option("H", "help", false, "Show the help");
	private final Option fileName = new Option("f", "file", true, "The out file");
	private final Option verbose = new Option("v", "verbose", false, "Turn on verbose");
	private final Options options;
	private final CommandLineParser parser;
	private final CommandLine cl;
	private final File file;
	private final ClockCount cc;

	private CountDown(String[] args) throws ParseException {
		options = new Options();
		hour.setRequired(true);
		options.addOption(hour);
		minute.setRequired(true);
		options.addOption(minute);
		second.setRequired(true);
		options.addOption(second);
		options.addOption(help);
		fileName.setRequired(true);
		options.addOption(fileName);
		options.addOption(verbose);

		parser = new DefaultParser();

		cl = parser.parse(options, args);

		cc = new ClockCount(Integer.parseInt(cl.getOptionValue(hour)), Integer.parseInt(cl.getOptionValue(minute)),
				Integer.parseInt(cl.getOptionValue(second)));
		file = new File(new File(cl.getOptionValue(fileName)).getAbsolutePath());
	}

	private void showCommandline() {
		options.getOptions().stream().forEach(o -> log.info("{}", o));
	}

	private void writeCpt() throws IOException {
		Files.writeString(file.toPath(), cc.valueString(), StandardCharsets.UTF_8);
	}

	private void printHelp() {
		final HelpFormatter formatter = new HelpFormatter();
		final PrintWriter writer = new PrintWriter(System.out);
		formatter.printUsage(writer, 80, "CountDown", options);
		writer.flush();
	}

	private boolean isHelpRequierd() {
		return cl.hasOption(help);
	}

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws ParseException, IOException, InterruptedException {
		final CountDown cd = new CountDown(args);

		if (cd.isHelpRequierd()) {
			cd.printHelp();
		} else {
			cd.showCommandline();
			cd.writeCpt();
			cd.run();
		}
	}

	@Override
	public void run() {
		try {
			while (!cc.isFishied()) {

				if (cl.hasOption(verbose)) {
					log.info(cc.valueString());
				}

				cc.decrement();
				Thread.sleep(1000);
				writeCpt();
			}
			Thread.currentThread().interrupt();
		} catch (final InterruptedException e) {
			log.debug("InterruptException cause: {}, message: {}", e.getCause(), e.getMessage());
			e.printStackTrace();
		} catch (final IOException e) {
			log.debug("IOException cause:{}, message: {}", e.getCause(), e.getMessage());
			e.printStackTrace();
		}
	}

}
