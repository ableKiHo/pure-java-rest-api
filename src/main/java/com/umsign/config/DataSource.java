package com.umsign.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource hikariDataSource ;

    static {
        config.setJdbcUrl("jdbc:mysql://localhost/springbook?useSSL=false&amp;characterEncoding=UTF-8");
        config.setUsername("root");
        config.setPassword("1234");
        config.setMaximumPoolSize(5);
        hikariDataSource = new HikariDataSource(config);
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

}
