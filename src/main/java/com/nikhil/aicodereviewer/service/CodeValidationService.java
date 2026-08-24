package com.nikhil.aicodereviewer.service;

import com.nikhil.aicodereviewer.model.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CodeValidationService {

    public ValidationResult validate(String code, String language) {

        ValidationResult result = new ValidationResult();

        result.setValid(true);
        result.setErrors(new ArrayList<>());

        if (code == null || code.isBlank()) {
            result.setValid(false);
            result.getErrors().add("Code cannot be empty.");
            return result;
        }

        String lang = language == null
                ? ""
                : language.trim().toLowerCase();

        switch (lang) {

            case "c++":
                validateCpp(code, result);
                break;

            case "java":
                validateJava(code, result);
                break;

            case "python":
                validatePython(code, result);
                break;

            case "c":
                validateC(code, result);
                break;

            case "javascript":
                validateJavaScript(code, result);
                break;

            case "c#":
                validateCSharp(code, result);
                break;

            default:
                result.setValid(false);
                result.getErrors().add(
                        "Unsupported programming language: " + language
                );
        }

        return result;
    }

    private void validateCpp(String code, ValidationResult result) {

        if (code.matches("(?s).*cout\\s*<<\\s*a\\s*;?.*")
                && !code.matches(
                "(?s).*\\b(?:int|long|float|double|char|bool|string)\\s+a\\b.*")) {

            result.setValid(false);

            result.getErrors().add(
                    "Variable 'a' is used in cout but has not been declared."
            );
        }

        if (code.matches(
                "(?s).*cout\\s*<<[^;\\n{}]+\\s*\\n.*")) {

            result.setValid(false);

            result.getErrors().add(
                    "Missing semicolon after the cout statement."
            );
        }

        if (code.contains("int main")
                && !code.contains("return 0;")) {

            result.getErrors().add(
                    "The main function does not explicitly return 0."
            );
        }

        if (!result.getErrors().isEmpty()) {
            result.setValid(false);
        }
    }

    private void validateJava(String code, ValidationResult result) {

        if (code.contains("System.out.println(")
                && !code.matches(
                "(?s).*System\\.out\\.println\\([^;]*\\);.*")) {

            result.setValid(false);

            result.getErrors().add(
                    "Possible missing semicolon after System.out.println()."
            );
        }
    }

    private void validatePython(String code, ValidationResult result) {

        if (code.contains("def ")
                && !code.contains(":")) {

            result.setValid(false);

            result.getErrors().add(
                    "Python function definition appears to be missing ':'."
            );
        }
    }

    private void validateC(String code, ValidationResult result) {

        if (code.contains("printf(")
                && !code.matches(
                "(?s).*printf\\([^;]*\\);.*")) {

            result.setValid(false);

            result.getErrors().add(
                    "Possible missing semicolon after printf()."
            );
        }
    }

    private void validateJavaScript(String code, ValidationResult result) {

        int openBraces = count(code, '{');
        int closeBraces = count(code, '}');

        if (openBraces != closeBraces) {

            result.setValid(false);

            result.getErrors().add(
                    "Mismatched curly braces detected."
            );
        }
    }

    private void validateCSharp(String code, ValidationResult result) {

        if (code.contains("Console.WriteLine(")
                && !code.matches(
                "(?s).*Console\\.WriteLine\\([^;]*\\);.*")) {

            result.setValid(false);

            result.getErrors().add(
                    "Possible missing semicolon after Console.WriteLine()."
            );
        }
    }

    private int count(String text, char character) {

        int count = 0;

        for (char c : text.toCharArray()) {
            if (c == character) {
                count++;
            }
        }

        return count;
    }
}