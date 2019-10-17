package main;

import java.sql.*;

public interface ConnectionMaker {
    public Connection makeConnection() throws  ClassNotFoundException, SQLException;
}
