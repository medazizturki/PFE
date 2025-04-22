package com.ali.user.Controller;

import com.ali.user.Config.KeycloakConfig;
import com.ali.user.Model.*;
import com.ali.user.Model.LoginRequest;
import com.ali.user.Model.LoginResponse;
import com.ali.user.Model.PasswordUpdateRequest;
import com.ali.user.Model.User;
import com.ali.user.Service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*"
)
@AllArgsConstructor
@Slf4j
public class AuthController {
    @Autowired
    AuthService loginservice;

//    private UsersProducer usersProducer;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginrequest) {
        logger.info( "User logged in successfully !");

        return loginservice.login(loginrequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestParam String userId) {
        boolean result = loginservice.logoutUser(userId);
        if (result) {
            return ResponseEntity.ok("User successfully logged out.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to logout user.");
        }
    }


    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        logger.info("New user signup: username={}, email={}", user.getUserName(), user.getEmail());
        loginservice.signup(user);

        // 🔹 Rechercher l'utilisateur nouvellement créé
        List<UserRepresentation> representationList = KeycloakConfig.getUsersResource().search(user.getUserName(), true);

        if (!representationList.isEmpty()) {
            UserRepresentation userRepresentation = representationList.get(0);

            if (Boolean.FALSE.equals(userRepresentation.isEmailVerified())) {
                // 🔹 Envoyer l'email de vérification
                loginservice.sendVerificationEmail(userRepresentation.getId());
            }
        }

        return "User added successfully! Please check your email for verification.";
    }

    // Envoyer un email de vérification après l'inscription
    @PostMapping("/users/{userId}/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(@PathVariable String userId) {
        try {
            loginservice.sendVerificationEmail(userId);
            return ResponseEntity.ok("Verification email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification email.");
        }
    }

    // Vérifier si l'email de l'utilisateur est validé
    @GetMapping("/users/{username}/is-email-verified")
    public ResponseEntity<Boolean> isEmailVerified(@PathVariable String username) {
        try {
            boolean isVerified = loginservice.isEmailVerified(username);
            return ResponseEntity.ok(isVerified);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/users/{id}")
    public UserRepresentation getUserById(@PathVariable String id) {
        return KeycloakConfig.getKeycloakInstance().realm("GestionUser").users().get(id).toRepresentation();
    }
    @GetMapping("/role")
    public List<UserRepresentation> getAllUsersWithRole() {
        return loginservice.getAllUsersWithRole("GestionUser","psychiatre");
    }


    @GetMapping("/users")
    public List<UserRepresentation> getAllUsers() {
        return loginservice.getAllUsers("GestionUser");
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable String userId) {
        boolean result = loginservice.deleteUser(userId);
        if (result) {
            return "User successfully deleted.";
        } else {
            return "Failed to delete user.";
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable String userId, @RequestBody User user) {
        try {
            loginservice.updateUser(userId, user);
            return ResponseEntity.ok().body("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update user: " + e.getMessage());
        }
    }

    @PostMapping("users/{userId}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable String userId, @RequestParam String role) {
        try {
            loginservice.updateUserRole(userId, role);
            return ResponseEntity.ok("User role updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user role");
        }
    }
    @PutMapping("update/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody User user) {
        try {
            loginservice.updateUserWithEmail(email, user);
            return ResponseEntity.ok().body("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update user: " + e.getMessage());
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest request) {
        boolean success = loginservice.updatePassword(request.getEmail(), request.getCurrentPassword(), request.getNewPassword());
        if (success) {
            logger.info("Password update success: email={}", request.getEmail());
            return ResponseEntity.ok().body("Password updated successfully");
        } else {
            logger.warn("Password update failed: email={}", request.getEmail());
            return ResponseEntity.badRequest().body("Failed to update password. Current password might be incorrect.");
        }
    }

    @GetMapping("/user/{userId}/sessions")
    public ResponseEntity<List<UserSessionRepresentation>> getUserSessions(@PathVariable String userId) {
        try {
            List<UserSessionRepresentation> sessions = loginservice.getUserSessions("GestionUser", userId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/disable/{userId}")
    public ResponseEntity<?> disableUser(@PathVariable String userId) {
        boolean isDisabled = loginservice.disableUser(userId);
        if (isDisabled) {
            logger.info("User disabled: userId={}", userId);
            return ResponseEntity.ok("User disabled successfully");
        } else {
            logger.error("Failed to disable user: userId={}", userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to disable user");
        }
    }

    @PostMapping("/enable/{userId}")
    public ResponseEntity<?> enableuser(@PathVariable String userId) {
        boolean isEnabled = loginservice.enableuser(userId);
        if (isEnabled) {
            return ResponseEntity.ok("User enabled successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to enable user");
        }
    }

    @GetMapping("/{userId}/status")
    public ResponseEntity<Boolean> getUserStatus(@PathVariable String userId) {
        try {
            boolean isEnabled = loginservice.isUserEnabled(userId);
            return ResponseEntity.ok(isEnabled);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
