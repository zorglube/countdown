/**
 * 
 */
package org.bcrm.countdown;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zorglube
 *
 */
@Slf4j
public class CoundDown {

	private Option hour = new Option("h", "hour", true, "Number of hour(s) to count down");
	private Option minute = new Option("m", "minute", true, "Number of minute(s) to count down");
	private Option second = new Option("s", "second", true, "Number of second(s) to count down");
	private Option help = new Option("H", "help", false, "Show the help");
	private Option fileName = new Option("f", "file", true, "The out file");
	private Option verbose = new Option("v", "verbose", false, "Turn on verbosity");
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;
	private File file;
	private ClockCount cpt;

	private CoundDown(String[] args) throws ParseException {
		options = new Options();
		options.addOption(hour);
		options.addOption(minute);
		options.addOption(second);
		options.addOption(help);
		options.addOption(fileName);
		options.addOption(verbose);

		parser = new DefaultParser();

		cmd = parser.parse(options, args);

		cpt = new ClockCount(Integer.valueOf(cmd.getOptionValue(hour)), Integer.valueOf(cmd.getOptionValue(minute)),
				Integer.valueOf(cmd.getOptionValue(second)));
		file = new File(new File(cmd.getOptionValue(fileName)).getAbsolutePath());
	}

	private void showCommandline() {
		options.getOptions().stream().forEach(options -> log.info(options.toString()));
	}

	private void writeCpt() throws IOException {
		Files.writeString(file.toPath(), cpt.valueString(), StandardCharsets.UTF_8);
	}

	private void runCountDown() throws InterruptedException, IOException {
		while (!cpt.isFishied()) {
			log.info(cpt.valueString());
			cpt.decrement();
			Thread.sleep(1000);
			this.writeCpt();
		}
	}

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws ParseException, IOException, InterruptedException {
		CoundDown cd = new CoundDown(args);

		cd.showCommandline();
		cd.writeCpt();
		cd.runCountDown();
	}

}
