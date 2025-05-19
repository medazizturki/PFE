package com.projet.administration.Entity.GestionReferentiels;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Intermediaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String libellefr;
    private String symbolefrancais;
    private String libellear;
    private String symbolearabe;
    private String libelleen;
    private String symboleanglais;
    private String adresse;
    private String fax;
    private String telephone;
    private String CEO;
    private String comptebanquaire;
    private String banque;
    private String typebanque;
    private Date dateDebut;
    private Date dateFin;
}
