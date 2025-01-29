package de.hsos.user.control;

import de.hsos.user.entity.User;

public interface UserService {
    public void createUser(User user);
    long getUserId(String username);
    User getUser(String username);
    void updateUser(String username, String street, String houseNumber,
                    String zipCode, String city, String currency);
}
