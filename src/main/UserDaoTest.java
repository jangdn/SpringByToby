package main;

import main.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.junit.Test;
import org.junit.Before;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContextTest.xml")
//@DirtiesContext
public class UserDaoTest {
//    @Autowired
    UserDao dao;

    private User user;
    private User user2;
    private User user3;

//    @Autowired
//    private ApplicationContext context;

    @Before
    public void setUp(){
//        System.out.println(this.context);
        System.out.println(this);
        dao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/testdb", "jangdn_user", "akdntm90", true);
        dao.setDataSource(dataSource);
        this.user  = new User("whiteship", "안장우", "married");
        this.user2 = new User("wjdgus", "정현", "akdntm93");
        this.user3 = new User("tndl", "수이", "ak90");
    }
    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException{
//        ConnectionMaker connectionMaker = new DConnectionMaker();
//
//        UserDao dao = new DaoFactory().userDao();
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User usertarget1 = dao.get(user.getId());

        assertThat(usertarget1.getName(), is(user.getName()));
        assertThat(usertarget1.getPassword(), is(user.getPassword()));

        User usertarget2 = dao.get(user2.getId());
        assertThat(usertarget2.getName(), is(user2.getName()));
        assertThat(usertarget2.getPassword(), is(user2.getPassword()));

//        if(!user.getName().equals(user2.getName())){
//            System.out.println("테스트 실패 (name)");
//        }
//        else if (!user.getPassword().equals(user2.getPassword())){
//            System.out.println("테스트 실패 (password)");
//        }
//        else {
//            System.out.println("조회 테스트 성공!");
//        }
//        System.out.println(user2.getName());
//        System.out.println(user2.getPassword());
//        System.out.println(user2.getId() + " 조회 성공");

//        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
//        System.out.println("Connection counter : " + ccm.getCounter());
    }

    @Test
    public void count() throws SQLException {
//        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        assertThat(dao.getCount(), is(1));
        dao.add(user2);
        assertThat(dao.getCount(), is(2));
        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
//        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//
//        UserDao dao = context.getBean("userDao", UserDao.class);
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_ID");
    }
}
