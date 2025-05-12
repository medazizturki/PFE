package com.projet.administration.Entity.GestionReferentiels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NatureReferentiels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeBvmt;
    private String libelle;
    @ManyToOne
    private Groupes groupes;
    private String libellefr;
    private String libellear;
    private String libelleen;
    private String description;


}
