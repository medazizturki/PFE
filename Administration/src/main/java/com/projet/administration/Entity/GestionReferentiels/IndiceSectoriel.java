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
public class IndiceSectoriel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeIsin;
    private String codeICB;
    private String groupe;
    private String mnemonique;
    private String libellefr;
    private String libellear;
    private String libelleen;
    private String description;
}
