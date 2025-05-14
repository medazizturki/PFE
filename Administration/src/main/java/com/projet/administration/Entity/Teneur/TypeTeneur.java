package com.projet.administration.Entity.Teneur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class TypeTeneur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String libelle;

    @OneToMany(mappedBy = "typeTeneur", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("typeTeneur")
    private List<CompteTeneur> comptes;

}
