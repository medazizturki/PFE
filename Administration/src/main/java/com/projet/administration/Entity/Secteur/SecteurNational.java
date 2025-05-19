package com.projet.administration.Entity.Secteur;


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
public class SecteurNational {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String section;
    public String devision;
    public String groupe;
    public String categorie;
    public String libellefr;
    public String libellear;
    public String libelleen;
    public Double partetrangere;
}
