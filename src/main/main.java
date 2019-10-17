package main;

import org.junit.runner.JUnitCore;

import java.sql.SQLException;

public class main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        JUnitCore.main("main.UserDaoTest");
    }
}
