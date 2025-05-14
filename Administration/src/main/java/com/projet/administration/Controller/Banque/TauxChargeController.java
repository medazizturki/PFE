    package com.projet.administration.Controller.Banque;


    import com.projet.administration.Entity.Banque.TauxCharge;
    import com.projet.administration.Service.Banque.TauxChargeService;
    import io.swagger.v3.oas.annotations.Operation;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/TauxCharge")
    @RequiredArgsConstructor
    @CrossOrigin("http://localhost:4200")
    public class TauxChargeController {

        private final TauxChargeService tauxChargeService;

        @Operation(summary = "Get all TauxCharge", description = "Returns a list of all TauxCharge entities")
        @GetMapping("/all")
        public ResponseEntity<List<TauxCharge>> getAllTauxCharge() {
            return ResponseEntity.ok(tauxChargeService.getAllTauxCharge());
        }

        @Operation(summary = "add TauxCharge")
        @PostMapping("/add")
        public ResponseEntity<TauxCharge> addTauxCharge(@RequestBody TauxCharge tauxCharge) {
            TauxCharge createdTauxCharge = tauxChargeService.addTauxCharge(tauxCharge);
            return new ResponseEntity<>(createdTauxCharge, HttpStatus.CREATED);
        }


        @Operation(summary = "update TauxCharge")
        @PutMapping("/update/{id}")
        public ResponseEntity<TauxCharge> updateTauxCharge(@PathVariable Long id, @RequestBody TauxCharge tauxCharge) {
            TauxCharge updatedTauxCharge = tauxChargeService.updateTauxChargeWithId(id, tauxCharge);
            return updatedTauxCharge != null ? ResponseEntity.ok(updatedTauxCharge) : ResponseEntity.notFound().build();
        }

        @Operation(summary = "Delete TauxCharge")
        @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> deleteTauxCharge(@PathVariable (value = "id") Long id) {

            return new ResponseEntity<>(tauxChargeService.deleteTauxCharge(id), HttpStatus.OK);

        }
    }
