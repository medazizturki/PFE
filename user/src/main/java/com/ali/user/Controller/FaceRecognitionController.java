package com.ali.user.Controller;

import com.ali.user.Service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/face-recognition")
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@Slf4j
public class FaceRecognitionController {

    private static final Logger logger = LoggerFactory.getLogger(FaceRecognitionController.class);
    private static final double SIMILARITY_THRESHOLD = 0.85; // Adjusted to typical face recognition threshold
    private static final int MIN_DESCRIPTOR_SIZE = 10;

    @Autowired
    private AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/login")
    public ResponseEntity<?> faceLogin(@RequestBody Map<String, Object> requestData) {
        try {
            logger.info("Starting face recognition login");

            // Validate input
            if (!requestData.containsKey("descriptor")) {
                logger.error("Face descriptor missing in request");
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Face descriptor is required"
                ));
            }

            List<Double> inputDescriptor;
            try {
                inputDescriptor = (List<Double>) requestData.get("descriptor");
                if (inputDescriptor == null || inputDescriptor.size() < MIN_DESCRIPTOR_SIZE) {
                    logger.error("Invalid descriptor size: {}", inputDescriptor == null ? "null" : inputDescriptor.size());
                    return ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "message", "Invalid descriptor format or size"
                    ));
                }
            } catch (ClassCastException e) {
                logger.error("Invalid descriptor format", e);
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid descriptor format"
                ));
            }

            // Normalize input descriptor
            inputDescriptor = normalizeDescriptor(inputDescriptor);
            logger.debug("Normalized input descriptor (first 5): {}",
                    inputDescriptor.subList(0, Math.min(5, inputDescriptor.size())));

            // Get all users
            List<UserRepresentation> allUsers = authService.getAllUsers("GestionUser");
            logger.info("Comparing against {} registered users", allUsers.size());

            UserRepresentation bestMatch = null;
            double highestSimilarity = 0;
            int comparisonsMade = 0;

            for (UserRepresentation user : allUsers) {
                try {
                    String storedDescriptorStr = getAttributeValue(user.getAttributes(), "faceDescriptor");
                    if (storedDescriptorStr == null || storedDescriptorStr.isEmpty()) {
                        logger.debug("User {} has no face descriptor", user.getUsername());
                        continue;
                    }

                    List<Double> storedDescriptor = parseDescriptor(storedDescriptorStr);
                    if (storedDescriptor.size() != inputDescriptor.size()) {
                        logger.warn("Descriptor size mismatch for user {}: expected {}, got {}",
                                user.getUsername(), inputDescriptor.size(), storedDescriptor.size());
                        continue;
                    }

                    double similarity = cosineSimilarity(inputDescriptor, storedDescriptor);
                    comparisonsMade++;
                    logger.debug("Similarity with {}: {}", user.getUsername(), similarity);

                    if (similarity > highestSimilarity) {
                        highestSimilarity = similarity;
                        bestMatch = user;
                    }
                } catch (Exception e) {
                    logger.warn("Error processing user {}: {}", user.getUsername(), e.getMessage());
                }
            }

            logger.info("Completed {} comparisons. Highest similarity: {}", comparisonsMade, highestSimilarity);

            if (bestMatch != null && highestSimilarity >= SIMILARITY_THRESHOLD) {
                logger.info("Successful match with user {} (similarity: {})",
                        bestMatch.getUsername(), highestSimilarity);
                String token = authService.generateTokenForFaceAuth(bestMatch.getUsername());

                // Retrieve custom attributes
                String phone = getAttributeValue(bestMatch.getAttributes(), "phone");
                String sexe = getAttributeValue(bestMatch.getAttributes(), "sexe");
                String adresse = getAttributeValue(bestMatch.getAttributes(), "adresse");
                String image = getAttributeValue(bestMatch.getAttributes(), "image");

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("token", token);
                response.put("id", bestMatch.getId());
                response.put("username", bestMatch.getUsername());
                response.put("firstName", bestMatch.getFirstName());
                response.put("lastName", bestMatch.getLastName());
                response.put("email", bestMatch.getEmail());
                response.put("phone", phone);
                response.put("image", image);
                response.put("sexe", sexe);
                response.put("adresse", adresse);
                response.put("similarity", highestSimilarity);

                return ResponseEntity.ok(response);

            }
             else {
                logger.warn("No match found (best similarity: {}, threshold: {})",
                        highestSimilarity, SIMILARITY_THRESHOLD);
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Face not recognized",
                        "bestSimilarity", highestSimilarity,
                        "threshold", SIMILARITY_THRESHOLD
                ));
            }
        } catch (Exception e) {
            logger.error("Face login failed", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    @PostMapping("/register-face/{userId}")
    public ResponseEntity<?> registerFace(@PathVariable String userId, @RequestBody Map<String, Object> requestData) {
        try {
            logger.info("Registering face for user: {}", userId);

            // Validate descriptor
            if (!requestData.containsKey("descriptor")) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Face descriptor is required"
                ));
            }

            List<Double> descriptor;
            try {
                descriptor = (List<Double>) requestData.get("descriptor");
                if (descriptor == null || descriptor.size() < MIN_DESCRIPTOR_SIZE) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "message", "Invalid descriptor format or size"
                    ));
                }
            } catch (ClassCastException e) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid descriptor format"
                ));
            }

            // Normalize and store descriptor
            descriptor = normalizeDescriptor(descriptor);
            String descriptorString = objectMapper.writeValueAsString(descriptor);

            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("faceDescriptor", Collections.singletonList(descriptorString));
            attributes.put("faceRegisteredAt", Collections.singletonList(new Date().toString()));

            authService.updateUserAttributes(userId, attributes);

            logger.info("Successfully registered face for user {}", userId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Face registered successfully",
                    "descriptorSize", descriptor.size()
            ));
        } catch (JsonProcessingException e) {
            logger.error("Descriptor serialization failed", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Descriptor processing failed"
            ));
        } catch (Exception e) {
            logger.error("Registration failed", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Registration failed"
            ));
        }
    }

    // Helper Methods
    private List<Double> normalizeDescriptor(List<Double> descriptor) {
        if (descriptor == null || descriptor.isEmpty()) {
            return descriptor;
        }

        // Calculate L2 norm
        double norm = 0.0;
        for (double val : descriptor) {
            norm += val * val;
        }
        norm = Math.sqrt(norm);

        // Normalize if norm is not zero
        List<Double> normalized = new ArrayList<>(descriptor.size());
        if (norm > 0) {
            for (double val : descriptor) {
                normalized.add(val / norm);
            }
        } else {
            normalized.addAll(descriptor);
        }

        return normalized;
    }

    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        if (v1 == null || v2 == null || v1.size() != v2.size()) {
            return 0;
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
            normA += v1.get(i) * v1.get(i);
            normB += v2.get(i) * v2.get(i);
        }

        // Handle division by zero
        if (normA <= 0 || normB <= 0) {
            return 0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private List<Double> parseDescriptor(String descriptorStr) throws JsonProcessingException {
        return objectMapper.readValue(descriptorStr, new TypeReference<List<Double>>() {});
    }

    private String getAttributeValue(Map<String, List<String>> attributes, String attributeName) {
        if (attributes == null || attributeName == null) {
            return null;
        }
        List<String> values = attributes.get(attributeName);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }
}