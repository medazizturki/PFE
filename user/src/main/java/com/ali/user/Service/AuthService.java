package com.ali.user.Service;

import com.ali.user.Config.Credentials;
import com.ali.user.Config.KeycloakConfig;
import com.ali.user.Model.LoginRequest;
import com.ali.user.Model.LoginResponse;
import com.ali.user.Model.User;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
@PropertySource("classpath:application.yaml")
public class AuthService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String FACE_AUTH_PASSWORD = "face-auth-secret-password";


    @Autowired
    AuthService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${spring.security.oauth2.client.registration.ouath2-client-credentials.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.ouath2-client-credentials.client-secret}")
    private String clientSecret;

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", loginRequest.getUsername());
        map.add("password", loginRequest.getPassword());
        map.add("scope", "openid email profile");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        try {
            LoginResponse response = restTemplate.postForObject(
                    "http://localhost:8080/realms/GestionUser/protocol/openid-connect/token",
                    httpEntity,
                    LoginResponse.class
            );

            if (response != null && response.getAccess_token() != null) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to authenticate: " + e.getResponseBodyAsString());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    public boolean logoutUser(String userId) {
        try {
            KeycloakConfig.getKeycloakInstance().realm("GestionUser").users().get(userId).logout();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void signup(User user) {
        // Use the FACE_AUTH_PASSWORD for all new users if you want them to support face auth
        CredentialRepresentation credential = Credentials.createPasswordCredentials(user.getPassword());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setCredentials(Collections.singletonList(credential));
        userRepresentation.setEnabled(true);

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("image", Collections.singletonList(user.getImage()));
        attributes.put("adresse", Collections.singletonList(user.getAdresse()));
        attributes.put("sexe", Collections.singletonList(user.getSexe().toString()));
        attributes.put("phone", Collections.singletonList(String.valueOf(user.getPhone())));
        attributes.put("verified", Collections.singletonList(String.valueOf(user.getVerified())));

        userRepresentation.setAttributes(attributes);

        UsersResource usersResource = KeycloakConfig.getUsersResource();
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            System.out.println("User added successfully");
            String userId = CreatedResponseUtil.getCreatedId(response);
            System.out.println("User ID: " + userId);
            sendVerificationEmail(userId);
        } else {
            System.out.println("Failed to add user. Status: " + response.getStatus());
        }

        response.close();
    }


    public void sendVerificationEmail(String userId) {
        UsersResource usersResource = KeycloakConfig.getUsersResource();
        try {
            usersResource.get(userId).sendVerifyEmail();
            System.out.println("Verification email sent successfully.");
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isEmailVerified(String username) {
        UsersResource usersResource = KeycloakConfig.getUsersResource();
        List<UserRepresentation> users = usersResource.search(username, true);

        if (!users.isEmpty()) {
            UserRepresentation user = users.get(0);
            return user.isEmailVerified();
        }

        return false;
    }

    public void updateUser(String userId, User user) {
        UsersResource usersResource = KeycloakConfig.getUsersResource();
        UserResource userResource = usersResource.get(userId);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());

        Map<String, List<String>> updatedAttributes = new HashMap<>();
        updatedAttributes.put("image", Collections.singletonList(user.getImage()));
        updatedAttributes.put("adresse", Collections.singletonList(user.getAdresse()));
        updatedAttributes.put("sexe", Collections.singletonList(user.getSexe().toString()));
        updatedAttributes.put("phone", Collections.singletonList(String.valueOf(user.getPhone())));
        updatedAttributes.put("verified", Collections.singletonList(String.valueOf(user.getVerified())));
        userRepresentation.setAttributes(updatedAttributes);
        userResource.update(userRepresentation);
        System.out.println("User updated successfully");
    }

    public void updateUserRole(String userId, String role) {
        UsersResource usersResource = KeycloakConfig.getUsersResource();
        UserResource userResource = usersResource.get(userId);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        attributes.put("role", Collections.singletonList(role));
        userRepresentation.setAttributes(attributes);

        userResource.update(userRepresentation);
        System.out.println("User role updated successfully");
    }

    public void updateUserWithEmail(String email, User user) {
        UsersResource usersResource = KeycloakConfig.getUsersResource();
        List<UserRepresentation> userRepresentations = usersResource.search(email, true);

        if (userRepresentations.isEmpty()) {
            System.out.println("User not found with email: " + email);
            return;
        }
        UserRepresentation userRepresentation = userRepresentations.get(0);
        String userId = userRepresentation.getId();
        UserResource userResource = usersResource.get(userId);
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        Map<String, List<String>> updatedAttributes = new HashMap<>();
        updatedAttributes.put("image", Collections.singletonList(user.getImage()));
        updatedAttributes.put("adresse", Collections.singletonList(user.getAdresse()));
        updatedAttributes.put("sexe", Collections.singletonList(user.getSexe().toString()));
        updatedAttributes.put("phone", Collections.singletonList(String.valueOf(user.getPhone())));
        updatedAttributes.put("verified", Collections.singletonList(String.valueOf(user.getVerified())));

        userRepresentation.setAttributes(updatedAttributes);

        userResource.update(userRepresentation);
        System.out.println("User updated successfully with email: " + email);
    }

    public boolean deleteUser(String userId) {
        try {
            KeycloakConfig.getKeycloakInstance().realm("GestionUser").users().delete(userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserRepresentation> getAllUsers(String realmName) {
        Keycloak keycloak = KeycloakConfig.getKeycloakInstance();
        UsersResource usersResource = keycloak.realm(realmName).users();
        return usersResource.list();
    }

    public List<UserRepresentation> getAllUsersWithRole(String realmName, String roleName) {
        Keycloak keycloak = KeycloakConfig.getKeycloakInstance();
        UsersResource usersResource = keycloak.realm(realmName).users();
        List<UserRepresentation> allUsers = usersResource.list();
        List<UserRepresentation> usersWithRole = allUsers.stream()
                .filter(user -> hasRole(user, roleName))
                .collect(Collectors.toList());
        return usersWithRole;
    }

    private boolean hasRole(UserRepresentation user, String roleName) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes != null) {
            List<String> roles = attributes.get("role");
            return roles != null && roles.contains(roleName);
        } else {
            return false;
        }
    }

    public boolean updatePassword(String email, String currentPassword, String newPassword) {
        try {
            Keycloak keycloak = KeycloakConfig.getKeycloakInstance();
            UsersResource usersResource = keycloak.realm("GestionUser").users();
            List<UserRepresentation> users = usersResource.search(email);
            if (users.isEmpty()) {
                return false;
            }
            UserRepresentation user = users.get(0);
            UserResource userResource = usersResource.get(user.getId());

            if (!verifyCurrentPassword(userResource, currentPassword)) {
                return false;
            }
            CredentialRepresentation newCredential = new CredentialRepresentation();
            newCredential.setType(CredentialRepresentation.PASSWORD);
            newCredential.setValue(newPassword);
            newCredential.setTemporary(false);
            userResource.resetPassword(newCredential);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean verifyCurrentPassword(UserResource userResource, String currentPassword) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl("http://localhost:8080")
                    .realm("GestionUser")
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(userResource.toRepresentation().getUsername())
                    .password(currentPassword)
                    .resteasyClient(org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder.newBuilder()
                            .build())
                    .build();

            AccessTokenResponse response = keycloak.tokenManager().getAccessToken();
            return response != null;
        } catch (Exception e) {
            System.out.println("Waaaaa: ");
            return false;
        }
    }

    public void updateUserAttributes(String userId, Map<String, List<String>> attributesToUpdate) {
        try {
            UsersResource usersResource = KeycloakConfig.getUsersResource();
            UserResource userResource = usersResource.get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();

            Map<String, List<String>> existingAttributes = userRepresentation.getAttributes();
            if (existingAttributes == null) {
                existingAttributes = new HashMap<>();
            }

            Map<String, List<String>> validatedAttributes = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : attributesToUpdate.entrySet()) {
                List<String> validatedValues = entry.getValue().stream()
                        .map(value -> value.length() > 255 ? value.substring(0, 255) : value)
                        .collect(Collectors.toList());
                validatedAttributes.put(entry.getKey(), validatedValues);
            }

            existingAttributes.putAll(validatedAttributes);
            userRepresentation.setAttributes(existingAttributes);

            userResource.update(userRepresentation);
            System.out.println("User attributes updated successfully");
        } catch (Exception e) {
            System.err.println("Failed to update user attributes: " + e.getMessage());
            throw new RuntimeException("Failed to update user attributes", e);
        }
    }

    public List<UserSessionRepresentation> getUserSessions(String realmName, String userId) {
        Keycloak keycloak = KeycloakConfig.getKeycloakInstance();
        UsersResource usersResource = keycloak.realm(realmName).users();
        UserResource userResource = usersResource.get(userId);
        List<UserSessionRepresentation> userSessions = userResource.getUserSessions();

        return userSessions;
    }

    public boolean disableUser(String userId) {
        try {
            UsersResource usersResource = KeycloakConfig.getUsersResource();
            UserResource userResource = usersResource.get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            userRepresentation.setEnabled(false);
            userResource.update(userRepresentation);
            System.out.println("User disabled successfully with ID: " + userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to disable user with ID: " + userId);
            return false;
        }
    }

    public boolean enableuser(String userId) {
        try {
            UsersResource usersResource = KeycloakConfig.getUsersResource();
            UserResource userResource = usersResource.get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            userRepresentation.setEnabled(true);
            userResource.update(userRepresentation);
            System.out.println("User disabled successfully with ID: " + userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to disable user with ID: " + userId);
            return false;
        }
    }

    public boolean isUserEnabled(String userId) {
        UsersResource usersResource = KeycloakConfig.getUsersResource();
        UserResource userResource = usersResource.get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        return userRepresentation.isEnabled();
    }

    private String getAccessToken(LoginResponse loginResponse) {
        return loginResponse.getAccess_token();
    }

    public boolean isUserLoggedIn(String userId) {
        try {
            List<UserSessionRepresentation> sessions = getUserSessions("GestionUser", userId);
            return !sessions.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "http://localhost:8080/realms/GestionUser/protocol/openid-connect/userinfo",
                    HttpMethod.GET,
                    requestEntity,
                    Map.class
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> getUserInfo(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            System.out.println("Making request to userinfo endpoint with token: " + token.substring(0, 20) + "...");

            ResponseEntity<Map> response = restTemplate.exchange(
                    "http://localhost:8080/realms/GestionUser/protocol/openid-connect/userinfo",
                    HttpMethod.GET,
                    requestEntity,
                    Map.class
            );

            System.out.println("Response status: " + response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error calling userinfo endpoint: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasActiveSession(String username) {
        try {
            UsersResource usersResource = KeycloakConfig.getUsersResource();
            List<UserRepresentation> users = usersResource.search(username, true);

            if (users.isEmpty()) {
                return false;
            }

            String userId = users.get(0).getId();
            List<UserSessionRepresentation> sessions = getUserSessions("GestionUser", userId);

            return !sessions.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateTokenForFaceAuth(String username) {
        try {
            // First verify the user exists
            UsersResource usersResource = KeycloakConfig.getUsersResource();
            List<UserRepresentation> users = usersResource.search(username, true);

            if (users.isEmpty()) {
                throw new RuntimeException("User not found: " + username);
            }

            // Get the user's current attributes
            UserRepresentation user = users.get(0);
            Map<String, List<String>> attributes = user.getAttributes();

            // Check if face descriptor exists (optional)
            if (attributes == null || !attributes.containsKey("faceDescriptor")) {
                throw new RuntimeException("User has no registered face");
            }

            // Generate token using fixed face auth password
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "password");
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("username", username);
            map.add("password", FACE_AUTH_PASSWORD);
            map.add("scope", "openid email profile");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            LoginResponse response = restTemplate.postForObject(
                    "http://localhost:8080/realms/GestionUser/protocol/openid-connect/token",
                    request,
                    LoginResponse.class
            );

            if (response == null) {
                throw new RuntimeException("Keycloak returned null response");
            }

            return response.getAccess_token();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate face auth token: " + e.getMessage(), e);
        }
    }



}
