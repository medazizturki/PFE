package com.projet.administration.Entity.Banque;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.projet.administration.Entity.GestionReferentiels.Groupes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Devises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String libellefr;
    private String libelleCfr;
    private String libellear;
    private String libelleCar;
    private String libelleen;
    private String libelleCen;


    @OneToMany(mappedBy = "devises", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("devises")
    private List<TauxCharge> tauxCharges;

    @OneToMany(mappedBy = "devises", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Groupes> groupes;
}
