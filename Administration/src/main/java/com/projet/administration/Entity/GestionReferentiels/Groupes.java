package com.projet.administration.Entity.GestionReferentiels;

import com.fasterxml.jackson.annotation.*;
import com.projet.administration.Entity.Banque.Devises;
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
public class Groupes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Enumerated(EnumType.STRING)
    private Marche marche;
    @Enumerated(EnumType.STRING)
    private Categories Categorie;
    private String unitecotation;
    @ManyToOne
    @JsonIgnoreProperties("groupes")
    private Devises devises;
    @Enumerated(EnumType.STRING)
    private ModeCotation modeCotation;
    private String description;


    @OneToMany(mappedBy = "groupes", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("groupes")
    private List<NatureReferentiels> natureReferentielsList;

    public enum ModeCotation {
        Continu,
        Fixing
    }


    public enum Categories {
        Titre_de_Capitale,
        Titre_de_Creance,
        Sukuk,
        Fonds,
        Autres
    }


    public enum Marche {
        Marches_des_Titre_de_capital_Marche_Principale,
        Marches_des_Titre_de_capital_Marche_Alternatif,
        Marches_Obligatoire,
        Marches_des_Fonds,
        Marches_des_Sukuk,
        Marche_Hors_Cote
    }


}
