package com.mk.BackendQuiz.exception;

public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    ENTITY_EXCEPTION("exception"),
    DUPLICATE_ENTITY("duplicate"),
    UNAUTHORIZED_ENTITY("unauthorized");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
}
