package ar.edu.itba.bd.pgklint;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgsParamsStore {
	private CommandLine cmd;

	public ArgsParamsStore(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption("host", true, "Host name");
		options.addOption("database", true, "Database name");
		options.addOption("port", true, "Port number");
		options.addOption("user", true, "User name");
		options.addOption("password", true, "User password");
		options.addOption("file", true, "SQL File with Statement");
		options.addOption("function", true, "SQL File with Statement");
		CommandLineParser parser = new DefaultParser();
		this.cmd = parser.parse(options, args);

	}

	public String get(String key, String default_value){
		if(cmd.hasOption( key)){
			return cmd.getOptionValue( key );
		}
		return default_value;
	}
}
