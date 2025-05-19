package com.projet.electronique.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Emetteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Relation avec secteur national
    private String secteurNational;
    //Relation avec secteur intenational
    private String secteurInternational;

    private String codeBVMT;
    private String identifiantUnique;
    private String libellefr;
    private String libelleCfr;
    private String libellear;
    private String libelleCar;
    private String libelleen;
    private String libelleCen;
    private String pays;
    private boolean appelpublic;
    //Relation avec Teneur Compte
    private String teneurregister;

}
