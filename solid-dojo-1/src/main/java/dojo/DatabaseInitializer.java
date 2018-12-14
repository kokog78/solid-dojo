package dojo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

	public void run() throws SQLException {
		createLicensesTable();
	}
	
	private void createLicensesTable() throws SQLException {
		try (Connection conn = DriverManager.getConnection(LicenseJob.JDBC_URL)) {
			Statement stmt = conn.createStatement();
			stmt.execute("create table licenses (id varchar(15), type varchar(30), owner varchar(100), valid date)");
		}
	}
	
	public static void main(String[] args) throws SQLException {
		DatabaseInitializer initializer = new DatabaseInitializer();
		initializer.run();
	}
	
}
