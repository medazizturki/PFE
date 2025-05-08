package com.projet.administration.Controller.Banque;

import com.projet.administration.Entity.Banque.TMM;
import com.projet.administration.Service.Banque.TMMService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tmm")
@RequiredArgsConstructor
public class TMMController {

    private final TMMService tmmService;

    @Operation(summary = "Get all TMM", description = "Returns a list of all TMM entities")
    @GetMapping("/all")
    public ResponseEntity<List<TMM>> getAllTMM() {
        return ResponseEntity.ok(tmmService.getAllTMM());
    }

    @Operation(summary = "add TMM")
    @PostMapping("/add")
    public ResponseEntity<TMM> addTMM(@RequestBody TMM tmm) {
        TMM createdTMM = tmmService.addTMM(tmm);
        return new ResponseEntity<>(createdTMM, HttpStatus.CREATED);
    }


    @Operation(summary = "update TMM")
    @PutMapping("/update/{id}")
    public ResponseEntity<TMM> updateTMM(@PathVariable Long id, @RequestBody TMM tmm) {
        TMM updatedTMM = tmmService.updateTMMWithId(id, tmm);
        return updatedTMM != null ? ResponseEntity.ok(updatedTMM) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete TMM")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTMM(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(tmmService.deleteTMM(id), HttpStatus.OK);

    }
}
