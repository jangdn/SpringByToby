package main.dao;

import main.user.User;

import java.util.List;


public interface UserDao {

    public void add(final User user);

    public User get(String id);

    public List<User> getAll();

    public void update(User user);

    public void deleteAll();

    public int getCount();
}
