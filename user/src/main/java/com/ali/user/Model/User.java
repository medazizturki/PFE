package com.ali.user.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String image;
    private String adresse;
    private Sexe sexe;
    private Integer phone;
    private Boolean verified;
    private String faceData; // Add this field for face recognition

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", adresse='" + adresse + '\'' +
                ", sexe=" + sexe +
                ", phone=" + phone +
                ", verified=" + verified +
                ", faceData='" + (faceData != null ? "[face data present]" : "null") + '\'' +
                '}';
    }
}
