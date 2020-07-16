package com.locus.auth.validator;

import com.locus.auth.dao.InMemoryDb;
import com.locus.auth.dto.ValidationDto;
import com.locus.auth.enums.ActionType;
import com.locus.auth.enums.ValidationError;
import com.locus.auth.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AccessValidator {

    private final static List<BaseValidator> accessValidationList = List.of(new RequestValidation(), new AccessValidation());

    public static Optional<ValidationError> validateAccess(ValidationDto obj) {
        return accessValidationList
                .stream()
                .map((validator) -> validator.validate(obj))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    private static class RequestValidation implements BaseValidator {
        @Override
        public Optional<ValidationError> validate(ValidationDto obj) {
            if (null == obj || null == obj.getUser() || null == obj.getAction()) {
                return Optional.of(ValidationError.BAD_REQUEST);
            }
            return Optional.empty();
        }
    }

    private static class AccessValidation implements BaseValidator {
        @Override
        public Optional<ValidationError> validate(ValidationDto obj) {
            User user = obj.getUser();
            ActionType action = obj.getAction();
            Long resourceId = obj.getResourceId();
            boolean allowed = user.getRoles()
                    .stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .filter(perm -> perm.getAction().equals(action))
                    .anyMatch(perm -> perm.getResourceIdSet().contains(resourceId));
            return allowed ? Optional.empty() : Optional.of(ValidationError.getValidationError(action));
        }
    }
}

interface BaseValidator {
    public Optional<ValidationError> validate(ValidationDto obj);
}