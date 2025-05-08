package com.projet.administration.Service.GestionReferentiels;


import com.projet.administration.Entity.GestionReferentiels.Groupes;
import com.projet.administration.Interface.GestionReferentiels.GroupeInterface;
import com.projet.administration.Repository.GestionReferentiels.GroupeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupesService implements GroupeInterface {

    private final GroupeRepository groupeRepository;

    @Override
    public Groupes addGroupes(Groupes groupes) {
        return groupeRepository.save(groupes);
    }

    @Override
    public Groupes updateGroupesWithId(Long id, Groupes groupes) {
        return groupeRepository.save(groupes);
    }

    @Override
    public List<Groupes> getAllGroupes() {
        return groupeRepository.findAll();
    }

    @Override
    public String deleteGroupes(Long id) {
        groupeRepository.deleteById(id);
        return "groupe deleted with id: " + id;
    }
}
