package com.todotasks.domain;

import java.util.List;

public class ApiError {
    private List<String> error;

    public ApiError(List<String> error) {
        this.error = error;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }
}
