package de.hsos.user.control;

import de.hsos.user.entity.User;

public interface UserService {
    public void createUser(User user);
    long getUserId(String username);
    User getUser(String username);
}
