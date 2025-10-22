package org.olamide.academicrecordmanagementsystem.service.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.olamide.academicrecordmanagementsystem.dto.ai.QuizQuestionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public GeminiAIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public List<QuizQuestionDTO> generateQuiz(String topic, int numberOfQuestions, String difficulty) {
        String prompt = String.format(
                "Generate exactly %d multiple-choice questions about '%s' with %s difficulty level. " +
                        "Return ONLY a valid JSON array with this exact structure, no additional text: " +
                        "[{\"question\": \"question text\", \"options\": [\"option1\", \"option2\", \"option3\", \"option4\"], \"correctAnswer\": \"option1\", \"explanation\": \"why this is correct\"}]",
                numberOfQuestions, topic, difficulty
        );

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        Map<String, String> part = new HashMap<>();

        part.put("text", prompt);
        content.put("parts", List.of(part));
        requestBody.put("contents", List.of(content));

        try {
            Map<String, Object> response = webClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            String jsonResponse = extractTextFromResponse(response);
            return parseQuizQuestions(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate quiz: " + e.getMessage(), e);
        }
    }

    private String extractTextFromResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    private List<QuizQuestionDTO> parseQuizQuestions(String jsonResponse) {
        try {
            // Remove markdown code blocks if present
            jsonResponse = jsonResponse.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();

            return objectMapper.readValue(jsonResponse, new TypeReference<List<QuizQuestionDTO>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse quiz questions: " + e.getMessage() + "\nResponse: " + jsonResponse, e);
        }
    }
}