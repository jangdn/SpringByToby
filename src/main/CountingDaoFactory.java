package main;

import org.springframework.context.annotation.*;

@Configuration
public class CountingDaoFactory {

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        return userDao;
    }

    @Bean
    public ConnectionMaker connectionMaker(){
        return new CountingConnectionMaker(realConnectionMaker());
        // return new ProductionDBConnectionMaker();
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DConnectionMaker();
    }
}
