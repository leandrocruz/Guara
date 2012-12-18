package br.com.ibnetwork.guara.mojo;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.handler.TableReplacer;
import br.com.ibnetwork.guara.metadata.impl.PostgreSQL;
import br.com.ibnetwork.guara.metadata.impl.SqlMap;
import br.com.ibnetwork.guara.metadata.impl.SqlMapCrud;

/**
 * @goal generate-database
 * @execute phase="compile"
 * @phase package 
 * @requiresInjectResolution compile
 */
public class GenerateDatabaseMojo
	extends TextGenerator
{
	protected Metadata[] getTasks()
	{
		Metadata[] array = new Metadata[]{
				//create(HsqlDb.class),
				create(PostgreSQL.class, new TableReplacer()),
				create(SqlMapCrud.class),
				create(SqlMap.class)
			};
		return array;
	}

	@Override
    protected void executeMeta(Metadata meta) 
		throws Exception
    {
		generate(meta);
    }

	@Override
    protected void terminate()
		throws Exception
    {
	    //connect on the database and insert the tables
		String driver 	= database.get("driver");
		String url 		= database.get("url");
		String username = database.get("username");
		String password = database.get("password");
		
		getLog().debug("Connecting to: "+url);
		String file = project.getBasedir() + "/src/sql/postgresql/create-tables.sql";
		//executeBatchFromFile(file, driver, url, username, password);
    }

	public static void executeBatchFromFile(String file, String driver, String url, String username, String password)
	    throws Exception
	{
		Class.forName(driver); //register driver class
		Connection conn = DriverManager.getConnection(url, username, password);
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		System.out.println("loading batch from file: "+file);
		String sql = FileUtils.readFileToString(new File(file), "UTF-8");
		String[] cmds = sql.split(";");
		for (int i = 0; i < cmds.length; i++)
		{
			String cmd = cmds[i];
			cmd = StringUtils.trimToNull(cmd);
			if (cmd != null)
			{
				//System.out.println("command: "+cmd);
				stmt.addBatch(cmd + ";");
			}
		}
		int[] result = stmt.executeBatch();
		conn.commit();
		conn.close();
	}

}
