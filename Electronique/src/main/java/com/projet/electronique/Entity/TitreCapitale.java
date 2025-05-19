package com.projet.electronique.Entity;


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
public class TitreCapitale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relation avec Emetteur
    private String emetteur;

    private String codeBVMT;
    private String codeISIN;

    //Relation avec Groupe
    private String secteurNational;

    private String nature;
    private String nominal;
    private String libellefr;
    private String libelleCfr;
    private String libellear;
    private String libelleCar;
    private String libelleen;
    private String libelleCen;

    private String mnemonique;

    //Relation avec Devises
    private String devise;

    private Date dateDebutCotation;
    private Date dateFinCotation;
    private Date demarrageCotation;
    private String coursIntroduction;
    private Double nbrtitreadmis;
    private String compartiment;

    private Integer nmbrTitre;
    private Double maxAutorise;

}
