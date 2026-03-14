package com.bridgelabz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

public class ConnectionPool {

    private static ConnectionPool instance;
    private static final Deque<Connection> pool = new ArrayDeque<>();
    private final String url;
    private final String username;
    private final String password;
    private final int MAX_CONNECTIONS = 10;

    private ConnectionPool() {
        ApplicationConfig config = ApplicationConfig.getInstance();
        url = config.getDbUrl();
        username = config.getDbUsername();
        password = config.getDbPassword();

        try {
            Class.forName(config.getDbDriver());
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                pool.addLast(DriverManager.getConnection(url, username, password));
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize Connection Pool", e);
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public static synchronized Connection getConnection() {
        if (pool.isEmpty()) {
            throw new RuntimeException("All connections are in use");
        }
        return pool.removeFirst();
    }

    public synchronized void releaseConnection(Connection connection) {
        pool.addLast(connection);
    }

    public synchronized int availableConnections() {
        return pool.size();
    }
}