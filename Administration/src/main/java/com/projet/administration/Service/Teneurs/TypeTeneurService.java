package com.projet.administration.Service.Teneurs;

import com.projet.administration.Entity.Teneur.TypeTeneur;
import com.projet.administration.Interface.Teneurs.ITypeTeneur;
import com.projet.administration.Repository.Teneurs.TypeTeneurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeTeneurService implements ITypeTeneur {


    private final TypeTeneurRepository typeTeneurRepository;


    @Override
    public TypeTeneur addTypeTeneur(TypeTeneur typeTeneur) {
        return typeTeneurRepository.save(typeTeneur);
    }

    @Override
    public TypeTeneur updateTypeTeneurWithId(Long id, TypeTeneur typeTeneur) {
        return typeTeneurRepository.save(typeTeneur);
    }

    @Override
    public List<TypeTeneur> getAllTypeTeneur() {
        return typeTeneurRepository.findAll();
    }

    @Override
    public String deleteTypeTeneur(Long id) {
        typeTeneurRepository.deleteById(id);
        return "TypeTeneur deleted with id: " + id;
    }
}
