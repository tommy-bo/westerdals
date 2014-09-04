/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.tommybo.westerdals.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Tommy Bø
 */
public class DatabaseSetupIT {
	private static final String FINN_POSTKONTOR_I_DATABASEN = "Tøyen";

	private Connection connection;

	@Before
	public void getConnection() throws SQLException {
		MysqlDataSource ds = new MysqlDataSource();
		ds.setDatabaseName("test");
		ds.setServerName("localhost");
		ds.setUser("student");
		ds.setPassword("student");
		connection = ds.getConnection();
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
			if(FINN_POSTKONTOR_I_DATABASEN.equals(navn)) {
				return;
			}
		}
		Assertions.fail("Fant ikke Tøyen i databasen");
	}
}
