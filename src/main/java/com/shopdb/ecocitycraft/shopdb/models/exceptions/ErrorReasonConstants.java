package com.shopdb.ecocitycraft.shopdb.models.exceptions;

public interface ErrorReasonConstants {
    String RESOURCE_NOT_FOUND_BY_NAME = "%s '%s' not found";
    String RESOURCE_NOT_FOUND_BY_ID = "%s with ID %d not found";
    String RESOURCE_ALREADY_EXISTS = "%s '%s' already exists";
    String UNAUTHORIZED = "Unauthorized";
    String INVALID_SERVER = "Server '%s' not valid. Valid servers are: 'main', 'main-east', 'main-north'";
}
