package com.projet.administration.Controller.Teneurs;

import com.lowagie.text.pdf.ColumnText;
import com.projet.administration.Entity.Teneur.CompteTeneur;
import com.projet.administration.Service.Teneurs.CompteTeneurService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import com.lowagie.text.Document;
import java.awt.*;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@RestController
@RequestMapping("/compteteneur")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class CompteTeneurController {

    private final CompteTeneurService compteTeneurService;

    @Operation(summary = "Get all CompteTeneur", description = "Returns a list of all CompteTeneur entities")
    @GetMapping("/all")
    public ResponseEntity<List<CompteTeneur>> getAllCompteTeneur() {
        return ResponseEntity.ok(compteTeneurService.getAllCompteTeneur());
    }

    @Operation(summary = "add CompteTeneur")
    @PostMapping("/add")
    public ResponseEntity<CompteTeneur> addTCompteTeneur(@RequestBody CompteTeneur compteTeneur) {
        CompteTeneur created = compteTeneurService.addCompteTeneur(compteTeneur);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @Operation(summary = "update CompteTeneur")
    @PutMapping("/update/{id}")
    public ResponseEntity<CompteTeneur> updateCompteTeneur(@PathVariable Long id, @RequestBody CompteTeneur compteTeneur) {
        CompteTeneur updatedCompteTeneur = compteTeneurService.updateCompteTeneurWithId(id, compteTeneur);
        return updatedCompteTeneur != null ? ResponseEntity.ok(updatedCompteTeneur) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete CompteTeneur")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCompteTeneur(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(compteTeneurService.deleteCompteTeneur(id), HttpStatus.OK);

    }

    @Operation(summary = "Exporter la liste des CompteTeneur au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<CompteTeneur> list = compteTeneurService.getAllCompteTeneur();

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
        Paragraph titlePara = new Paragraph("Liste des Comptes Teneur",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau
        PdfPTable table = new PdfPTable(new float[]{3, 7, 5});
        table.setWidthPercentage(100);

        // En-têtes
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        PdfPCell c1 = new PdfPCell(new Phrase("Code", headFont));
        c1.setBackgroundColor(new Color(41, 128, 185)); c1.setPadding(6);
        PdfPCell c2 = new PdfPCell(new Phrase("Libellé", headFont));
        c2.setBackgroundColor(new Color(41, 128, 185)); c2.setPadding(6);
        PdfPCell c3 = new PdfPCell(new Phrase("Type Teneur", headFont));
        c3.setBackgroundColor(new Color(41, 128, 185)); c3.setPadding(6);
        table.addCell(c1); table.addCell(c2); table.addCell(c3);

        // Lignes
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (CompteTeneur ct : list) {
            table.addCell(new PdfPCell(new Phrase(ct.getCode(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(ct.getLibelle(), cellFont)));
            String typeLibelle = ct.getTypeTeneur() != null
                    ? ct.getTypeTeneur().getLibelle()
                    : "";
            table.addCell(new PdfPCell(new Phrase(typeLibelle, cellFont)));
        }
        document.add(table);
        document.close();

        // 3) Préparer un nom de fichier Windows-safe
        DateTimeFormatter fileNameFmt = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
        String rawTs = LocalDateTime.now().format(fileNameFmt);
        String safeTs = rawTs.replace("/", "-").replace(":", ".");
        String fileName = "comptes-teneur-" + safeTs + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\Compte Teneur";
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
