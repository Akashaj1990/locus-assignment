package com.locus.auth.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString
public class User {

    private Long userId;

    private String userName;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    private UserInfo userInfo;

    private LastAccess lastAccess;

    private ActionHistory<?> history;

    public User addRole(Role role) {
        roles.add(role);
        return this;
    }

    private static class UserInfo {
        private String contactNo;
        private String emailId;
        private String address;
    }

    private static class LastAccess {
        private Date lastAccess;
    }

    private static class ActionHistory<T> {
        private Resource<T> resource;
    }

}
