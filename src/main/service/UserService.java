package main.service;

import main.domain.User;

public interface UserService {
    void add (User user);
    void upgradeLevels();
}
