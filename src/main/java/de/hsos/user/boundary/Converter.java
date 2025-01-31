package de.hsos.user.boundary;

import de.hsos.user.boundary.DTO.UserDetailsDTO;
import de.hsos.user.entity.User;

public class Converter {
    public static UserDetailsDTO userToUserDetailsDTO(User user) {
        return new UserDetailsDTO(user.getFirstName(), user.getLastName(), user.getStreet(), user.getHouseNumber(), user.getZipCode(), user.getCity());
    }
}
