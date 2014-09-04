package com.github.tommybo.westerdals.database;

import com.google.common.collect.Sets;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostkontorMapper {
	public static Postkontor fra(ResultSet rs) throws SQLException {
		return Postkontor.med()
						.navn(rs.getString("navn"))
						.postnummer(Sets.newHashSet(rs.getString("postnummer_liste").split(" ")))
						.build();
	}
}
