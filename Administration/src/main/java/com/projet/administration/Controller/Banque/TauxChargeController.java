package com.projet.administration.Controller.Banque;


import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.projet.administration.Entity.Banque.TauxCharge;
import com.projet.administration.Service.Banque.TauxChargeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.io.ByteArrayOutputStream;
import java.util.List;


import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.IOUtils;
import com.lowagie.text.pdf.PdfWriter;

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

    @Operation(summary = "Exporter la liste des TauxCharge au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<TauxCharge> list = tauxChargeService.getAllTauxCharge();

        // 2) Générer le PDF en mémoire
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // 2.a) Logo
        String logoPath = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\src\\assets\\uploads-images\\aaaa.jpeg";
        Image logo = Image.getInstance(logoPath);
        logo.scaleAbsolute(50, 50);
        float pageHeight = document.getPageSize().getHeight();
        // Remonter le logo : 50pt sous le bord supérieur
        logo.setAbsolutePosition(36, pageHeight - 70);
        document.add(logo);

        // 2.b) Date à droite
        String dateStr = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Phrase datePhrase = new Phrase("Généré le : " + dateStr,
                FontFactory.getFont(FontFactory.HELVETICA, 10)
        );
        ColumnText.showTextAligned(
                writer.getDirectContent(),
                Element.ALIGN_RIGHT,
                datePhrase,
                document.getPageSize().getWidth() - 36,
                pageHeight - 40,
                0
        );

        // 2.c) Titre centré
        Paragraph titlePara = new Paragraph("Liste des Taux Charge",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau (Date | Valeur | Variation Annuelle | Devise)
        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3});
        table.setWidthPercentage(100);

        // En-têtes
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        PdfPCell hDate = new PdfPCell(new Phrase("Date", headFont));
        hDate.setBackgroundColor(new Color(41, 128, 185)); hDate.setPadding(6);
        PdfPCell hVal = new PdfPCell(new Phrase("Valeur", headFont));
        hVal.setBackgroundColor(new Color(41, 128, 185)); hVal.setPadding(6);
        PdfPCell hVar = new PdfPCell(new Phrase("Variation Annuelle", headFont));
        hVar.setBackgroundColor(new Color(41, 128, 185)); hVar.setPadding(6);
        PdfPCell hDev = new PdfPCell(new Phrase("Devise", headFont));
        hDev.setBackgroundColor(new Color(41, 128, 185)); hDev.setPadding(6);
        table.addCell(hDate); table.addCell(hVal); table.addCell(hVar);table.addCell(hDev);

        // Lignes
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (TauxCharge tc : list) {
            String dateCell = tc.getDate() != null
                    ? dateFmt.format(tc.getDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                    : "";
            String valCell = tc.getValue() != null
                    ? tc.getValue().toString()
                    : "";
            String varCell = tc.getVariationAnnuelle() != null
                    ? tc.getVariationAnnuelle().toString()
                    : "";
            String devise = tc.getDevises() != null
                    ? tc.getDevises().getCode()
                    : "";
            table.addCell(new PdfPCell(new Phrase(dateCell, cellFont)));
            table.addCell(new PdfPCell(new Phrase(valCell, cellFont)));
            table.addCell(new PdfPCell(new Phrase(varCell, cellFont)));
            table.addCell(new PdfPCell(new Phrase(devise, cellFont)));
        }
        document.add(table);
        document.close();

        // 3) Préparer un nom de fichier safe Windows
        DateTimeFormatter fileNameFmt = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
        String rawTs    = LocalDateTime.now().format(fileNameFmt);
        String safeTs   = rawTs.replace("/", "-").replace(":", ".");
        String fileName = "taux-charge-" + safeTs + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\TauxCharge";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        // 5) Envoyer au client
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }

}
