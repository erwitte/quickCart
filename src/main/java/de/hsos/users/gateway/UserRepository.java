package de.hsos.users.gateway;

import de.hsos.users.control.UserService;
import de.hsos.users.entity.User;
import de.hsos.users.gateway.DTO.UserJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

@RequestScoped
public class UserRepository implements PanacheRepository<UserJPAEntity>, UserService {

    @Transactional
    @Override
    public void createUser(User user) {
        UserJPAEntity userJPAEntity = new UserJPAEntity(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getStreet(), user.getCity(), user.getZipCode(), user.getHouseNumber(), user.getCurrency());
        userJPAEntity.persist();
    }
}
