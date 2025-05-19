package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.Intermediaire;
import com.projet.administration.Service.GestionReferentiels.IntermediaireService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.awt.*;
import java.util.List;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.ColumnText;

@RestController
@RequestMapping("intermediaire")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class IntermediaireController {


    private final IntermediaireService IntermediaireService;

    @Operation(summary = "Get all Intermediaire")
    @GetMapping("/all")
    public ResponseEntity<List<Intermediaire>> getAllIntermediaire() {
        return ResponseEntity.ok(IntermediaireService.getAllIntermediaire());
    }

    @Operation(summary = "Add a Groupe")
    @PostMapping("/add")    // ← no consumes/produces here
    public ResponseEntity<Intermediaire> addIntermediaire(@RequestBody Intermediaire Intermediaire) {
        Intermediaire created = IntermediaireService.addIntermediaire(Intermediaire);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a Groupe")
    @PutMapping("/update/{id}")  // ← no consumes/produces here
    public ResponseEntity<Intermediaire> updateIntermediaire(
            @PathVariable Long id,
            @RequestBody Intermediaire Intermediaire
    ) {
        Intermediaire updated = IntermediaireService.updateIntermediaireWithId(id, Intermediaire);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a Groupe")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteIntermediaire(@PathVariable Long id) {
        return new ResponseEntity<>(IntermediaireService.deleteIntermediaire(id), HttpStatus.OK);
    }

    @Operation(summary = "Exporter la liste des Intermédiaires au format PDF (avec dates séparées)")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<Intermediaire> list = IntermediaireService.getAllIntermediaire();

        // 2) Générer le PDF en mémoire, A4 paysage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // 2.a) Logo
        String logoPath = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\src\\assets\\uploads-images\\aaaa.jpeg";
        Image logo = Image.getInstance(logoPath);
        logo.scaleAbsolute(50, 50);
        float pageHeight = document.getPageSize().getHeight();
        logo.setAbsolutePosition(36, pageHeight - 70);
        document.add(logo);

        // 2.b) Date alignée à droite
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
        Paragraph title = new Paragraph("Liste des Intermédiaires",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // 2.d) Tableau : 9 colonnes, mêmes largeurs
        int columnCount = 9;
        float[] widths = new float[columnCount];
        Arrays.fill(widths, 1f);
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);

        // en-têtes
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = {
                "Code",
                "Libellé FR",
                "Symbole FR",
                "Adresse",
                "Téléphone",
                "Compte bancaire",
                "Banque",
                "Date Début",
                "Date Fin"
        };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41, 128, 185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        // cellules
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Intermediaire i : list) {
            table.addCell(new PdfPCell(new Phrase(i.getCode(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(i.getLibellefr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(i.getSymbolefrancais(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(i.getAdresse(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(i.getTelephone(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(i.getComptebanquaire(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(i.getBanque(), cellFont)));

            // Date Début
            String debut = i.getDateDebut() != null
                    ? dtf.format(i.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    : "-";
            table.addCell(new PdfPCell(new Phrase(debut, cellFont)));

            // Date Fin
            String fin = i.getDateFin() != null
                    ? dtf.format(i.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    : "-";
            table.addCell(new PdfPCell(new Phrase(fin, cellFont)));
        }

        document.add(table);
        document.close();

        // 3) Nom de fichier
        String safeTs = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "intermediaires-" + safeTs + ".pdf";

        // 4) Sauvegarde locale (optionnelle)
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\Intermediaire";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        // 5) Envoi au client
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }


}
