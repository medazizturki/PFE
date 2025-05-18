package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.NatureReferentiels;
import com.projet.administration.Service.GestionReferentiels.NatureReferentielsService;
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
@RestController
@RequestMapping("/naturereferentiels")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class NatureReferentielsController {

    private final NatureReferentielsService natureReferentielsService;

    @Operation(summary = "Get all NatureReferentiels", description = "Returns a list of all NatureReferentiels entities")
    @GetMapping("/all")
    public ResponseEntity<List<NatureReferentiels>> getAllNatureReferentiels() {
        return ResponseEntity.ok(natureReferentielsService.getAllNatureReferentiels());
    }

    @Operation(summary = "add NatureReferentiels")
    @PostMapping("/add")
    public ResponseEntity<NatureReferentiels> addNatureReferentiels(@RequestBody NatureReferentiels naturereferentiels) {
        NatureReferentiels createdNatureReferentiels = natureReferentielsService.addNatureReferentiels(naturereferentiels);
        return new ResponseEntity<>(createdNatureReferentiels, HttpStatus.CREATED);
    }


    @Operation(summary = "update NatureReferentiels")
    @PutMapping("/update/{id}")
    public ResponseEntity<NatureReferentiels> updateNatureReferentiels(@PathVariable Long id, @RequestBody NatureReferentiels naturereferentiels) {
        NatureReferentiels updatedNatureReferentiels = natureReferentielsService.updateNatureReferentielsWithId(id, naturereferentiels);
        return updatedNatureReferentiels != null ? ResponseEntity.ok(updatedNatureReferentiels) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete NatureReferentiels")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteNatureReferentiels(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(natureReferentielsService.deleteNatureReferentiels(id), HttpStatus.OK);

    }

    @Operation(summary = "Exporter la liste des NatureReferentiels au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<NatureReferentiels> list = natureReferentielsService.getAllNatureReferentiels();

        // 2) Générer le PDF en mémoire, A4 paysage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // 2.a) Logo (ajustez le chemin si besoin)
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
        Paragraph title = new Paragraph("Liste des Nature Référentiels",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // 2.d) Tableau : Code BVMT | Libellé | Groupe | Libellé FR | AR | EN | Description
        PdfPTable table = new PdfPTable(new float[]{2, 4, 3, 3, 3, 3, 5});
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = {
                "Code BVMT",
                "Libellé",
                "Groupe",
                "Libellé FR",
                "Libellé AR",
                "Libellé EN",
                "Description"
        };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41, 128, 185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (NatureReferentiels n : list) {
            table.addCell(new PdfPCell(new Phrase(n.getCodeBvmt(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(n.getLibelle(), cellFont)));
            // Affichez ici ce que vous souhaitez du 'groupes' (par ex. son code)
            table.addCell(new PdfPCell(new Phrase(
                    n.getGroupes() != null ? n.getGroupes().getCode() : "-", cellFont)));
            table.addCell(new PdfPCell(new Phrase(n.getLibellefr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(n.getLibellear(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(n.getLibelleen(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(
                    n.getDescription() != null ? n.getDescription() : "-", cellFont)));
        }

        document.add(table);
        document.close();

        // 3) Nom de fichier
        String safeTs = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "naturereferentiels-" + safeTs + ".pdf";

        // 4) Sauvegarde locale (si souhaité)
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\NatureReferentiels";
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
