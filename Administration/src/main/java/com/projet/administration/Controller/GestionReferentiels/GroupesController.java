package com.projet.administration.Controller.GestionReferentiels;


import com.projet.administration.Entity.GestionReferentiels.Groupes;
import com.projet.administration.Repository.GestionReferentiels.GroupeRepository;
import com.projet.administration.Service.GestionReferentiels.GroupesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupes")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class GroupesController {

    private final GroupesService groupesService;

    @Operation(summary = "Get all Groupes", description = "Returns a list of all Groupes entities")
    @GetMapping("/all")
    public ResponseEntity<List<Groupes>> getAllGroupes() {
        return ResponseEntity.ok(groupesService.getAllGroupes());
    }

    @Operation(summary = "add Groupes")
    @PostMapping("/add")
    public ResponseEntity<Groupes> addGroupes(@RequestBody Groupes groupes) {
        Groupes createdGroupes = groupesService.addGroupes(groupes);
        return new ResponseEntity<>(createdGroupes, HttpStatus.CREATED);
    }


    @Operation(summary = "update Groupes")
    @PutMapping("/update/{id}")
    public ResponseEntity<Groupes> updateGroupes(@PathVariable Long id, @RequestBody Groupes groupes) {
        Groupes updatedGroupes = groupesService.updateGroupesWithId(id, groupes);
        return updatedGroupes != null ? ResponseEntity.ok(updatedGroupes) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete Groupes")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteGroupes(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(groupesService.deleteGroupes(id), HttpStatus.OK);

    }
}
