package de.hsos.user.gateway;

import de.hsos.cart.cotrol.CartService;
import de.hsos.user.control.UserService;
import de.hsos.user.entity.User;
import de.hsos.user.gateway.DTO.UserJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class UserRepository implements PanacheRepository<UserJPAEntity>, UserService {
    @Inject
    CartService cartService;

    @Override
    public User getUser(String username) {
        UserJPAEntity userJPAEntity = find("username", username).firstResult();
        return new User(userJPAEntity.getUsername(), userJPAEntity.getPassword(), userJPAEntity.getFirstName(),
                userJPAEntity.getLastName(), userJPAEntity.getEmail(), userJPAEntity.getStreet(), userJPAEntity.getCity(),
                userJPAEntity.getZipCode(), userJPAEntity.getHouseNumber(), userJPAEntity.getCurrency());
    }

    @Transactional
    @Override
    public void createUser(User user) {
        UserJPAEntity userJPAEntity = new UserJPAEntity(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getStreet(), user.getCity(), user.getZipCode(), user.getHouseNumber(), user.getCurrency());
        userJPAEntity.persist();
        cartService.createCart(user.getUsername());
    }

    @Override
    public long getUserId(String username) {
        UserJPAEntity userJPAEntity = find("username", username).firstResult();
        return userJPAEntity.id;
    }
}
