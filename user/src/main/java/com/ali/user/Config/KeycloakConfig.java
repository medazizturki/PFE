package com.ali.user.Config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KeycloakConfig {

    static Keycloak keycloak = null;
    final static String serverUrl = "http://localhost:8080";
    public final static String realm = "GestionUser";
    final static String adminUsername = "admin"; // Your admin username
    final static String adminPassword = "password"; // Your admin password

    public KeycloakConfig(){
    }

    @Bean
    public static Keycloak getKeycloakInstance(){
        if(keycloak == null){
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm("master")  // Always use master realm for admin access
                    .clientId("admin-cli")  // Use admin-cli client (built-in)
                    .username(adminUsername)
                    .password(adminPassword)
                    .grantType(OAuth2Constants.PASSWORD)
                    .build();
        }
        return keycloak ;
    }


    @Bean
    public static UsersResource getUsersResource() {
        return getKeycloakInstance().realm(realm).users();
    }
}