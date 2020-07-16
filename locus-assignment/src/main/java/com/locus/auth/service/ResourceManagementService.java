package com.locus.auth.service;

import com.locus.auth.enums.ActionType;
import com.locus.auth.exception.AccessValidationException;
import com.locus.auth.model.Resource;
import com.locus.auth.model.User;

public interface ResourceManagementService {

    public Resource<?> readResource(Long resourceId, User user) throws AccessValidationException;

    public void updateResource(Resource<?> resource, User user) throws AccessValidationException;

    public void deleteResource(Long resourceId, User user) throws AccessValidationException;

}
