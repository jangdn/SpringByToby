package main.service;

import main.dao.UserDao;
import main.main;
import main.user.Level;
import main.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Arrays;


import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContextTest.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;

    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
            new User("joden", " 병진", "p1", Level.BASIC, 49, 0),
            new User("caden", " 주은", "p2", Level.BASIC, 50, 0),
            new User("sawod", " 민수", "p3", Level.SILVER, 60, 29),
            new User("evan", " 지은", "p4", Level.SILVER, 60, 30),
            new User("changwoo", " 고수", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void bean(){
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    public void upgradeLevels(){
        userService.userDao.deleteAll();
        for(User user : users) userService.userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }
    private void checkLevel (User user, Level expectedLevel) {
        User userUpdate = userService.userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }
}
