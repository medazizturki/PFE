package com.ali.user.Model;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String keycloakName;

    Role(String keycloakName) {
        this.keycloakName = keycloakName;
    }

    public String getKeycloakName() {
        return keycloakName;
    }

    public static Role fromKeycloakName(String keycloakName) {
        for (Role role : values()) {
            if (role.keycloakName.equalsIgnoreCase(keycloakName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + keycloakName);
    }
}