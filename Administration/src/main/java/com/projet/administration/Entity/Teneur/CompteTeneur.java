package com.projet.administration.Entity.Teneur;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projet.administration.Entity.Banque.Devises;
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
public class CompteTeneur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String code;
    private String libelle;

    @ManyToOne
    @JsonIgnoreProperties("comptes") // ignore la liste pour éviter récursion inverse
    private TypeTeneur typeTeneur;


}
