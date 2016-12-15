package main.java.com.aws;

import java.sql.Connection;
import java.sql.DriverManager;

public class AWSConnectionManager {


	public static Connection getRemoteConnection() throws Exception {

		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String dbName = "mycompany";
			String userName = "aXXHEiVfE34O8TWX";
			String password = "bAgiaifH5N6ztWsd";
			String hostname = "aau9yl99e7iq7e.cnire5ax1omy.us-west-2.rds.amazonaws.com";
			String port = "3306";
			con = DriverManager.getConnection("jdbc:mysql://p-mysql-proxy.run.pivotal.io:3306/cf_236e3b29_1e8c_4782_9c8b_1643103c19ad", userName,password);

		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return con;
	}

}
