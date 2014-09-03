/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.tommybo.westerdals.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Tommy Bø
 */
public class DatabaseSetupIT {

	private Connection connection;

	@BeforeClass
	public static void setUpClass() throws ClassNotFoundException {
		Class.forName("org.apache.derby.jdbc.ClientDriver");
	}

	@Before
	public void getConnection() throws SQLException {
		connection = DriverManager.getConnection("jdbc:derby://localhost/memory:test", "sa", "pw");
	}

	@After
	public void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	@Test
	public void shouldHaveDataInPostOfficeTable() throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("select * from post_kontor");
		while(rs.next()) {
			String navn = rs.getString("navn");
			System.out.println("Fant " + navn);
			if("Tøyen".equals(navn)) {
				return;
			}
		}
		Assertions.fail("Fant ikke Tøyen i databasen");
	}
}
