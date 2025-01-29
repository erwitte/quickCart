package de.hsos.user.gateway;

import de.hsos.user.control.UserService;
import de.hsos.user.entity.User;
import de.hsos.user.gateway.DTO.UserJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

@RequestScoped
public class UserRepository implements PanacheRepository<UserJPAEntity>, UserService {

    @Override
    public User getUser(String username) {
        UserJPAEntity userJPAEntity = find("username", username).firstResult();
        return new User(userJPAEntity.getUsername(), userJPAEntity.getPassword(), userJPAEntity.getFirstName(),
                userJPAEntity.getLastName(), userJPAEntity.getEmail(), userJPAEntity.getStreet(), userJPAEntity.getCity(),
                userJPAEntity.getZipCode(), userJPAEntity.getHouseNumber(), userJPAEntity.getCurrency());
    }

    @Override
    @Transactional
    public void updateUser(String username, String street, String houseNumber,
                           String zipCode, String city, String currency){
        UserJPAEntity userJPAEntity = find("username", username).firstResult();
        if (street != null) {
            userJPAEntity.setStreet(street);
        }
        if (houseNumber != null) {
            userJPAEntity.setHouseNumber(houseNumber);
        }
        if (zipCode != null) {
            userJPAEntity.setZipCode(zipCode);
        }
        if (city != null) {
            userJPAEntity.setCity(city);
        }
        if (currency != null) {
            userJPAEntity.setCurrency(currency);
        }
        userJPAEntity.persist();
    }

    @Transactional
    @Override
    public void createUser(User user) {
        UserJPAEntity userJPAEntity = new UserJPAEntity(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getStreet(), user.getCity(), user.getZipCode(), user.getHouseNumber(), user.getCurrency());
        userJPAEntity.persist();
    }

    @Override
    public long getUserId(String username) {
        UserJPAEntity userJPAEntity = find("username", username).firstResult();
        return userJPAEntity.id;
    }
}
