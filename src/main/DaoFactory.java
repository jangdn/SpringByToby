package main;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.*;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/jangdn");
        dataSource.setUsername("jangdn_user");
        dataSource.setPassword("akdntm90");

        return dataSource;
    }

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }
//
//    @Bean
//    public UserDao userDao() {
//        UserDao userDao = new UserDao();
//        userDao.setConnectionMaker(connectionMaker());
//        return userDao;
//    }
//
//    @Bean
//    public ConnectionMaker connectionMaker(){
//        return new DConnectionMaker();
//        // return new ProductionDBConnectionMaker();
//    }
}
