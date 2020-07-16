package com.locus.auth.dto;

import com.locus.auth.enums.ActionType;
import com.locus.auth.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ValidationDto {
    private User user;
    private ActionType action;
    private Long resourceId;

}
