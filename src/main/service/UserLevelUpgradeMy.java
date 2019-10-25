package main.service;

import main.user.Level;
import main.user.User;

import static main.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static main.service.UserService.MIN_RECOMMEND_FOR_GOLD;

public class UserLevelUpgradeMy implements UserLevelUpgradePolicy {

    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch(currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : " + currentLevel);
        }
    }
    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
    }
}
