package com.locus.auth.enums;

import java.util.Map;

public enum ValidationError {

    READ_NOT_ALLOWED("01", "Read Not Allowed"),
    WRITE_NOT_ALLOWED("02", "Write Not Allowed"),
    DELETE_NOT_ALLOWED("03", "Delete Not Allowed"),
    BAD_REQUEST("04", "Bad Request");

    public final String code;
    public final String msg;

    private static final Map<ActionType, ValidationError> map = Map.of(
            ActionType.READ, READ_NOT_ALLOWED,
            ActionType.WRITE, WRITE_NOT_ALLOWED,
            ActionType.DELETE, DELETE_NOT_ALLOWED);

    public static ValidationError getValidationError(ActionType action) {
        return map.get(action);
    }

    private ValidationError(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ValidationError{");
        sb.append("code='").append(code).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
