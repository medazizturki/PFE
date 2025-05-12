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
public class CategoriesDavoir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeBVMT;
    private String codeNSC;
    private String codeOptique;
    private String codeTc;
    private String libellefr;
    private String libellecourtefr;
    private String libellear;
    private String libellecourtear;
    private String libelleen;
    private String libellecourteen;
    private Double tauxreductionCTB;
    private Double tauxreductionRUS;
    private Double tauxreductionCEB_ENR;
    private Double tauxreductionRUS_ENR;
}
