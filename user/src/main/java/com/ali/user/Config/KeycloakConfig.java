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
    final static String clientId = "GestionUser";
    final static String clientSecret = "gOC4CjPF2b3KCjcdQYWR2wDVBQC8k94p";
    final static String userName = "admin";
    final static String password = "password";

    public KeycloakConfig(){
    }
    @Bean
    public static Keycloak getKeycloakInstance(){
        if(keycloak == null){
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder.newBuilder()
                            .build())
                    .build();
        }
        return keycloak ;
    }
    @Bean
    public static UsersResource getUsersResource() {
        return getKeycloakInstance().realm(realm).users();
    }
}