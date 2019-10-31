package main.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static main.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static main.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.dao.UserDao;
import main.domain.Level;
import main.domain.User;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired UserService userService;
	@Autowired UserDao userDao;
	@Autowired DataSource dataSource;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired UserServiceImpl userServiceImpl;
	@Autowired MailSender mailSender;


	List<User> users;	// test fixture
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "범진이", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0,"jangdn@nate.com"),
				new User("joytouch", "조토이", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "joy1213@naver.com"),
				new User("erwins", "얼인이", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1, "erwins@daum.net"),
				new User("madnite1", "마드니테", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD, "mad@gamil.com"),
				new User("green", "그린이", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "green@naver.com")
				);
	}

	@Test
	public void upgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);

		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();

		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));

		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}
		else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}

	@Test 
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);	  // GOLD ����  
		User userWithoutLevel = users.get(0);  
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);	  
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel())); 
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
 
	@Test
	public void upgradeAllOrNothing() throws Exception {
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao); 
		testUserService.setMailSender(mailSender);

		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);

		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected"); 
		}
		catch(TestUserServiceException e) { 
		}
		
		checkLevelUpgraded(users.get(1), false);
	}

	
	static class TestUserService extends UserServiceImpl {
		private String id;
		
		private TestUserService(String id) {  
			this.id = id;
		}

		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) throw new TestUserServiceException();  
			super.upgradeLevel(user);  
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
	}
}

