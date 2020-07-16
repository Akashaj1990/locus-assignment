package com.locus.auth.model;

import com.locus.auth.enums.ActionType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString
public class Permission<T> {

    private Long permissionId;
    private String permissionName;
    private ActionType action;

    @Builder.Default
    private Set<Long> resourceIdSet = new HashSet<>();

    public Permission<T> addResourceId(Long resourceId) {
        resourceIdSet.add(resourceId);
        return this;
    }
}
