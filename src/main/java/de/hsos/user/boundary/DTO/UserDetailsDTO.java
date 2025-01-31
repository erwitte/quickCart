package de.hsos.user.boundary.DTO;

public record UserDetailsDTO(
        String firstName,
        String lastName,
        String street,
        String houseNumber,
        String zipCode,
        String city
        ) {
}
