
import main.service.UserLevelUpgradeMy;
import main.service.UserLevelUpgradePolicy;
import main.service.UserService;
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


import static main.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static main.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContextTest.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;

    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
            new User("joden", " 병진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
            new User("caden", " 주은", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("sawod", " 민수", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
            new User("evan", " 지은", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("changwoo", " 고수", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
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

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }
    private void checkLevelUpgraded (User user, boolean upgraded) {
        User userUpdate = userService.userDao.get(user.getId());
        if(upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        }
        else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void add() {
        userService.userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userService.userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userService.userDao.get(userWithoutLevel.getId());
        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    @Test
    public void upgradeAllOrNothing() {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userService.userDao);
        testUserService.userDao.deleteAll();
        for(User user:users) testUserService.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch (TestUserServiceException e) {

        }

        checkLevelUpgraded(users.get(1), false);
    }

    static class TestUserService extends UserService{
        private String id ;
        private UserLevelUpgradePolicy userLevelUpgradePolicy;
        public TestUserService(String id){
            this.id = id;
            userLevelUpgradePolicy = new UserLevelUpgradeMy();
        }

        @Override
        public void upgradeLevels() {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
                    if (user.getId().equals(this.id)) throw new TestUserServiceException();
                    user.upgradeLevel();
                    userDao.update(user);
                }
            }
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }
}

