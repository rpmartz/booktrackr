package com.ryanpmartz.booktrackr.controller.dto;

import com.ryanpmartz.booktrackr.domain.User;

public class UserDto {

    private final String email;
    private final String firstName;
    private final String lastName;

    public UserDto(final User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
