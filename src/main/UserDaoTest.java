package main;

import org.junit.Test;
import org.junit.Before;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@ContextConfiguration(locations="/applicationContextTest.xml")
public class UserDaoTest {
    UserDao dao;

    private User user;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
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

    }

    @Test
    public void count() throws SQLException {

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
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_ID");
    }
}
