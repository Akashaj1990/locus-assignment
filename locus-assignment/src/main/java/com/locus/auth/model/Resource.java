package com.locus.auth.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Resource<T> {
    //unique
    private final Long resourceId;
    //unique
    private final String name;
    private final T val;
}
