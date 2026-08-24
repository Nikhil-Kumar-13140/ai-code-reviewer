package com.nikhil.aicodereviewer.model;

import java.util.List;

public class ValidationResult {

    private boolean valid;
    private List<String> errors;

    public ValidationResult() {
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}