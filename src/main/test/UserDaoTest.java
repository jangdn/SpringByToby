import main.dao.UserDaoJdbc;
import main.user.Level;
import main.user.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = "/applicationContextTest.xml")
public class UserDaoTest {
    @Autowired
    private UserDaoJdbc dao;
    @Autowired
    private DataSource dataSource;

    private User user;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        System.out.println(this);
        dao = new UserDaoJdbc();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/testdb", "jangdn_user", "akdntm90", true);
        dao.setDataSource(dataSource);
        this.user = new User("waiteship", "안장우", "married", Level.GOLD, 1, 0);
        this.user2 = new User("wbjdgus", "정현", "akdntm93", Level.SILVER, 50, 100);
        this.user3 = new User("zztcndl", "수이", "ak90", Level.BASIC, 24, 1000);
    }

    @Test
    public void addAndGet() {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User usertarget1 = dao.get(user.getId());
        checkSameUser(user, usertarget1);

        assertThat(usertarget1.getName(), is(user.getName()));
        assertThat(usertarget1.getPassword(), is(user.getPassword()));

        User usertarget2 = dao.get(user2.getId());
        checkSameUser(user2, usertarget2);
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

    @Test(expected = DataAccessException.class)
    public void duplciateKey(){
        dao.deleteAll();

        dao.add(user);
        dao.add(user);
    }

    @Test
    public void update(){
        dao.deleteAll();

        dao.add(user);
        dao.add(user2);

        user.setName("끌끌");
        user.setPassword("pring");
        user.setLevel(Level.BASIC);
        user.setLogin(900);
        user.setRecommend(3000);

        dao.update(user);

        User userupdate = dao.get(user.getId());
        checkSameUser(user, userupdate);

        User usernotupdate = dao.get(user2.getId());
        checkSameUser(user2, usernotupdate);
    }

    private void checkSameUser(User user, User vsUser) {
        assertThat(user.getId(), is(vsUser.getId()));
        assertThat(user.getPassword(), is(vsUser.getPassword()));
        assertThat(user.getName(), is(vsUser.getName()));
        assertThat(user.getLevel(), is(vsUser.getLevel()));
        assertThat(user.getLogin(), is(vsUser.getLogin()));
        assertThat(user.getRecommend(), is(vsUser.getRecommend()));
    }

}
