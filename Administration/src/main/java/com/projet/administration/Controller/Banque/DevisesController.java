package com.projet.administration.Controller.Banque;

import com.projet.administration.Entity.Banque.Devises;
import com.projet.administration.Repository.Banque.DevisesRepository;
import com.projet.administration.Service.Banque.DeviseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devises")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class DevisesController {

    protected final DeviseService deviseService;

    @Operation(summary = "Get all Devises", description = "Returns a list of all Devises entities")
    @GetMapping("/all")
    public ResponseEntity<List<Devises>> getAllDevises() {
        return ResponseEntity.ok(deviseService.getAllDevises());
    }

    @Operation(summary = "add Devises")
    @PostMapping("/add")
    public ResponseEntity<Devises> addDevises(@RequestBody Devises devises) {
        Devises createdDevises = deviseService.addDevises(devises);
        return new ResponseEntity<>(createdDevises, HttpStatus.CREATED);
    }


    @Operation(summary = "update Devises")
    @PutMapping("/update/{id}")
    public ResponseEntity<Devises> updateDevises(@PathVariable Long id, @RequestBody Devises devises) {
        Devises updatedDevises = deviseService.updateDevisesWithId(id, devises);
        return updatedDevises != null ? ResponseEntity.ok(updatedDevises) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete Devises")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteDevises(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(deviseService.deleteDevises(id), HttpStatus.OK);

    }
}
