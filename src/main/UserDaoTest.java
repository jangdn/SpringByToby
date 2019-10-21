package main;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = "/applicationContextTest.xml")
public class UserDaoTest {
    UserDao dao;

    private User user;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        System.out.println(this);
        dao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/testdb", "jangdn_user", "akdntm90", true);
        dao.setDataSource(dataSource);
        this.user = new User("waiteship", "안장우", "married");
        this.user2 = new User("wbjdgus", "정현", "akdntm93");
        this.user3 = new User("zztcndl", "수이", "ak90");
    }

    @Test
    public void addAndGet() {

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
    public void count() {

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
    public void getUserFailure() {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_ID");
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> userList0 = dao.getAll();
        assertThat(userList0.size(), is(0));

        dao.add(user);
        List<User> userList = dao.getAll();
        assertThat(userList.size(), is(1));
        checkSameUser(user, userList.get(0));

        dao.add(user2);
        List<User> userList1 = dao.getAll();
        assertThat(userList1.size(), is(2));
        checkSameUser(user, userList1.get(0));
        checkSameUser(user2, userList1.get(1));

        dao.add(user3);
        List<User> userList2 = dao.getAll();
        assertThat(userList2.size(), is(3));
        checkSameUser(user, userList2.get(0));
        checkSameUser(user2, userList2.get(1));
        checkSameUser(user3, userList2.get(2));

    }

    private void checkSameUser(User user, User vsUser) {
        assertThat(user.getId(), is(vsUser.getId()));
        assertThat(user.getPassword(), is(vsUser.getPassword()));
        assertThat(user.getName(), is(vsUser.getName()));
    }
}
