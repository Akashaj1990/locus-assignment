package com.locus.auth.service;

import com.locus.auth.dao.InMemoryDb;
import com.locus.auth.dto.ValidationDto;
import com.locus.auth.enums.ActionType;
import com.locus.auth.enums.ValidationError;
import com.locus.auth.exception.AccessValidationException;
import com.locus.auth.model.Resource;
import com.locus.auth.model.User;
import com.locus.auth.processor.AsynchTaskProcessor;
import com.locus.auth.validator.AccessValidator;

import java.util.Optional;

public class ResourceManagementServiceImpl implements ResourceManagementService {

    private static final InMemoryDb database = InMemoryDb.getInstance();

    private ResourceManagementServiceImpl() {

    }

    @Override
    public Resource<?> readResource(Long resourceId, User user) throws AccessValidationException {
        Optional<ValidationError> error = AccessValidator.validateAccess(ValidationDto.builder()
                .action(ActionType.READ)
                .user(user)
                .resourceId(resourceId).build());

        if (error.isPresent()) {
            throw new AccessValidationException(error.get());
        }

        return database.getResource(resourceId);
    }

    @Override
    public void updateResource(Resource<?> resource, User user) throws AccessValidationException {
        Optional<ValidationError> error = AccessValidator.validateAccess(ValidationDto.builder()
                .action(ActionType.WRITE)
                .user(user)
                .resourceId(resource.getResourceId()).build());

        if (error.isPresent()) {
            throw new AccessValidationException(error.get());
        }
        //updates if exists and add if not exists Asynchronously
        AsynchTaskProcessor.submitTask(AsynchTaskProcessor
                .SaveWorker
                .builder()
                .resource(resource)
                .callback(() -> System.out.println("Updated Successfully"))
                .build());
    }

    @Override
    public void deleteResource(Long resourceId, User user) throws AccessValidationException {
        Optional<ValidationError> error = AccessValidator.validateAccess(ValidationDto.builder()
                .action(ActionType.DELETE)
                .user(user)
                .resourceId(resourceId).build());

        if (error.isPresent()) {
            throw new AccessValidationException(error.get());
        }
        //updates if exists and add if not exists
        AsynchTaskProcessor.submitTask(AsynchTaskProcessor
                .DeleteWorker
                .builder()
                .resourceId(resourceId)
                .callback(() -> System.out.println("Deleted Successfully"))
                .build());
    }

    private final static class ResourceManagementServiceHolder {
        private final static ResourceManagementService INSTANCE =
                new ResourceManagementServiceImpl();
    }

    public static ResourceManagementService getInstance() {
        return ResourceManagementServiceHolder.INSTANCE;
    }
}
