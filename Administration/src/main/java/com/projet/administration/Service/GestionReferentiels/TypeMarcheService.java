package com.projet.administration.Service.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.TypeMarche;
import com.projet.administration.Interface.GestionReferentiels.TypeMarcheInterface;
import com.projet.administration.Repository.GestionReferentiels.TypeMarcheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeMarcheService implements TypeMarcheInterface {

    private final TypeMarcheRepository typeMarcheRepository;

    @Override
    public TypeMarche addTypeMarche(TypeMarche typemarche) {
        return typeMarcheRepository.save(typemarche);
    }

    @Override
    public TypeMarche updateTypeMarcheWithId(Long id, TypeMarche typemarche) {
        return typeMarcheRepository.save(typemarche);
    }

    @Override
    public List<TypeMarche> getAllTypeMarche() {
        return typeMarcheRepository.findAll();
    }

    @Override
    public String deleteTypeMarche(Long id) {
        typeMarcheRepository.deleteById(id);
        return "TypeMarche was deleted with id " + id ;
    }
}
