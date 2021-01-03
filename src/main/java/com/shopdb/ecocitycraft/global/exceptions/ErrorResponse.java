package com.shopdb.ecocitycraft.global.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private Timestamp timestamp;
    private int status;
    private String error;
    private String message;
}
