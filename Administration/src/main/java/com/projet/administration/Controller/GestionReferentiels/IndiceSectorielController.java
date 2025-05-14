package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.Groupes;
import com.projet.administration.Entity.GestionReferentiels.IndiceSectoriel;
import com.projet.administration.Service.GestionReferentiels.IndiceSectorielService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/indicesectoriel")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class IndiceSectorielController {
    private final IndiceSectorielService indicesectorielService;


    @Operation(summary = "Get all IndiceSectoriel", description = "Returns a list of all IndiceSectoriel entities")
    @GetMapping("/all")
    public ResponseEntity<List<IndiceSectoriel>> getAllIndiceSectoriel() {
        return ResponseEntity.ok(indicesectorielService.getAllIndiceSectoriel());
    }

    @Operation(summary = "add IndiceSectoriel")
    @PostMapping("/add")
    public ResponseEntity<IndiceSectoriel> addIndiceSectoriel(@RequestBody IndiceSectoriel indiceSectoriel) {
        IndiceSectoriel createdIndiceSectoriel = indicesectorielService.addIndiceSectoriel(indiceSectoriel);
        return new ResponseEntity<>(indiceSectoriel, HttpStatus.CREATED);
    }


    @Operation(summary = "update IndiceSectoriel")
    @PutMapping("/update/{id}")
    public ResponseEntity<IndiceSectoriel> updateIndiceSectoriel(@PathVariable Long id, @RequestBody IndiceSectoriel indiceSectoriel) {
        IndiceSectoriel updatedIndiceSectoriel = indicesectorielService.updateIndiceSectorielWithId(id, indiceSectoriel);
        return updatedIndiceSectoriel != null ? ResponseEntity.ok(updatedIndiceSectoriel) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete IndiceSectoriel")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteIndiceSectoriel(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(indicesectorielService.deleteIndiceSectoriel(id), HttpStatus.OK);

    }

}
