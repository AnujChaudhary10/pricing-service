package com.service.pricing.configDB;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Profile("db")
@Configuration
public class HsqlDataSource {
	//jdbc:hsqldb:mem:testdb
	
	 private static final String CREATE_SQL_PATH = "db/sql/create-db.sql";
	 private static final String INSERT_SQL_PATH = "db/sql/insert-data.sql";
		@Bean
		public DataSource dataSource() {
			
			EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
			EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).addScript(CREATE_SQL_PATH).addScript(INSERT_SQL_PATH).build();
			return db;
		}

}
