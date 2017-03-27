package ar.edu.itba.bd.pgklint;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;

public class BDTest {

	public static void main(String[] args) throws SQLException, ParseException, IOException {
		ArgsParamsStore params = new ArgsParamsStore(args);
		String host = params.get("host", "localhost");
		String database = params.get("database", null);
		Integer port = Integer.valueOf(params.get("port", "5432"));
		String user = params.get("user", "postgres");
		String password = params.get("password", null);
		String file = params.get("file", null);
		String function = params.get("function", null);

		ConnectionManager connectionManager = new ConnectionManager(host, port, database, user, password);
		Connection connection = connectionManager.hold();
		
		FileInputStream inputStream = new FileInputStream(file);
		try {
		    String everything = IOUtils.toString(inputStream);
		    Statement stmt = connection.createStatement();
		    stmt.execute(everything);
		    stmt.close();
		    if(function != null){
		    	connection.setAutoCommit(false);
		    	DatabaseMetaData db_metadata = connection.getMetaData();
		    	ResultSet procd = db_metadata.getProcedureColumns(null, null, function, "*");
		    	while (procd.next()) {
					String object = procd.getString("COLUMN_NAME");
					//System.out.println(object);
				    
				}
		    	
		    	CallableStatement proc = connection.prepareCall("{ ? = call "+function+"() }");
		    	proc.registerOutParameter(1, Types.OTHER);
				proc.execute();
				SQLWarning warning = proc.getWarnings();

				while (warning != null)
				{
				   System.out.println(warning.getMessage());
				   warning = warning.getNextWarning();
				}
				
				proc.close();
		    }
		    
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    inputStream.close();
		}
		
	}

}
