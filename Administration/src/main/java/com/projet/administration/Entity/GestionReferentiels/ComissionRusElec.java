package com.projet.administration.Entity.GestionReferentiels;

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
public class ComissionRusElec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupe;
    private Double rang;
    private Double tauxCTB;
    private Double valeurmaxCTB;
    private Double valeurminCTB;
    private Double tauxRUS;
    private Double valeurmaxRUS;
    private Double valeurminRUS;
}
