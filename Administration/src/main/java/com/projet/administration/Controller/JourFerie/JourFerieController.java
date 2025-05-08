package com.projet.administration.Controller.JourFerie;
import com.projet.administration.Entity.JourFerie.JourFerie;
import com.projet.administration.Service.JourFerie.JourFerieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/JourFerie")
@RequiredArgsConstructor
public class JourFerieController {

    private final JourFerieService jourFerieService;



    @Operation(summary = "Get all Jour", description = "Returns a list of all Jour entities")
    @GetMapping("/all")
    public ResponseEntity<List<JourFerie>> getAllJourFerie() {
        return ResponseEntity.ok(jourFerieService.getAllJourFerie());
    }

    @Operation(summary = "add JourFerie")
    @PostMapping("/add")
    public ResponseEntity<JourFerie> addJourFerie(@RequestBody JourFerie jourFerie) {
        JourFerie createdJourFerie = jourFerieService.addJourFerie(jourFerie);
        return new ResponseEntity<>(createdJourFerie, HttpStatus.CREATED);
    }


    @Operation(summary = "update JourFerie")
    @PutMapping("/update/{id}")
    public ResponseEntity<JourFerie> updateJourFerie(@PathVariable Long id, @RequestBody JourFerie jourFerie) {
        JourFerie updatedJourFerie = jourFerieService.updateJourFerieWithId(id, jourFerie);
        return updatedJourFerie != null ? ResponseEntity.ok(updatedJourFerie) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete JourFerie")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteJourFerie(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(jourFerieService.deleteJourFerie(id), HttpStatus.OK);

    }
}
