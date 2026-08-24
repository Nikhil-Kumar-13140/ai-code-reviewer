package com.nikhil.aicodereviewer.service;
import com.nikhil.aicodereviewer.model.ValidationResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikhil.aicodereviewer.model.AiReviewResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.util.Map;

@Service
public class GeminiService {

    private final RestClient restClient;
    private final String apiKey;
    private final String apiUrl;
    private final ObjectMapper objectMapper;

    public GeminiService(
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.api.url}") String apiUrl,
            ObjectMapper objectMapper) {

        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.objectMapper = objectMapper;

        this.restClient = RestClient.builder().build();
    }

    public AiReviewResult reviewCode(
        String code,
        String language,
        ValidationResult validationResult) {

        String prompt = """
                You are an expert software engineer and professional code reviewer.

                You are reviewing %s code.

                IMPORTANT:
                You MUST analyze the ACTUAL CODE provided below.
                Do NOT invent problems.
                Do NOT say "no bugs" if the code contains a compilation or syntax error.

                Your analysis must identify REAL problems in the supplied code.

                ==================================================
                1. SUMMARY
                ==================================================

                Explain what the code is intended to do.

                ==================================================
                2. BUGS AND COMPILATION/RUNTIME ISSUES
                ==================================================

                Carefully check for:

                - syntax errors
                - compilation errors
                - undeclared variables
                - undefined functions
                - missing semicolons
                - incorrect types
                - invalid operators
                - incorrect conditions
                - null pointer risks
                - runtime exceptions
                - array/index errors
                - incorrect input handling
                - logical errors
                - edge cases

                For EVERY issue:

                Explain:
                - what is wrong
                - where it occurs
                - why it is wrong
                - how it should be fixed

                If the program cannot compile, explicitly say that.

                Example:

                "Line 6: 'a' is not declared. The program will fail to compile because the compiler cannot resolve the identifier 'a'. It should probably be 'x'."

                If there are genuinely no problems, return an empty array.

                ==================================================
                3. SECURITY
                ==================================================

                Check the actual code for:

                - hardcoded secrets
                - unsafe input handling
                - injection vulnerabilities
                - insecure APIs
                - sensitive information exposure
                - unsafe file operations
                - unsafe database operations

                Only report REAL issues.

                If there are none, return an empty array.

                ==================================================
                4. CODE QUALITY
                ==================================================

                Check for:

                - poor naming
                - unnecessary code
                - readability problems
                - duplicated logic
                - poor structure
                - maintainability problems
                - inappropriate coding practices

                Only report meaningful issues.

                If there are none, return an empty array.

                ==================================================
                5. PERFORMANCE
                ==================================================

                Check the ACTUAL CODE for:

                - inefficient algorithms
                - unnecessary loops
                - unnecessary memory usage
                - expensive operations
                - scalability problems

                Do NOT invent performance problems.

                If there are none, return an empty array.

                ==================================================
                6. IMPROVED CODE
                ==================================================

                Provide a COMPLETE corrected version of the entire program.

                IMPORTANT:

                - Fix ALL compilation errors.
                - Fix ALL syntax errors.
                - Fix REAL logical errors.
                - Preserve the original intended functionality.
                - Do not omit code.
                - Use proper indentation.
                - Use idiomatic %s practices.
                - The improved code MUST actually compile if possible.
                - Do NOT replace valid code unnecessarily.
                - Do NOT introduce new errors.

                ==================================================
                7. WHY THE CODE WAS IMPROVED
                ==================================================

                Explain specifically what was changed and why.

                Do NOT give generic statements like:

                "Better readability."

                Instead say things such as:

                "Changed 'a' to 'x' because 'a' was never declared."

                "Added the missing semicolon after the cout statement because the original code would not compile."

                "Changed cout << x to std::cout << x to avoid relying on 'using namespace std'."

                Return this explanation inside the codeQuality array.

                ==================================================
                RESPONSE FORMAT
                ==================================================

                Return ONLY valid JSON.

                Do NOT use Markdown.
                Do NOT use ```json.
                Do NOT add explanations outside the JSON.

                Use exactly this structure:

                {
                  "summary": "string",
                  "bugs": [
                    "string"
                  ],
                  "securityIssues": [
                    "string"
                  ],
                  "codeQuality": [
                    "string"
                  ],
                  "performance": [
                    "string"
                  ],
                  "improvedCode": "complete corrected source code"
                }

                IMPORTANT JSON RULES:

                - Escape quotation marks correctly.
                - Use valid JSON.
                - Keep improvedCode as a single JSON string.
                - Newlines inside improvedCode must be represented as valid JSON escaped newlines.
                - Do NOT write Markdown code fences.

                ==================================================
                LANGUAGE
                ==================================================

                %s

                ==================================================
                CODE TO REVIEW
                ==================================================

                %s
                """.formatted(
                language,
                language,
                language,
                code
        );

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                },
                "generationConfig", Map.of(
                        "responseMimeType", "application/json"
                )
        );

        Map<String, Object> response = restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        try {

            String jsonText = extractText(response);

            /*
             * Sometimes the AI response can contain Markdown fences.
             * Remove them before parsing JSON.
             */
            jsonText = cleanJsonResponse(jsonText);

            AiReviewResult result =
                    objectMapper.readValue(jsonText, AiReviewResult.class);

            /*
             * Normalize escaped newlines/tabs in improved code.
             */
            if (result.getImprovedCode() != null) {

                String improvedCode = result.getImprovedCode();

                improvedCode = improvedCode
                        .replace("\\r\\n", "\n")
                        .replace("\\n", "\n")
                        .replace("\\r", "\n")
                        .replace("\\t", "\t");

                result.setImprovedCode(improvedCode);
            }

            return result;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Gemini response: " + e.getMessage(),
                    e
            );
        }
    }

    private String cleanJsonResponse(String jsonText) {

        if (jsonText == null) {
            throw new RuntimeException("Gemini returned an empty response.");
        }

        jsonText = jsonText.trim();

        /*
         * Remove ```json ... ``` if Gemini accidentally returns Markdown.
         */
        if (jsonText.startsWith("```json")) {
            jsonText = jsonText.substring(7).trim();
        }

        if (jsonText.startsWith("```")) {
            jsonText = jsonText.substring(3).trim();
        }

        if (jsonText.endsWith("```")) {
            jsonText = jsonText.substring(
                    0,
                    jsonText.length() - 3
            ).trim();
        }

        return jsonText;
    }

    private String extractText(Map<String, Object> response) {

        try {

            JsonNode root = objectMapper.valueToTree(response);

            JsonNode candidates =
                    root.path("candidates");

            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new RuntimeException(
                        "Gemini response did not contain candidates."
                );
            }

            JsonNode textNode = candidates
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            if (textNode.isMissingNode() || textNode.isNull()) {

                throw new RuntimeException(
                        "Gemini response did not contain generated text."
                );
            }

            return textNode.asText();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to extract Gemini response: "
                            + e.getMessage(),
                    e
            );
        }
    }
}