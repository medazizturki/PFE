package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.Groupes;
import com.projet.administration.Service.GestionReferentiels.GroupesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/groupes")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class GroupesController {

    private final GroupesService groupesService;

    @Operation(summary = "Get all Groupes")
    @GetMapping("/all")
    public ResponseEntity<List<Groupes>> getAllGroupes() {
        return ResponseEntity.ok(groupesService.getAllGroupes());
    }

    @Operation(summary = "Add a Groupe")
    @PostMapping("/add")    // ← no consumes/produces here
    public ResponseEntity<Groupes> addGroupes(@RequestBody Groupes groupes) {
        Groupes created = groupesService.addGroupes(groupes);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a Groupe")
    @PutMapping("/update/{id}")  // ← no consumes/produces here
    public ResponseEntity<Groupes> updateGroupes(
            @PathVariable Long id,
            @RequestBody Groupes groupes
    ) {
        Groupes updated = groupesService.updateGroupesWithId(id, groupes);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a Groupe")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteGroupes(@PathVariable Long id) {
        return new ResponseEntity<>(groupesService.deleteGroupes(id), HttpStatus.OK);
    }

    @Operation(summary = "Exporter la liste des Groupes au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<Groupes> list = groupesService.getAllGroupes();

        // 2) Générer le PDF en mémoire, A4 paysage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // 2.a) Logo (ajustez le chemin à votre projet)
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
        Paragraph titlePara = new Paragraph("Liste des Groupes",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau : Code | Marché | Catégorie | Unité Cotation | Mode Cotation | Devise
        PdfPTable table = new PdfPTable(new float[]{3, 4, 4, 3, 3, 3});
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = {
                "Code",
                "Marché",
                "Catégorie",
                "Unité Cotation",
                "Mode Cotation",
                "Devise"
        };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41, 128, 185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (Groupes g : list) {
            table.addCell(new PdfPCell(new Phrase(g.getCode(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(g.getMarche().name(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(g.getCategorie().name(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(g.getUnitecotation(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(g.getModeCotation().name(), cellFont)));
            // Si vous avez un champ "code" ou "symbole" sur Devises :
            table.addCell(new PdfPCell(new Phrase(
                    g.getDevises() != null ? g.getDevises().getCode() : "-", cellFont)));
        }

        document.add(table);
        document.close();

        // 3) Nom de fichier sécurisé
        String safeTs = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "groupes-" + safeTs + ".pdf";

        // 4) Sauvegarde sur disque (optionnel)
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\Groupes";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        // 5) Envoyer au client
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }

}
