package com.projet.administration.Entity.GestionReferentiels;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeBvmt;
    private String libelle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = false)              // ← force full expansion
    @JsonIgnoreProperties("natureReferentielsList")         // avoid back-reference loop
    private Groupes groupes;

    private String libellefr;
    private String libellear;
    private String libelleen;
    private String description;
}
