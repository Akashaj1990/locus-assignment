package com.locus.auth.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString
public class Role {
    //unique
    private Long roleId;

    //unique
    private String roleName;

    @Builder.Default
    private Set<Permission<?>> permissions = new HashSet<>();

    public Role addPermission(Permission<?> permission) {
        permissions.add(permission);
        return this;
    }

}
