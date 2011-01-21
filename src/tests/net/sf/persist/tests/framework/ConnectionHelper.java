
// $Id$

package net.sf.persist.tests.framework;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHelper {
	
	/**
	 * Reads a properties file from the classpath and instantiates a java.sql.Connection with the given parameters.
	 */
	public static Connection getConnection(String propertiesFilePath) {
		
		Properties properties = new Properties();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFilePath);
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException("Could not load " + propertiesFilePath + " from the classpath");
		}
		
		String driver = properties.getProperty("driver");
		String url = properties.getProperty("url");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		
		if (driver==null || driver.trim().equals("")) throw new RuntimeException("driver not specified in " + propertiesFilePath);
		if (url==null || url.trim().equals("")) throw new RuntimeException("url not specified in " + propertiesFilePath);
		if (user==null || user.trim().equals("")) throw new RuntimeException("user not specified in " + propertiesFilePath);
		if (password==null || password.trim().equals("")) throw new RuntimeException("password not specified in " + propertiesFilePath);
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return connection;
	}

}
