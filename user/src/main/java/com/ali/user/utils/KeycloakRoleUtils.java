package com.ali.user.utils;

import com.ali.user.Model.User;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KeycloakRoleUtils {

    public static List<String> getUserRoles(String userId, RealmResource realmResource) {
        return realmResource.users()
                .get(userId)
                .roles()
                .realmLevel()
                .listAll()
                .stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
    }

    public static void assignRealmRoles(String userId, List<String> roles, RealmResource realmResource) {
        List<RoleRepresentation> roleRepresentations = roles.stream()
                .map(roleName -> realmResource.roles().get(roleName).toRepresentation())
                .collect(Collectors.toList());

        realmResource.users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(roleRepresentations);
    }

    public static void removeRealmRoles(String userId, List<String> roles, RealmResource realmResource) {
        List<RoleRepresentation> roleRepresentations = roles.stream()
                .map(roleName -> realmResource.roles().get(roleName).toRepresentation())
                .collect(Collectors.toList());

        realmResource.users()
                .get(userId)
                .roles()
                .realmLevel()
                .remove(roleRepresentations);
    }

    public static UserRepresentation toKeycloakRepresentation(User user) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(user.getUserName());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(user.getVerified() != null && user.getVerified());
        return userRep;
    }
}