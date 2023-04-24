package com.example.state;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL9Dialect;

public class AppPostgreSQL9Dialect extends PostgreSQL9Dialect{
	public AppPostgreSQL9Dialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}