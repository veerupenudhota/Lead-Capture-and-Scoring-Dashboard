package com.pvb.springboot.veeru.lead_capture.controller;

import com.pvb.springboot.veeru.lead_capture.dto.LeadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock-ai")
public class MockAIController {

    @Value("${openai.api.key}")
    private String openAiApiKey;
    private static final String OPENAI_MODEL = "gpt-4o-mini-2024-07-18";
    private final RestTemplate restTemplate;
    public MockAIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @PostMapping("/score")
    public ResponseEntity<Map<String, Object>> getScore(@RequestBody LeadRequest leadRequest) {
        System.out.println("--- AI CALL DEBUGGING ---");

        if (openAiApiKey == null || openAiApiKey.trim().isEmpty()) {
            System.err.println("OpenAI API Key is not configured. Please set 'openai.api.key' in application.properties.");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("score", 0);
            errorResponse.put("category", "api_key_missing");
            return ResponseEntity.internalServerError().body(errorResponse);
        }

        String prompt = String.format(
            "Based on the budget of ₹%.0f and location '%s', classify the lead and give a score (0–100) and a category like hot/warm/cold. Respond ONLY with a JSON object like this: {\"score\": 85, \"category\": \"hot\"}",
            leadRequest.getBudget(), leadRequest.getLocation()
        );

        String apiUrl = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        System.out.println("Sending request to OpenAI with prompt: " + prompt);
        System.out.println("Using API Key: ******" + openAiApiKey.substring(openAiApiKey.length() - 4)); 
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", OPENAI_MODEL); 
        requestBody.put("messages", Collections.singletonList(message));
        requestBody.put("temperature", 0.7); 
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");

                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> messageContent = (Map<String, Object>) firstChoice.get("message");
                    String content = (String) messageContent.get("content");

                    System.out.println("Received raw AI content: " + content);

                    try {
                        
                        Map<String, Object> aiResult;
                        if (content != null && content.startsWith("{") && content.endsWith("}")) {
                            aiResult = new HashMap<>();
                            String[] parts = content.substring(1, content.length() - 1).split(",");
                            for (String part : parts) {
                                String[] keyValue = part.split(":", 2); 
                                if (keyValue.length == 2) {
                                    String key = keyValue[0].trim().replaceAll("\"", "");
                                    String value = keyValue[1].trim().replaceAll("\"", "");
                                    if ("score".equals(key)) {
                                        aiResult.put(key, Integer.parseInt(value));
                                    } else {
                                        aiResult.put(key, value);
                                    }
                                }
                            }
                        } else {
                            System.err.println("AI content not in expected JSON format: " + content);
                            aiResult = new HashMap<>(); 
                        }
                        Integer score = ((Number) aiResult.getOrDefault("score", 0)).intValue();
                        String category = (String) aiResult.getOrDefault("category", "unknown");

                        Map<String, Object> result = new HashMap<>();
                        result.put("score", score);
                        result.put("category", category);
                        return ResponseEntity.ok(result);

                    } catch (Exception e) {
                        System.err.println("Error parsing AI response content: " + content + ". Error: " + e.getMessage());
                        e.printStackTrace();
                        Map<String, Object> fallback = new HashMap<>();
                        fallback.put("score", 0);
                        fallback.put("category", "parsing_error");
                        return ResponseEntity.internalServerError().body(fallback);
                    }
                }
            }
            System.err.println("OpenAI API call failed or returned empty response. Status: " + response.getStatusCode() + ", Body: " + response.getBody());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("score", 0);
            errorResponse.put("category", "ai_response_error");
            return ResponseEntity.internalServerError().body(errorResponse);

        } catch (Exception e) {
            System.err.println("Error calling OpenAI API: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("score", 0);
            errorResponse.put("category", "api_call_exception");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}