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

import lombok.extern.slf4j.Slf4j;

/**
 * @author zorglube
 *
 */
@Slf4j
public class CountDown {

	private Option hour = new Option("h", "hour", true, "Number of hour(s) to count down");
	private Option minute = new Option("m", "minute", true, "Number of minute(s) to count down");
	private Option second = new Option("s", "second", true, "Number of second(s) to count down");
	private Option help = new Option("H", "help", false, "Show the help");
	private Option fileName = new Option("f", "file", true, "The out file");
	private Option verbose = new Option("v", "verbose", false, "Turn on verbosity");
	private Options options;
	private CommandLineParser parser;
	private CommandLine cl;
	private File file;
	private ClockCount cc;

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

		cc = new ClockCount(Integer.valueOf(cl.getOptionValue(hour)), Integer.valueOf(cl.getOptionValue(minute)),
				Integer.valueOf(cl.getOptionValue(second)));
		file = new File(new File(cl.getOptionValue(fileName)).getAbsolutePath());
	}

	private void showCommandline() {
		options.getOptions().stream().forEach(options -> log.info(options.toString()));
	}

	private void writeCpt() throws IOException {
		Files.writeString(file.toPath(), cc.valueString(), StandardCharsets.UTF_8);
	}

	private void runCountDown() throws InterruptedException, IOException {
		while (!cc.isFishied()) {

			if (cl.hasOption(verbose))
				log.info(cc.valueString());

			cc.decrement();
			Thread.sleep(1000);
			this.writeCpt();
		}
	}

	private void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		PrintWriter writer = new PrintWriter(System.out);
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
		CountDown cd = new CountDown(args);

		if (cd.isHelpRequierd()) {
			cd.printHelp();
		} else {
			cd.showCommandline();
			cd.writeCpt();
			cd.runCountDown();
		}
	}

}
