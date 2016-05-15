package com.ryanpmartz.booktrackr.domain;

import org.springframework.security.core.GrantedAuthority;

public enum UserRoleEnum implements GrantedAuthority {

    ROLE_USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
