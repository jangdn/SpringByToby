package main;

import java.sql.*;

public class DConnectionMaker implements ConnectionMaker{

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/jangdn", "jangdn_user", "akdntm90"
        );
        return c;
    }
}
