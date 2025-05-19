package com.projet.administration.Controller.Secteur;
import com.projet.administration.Entity.Secteur.SecteurInternational;
import com.projet.administration.Service.Secteur.SecteurInternationalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import com.lowagie.text.Document;
import java.awt.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("secteurinternational")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class SecteurInternationalController {



    private final SecteurInternationalService SecteurInternationalService;

    @Operation(summary = "Get all SecteurInternationals", description = "Returns a list of all SecteurInternational")
    @GetMapping("/all")
    public ResponseEntity<List<SecteurInternational>> getAllSecteurInternational() {
        return ResponseEntity.ok(SecteurInternationalService.getAllSecteurInternational());
    }

    @Operation(summary = "add SecteurInternationals")
    @PostMapping("/add")
    public ResponseEntity<SecteurInternational> addSecteurInternational(@RequestBody SecteurInternational SecteurInternational) {
        SecteurInternational created = SecteurInternationalService.addSecteurInternational(SecteurInternational);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @Operation(summary = "update SecteurInternationals")
    @PutMapping("/update/{id}")
    public ResponseEntity<SecteurInternational> updateSecteurInternational(@PathVariable Long id, @RequestBody SecteurInternational SecteurInternational) {
        SecteurInternational updatedSecteurInternational = SecteurInternationalService.updateSecteurInternationalWithId(id, SecteurInternational);
        return updatedSecteurInternational != null ? ResponseEntity.ok(updatedSecteurInternational) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete SecteurInternational")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSecteurInternational(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(SecteurInternationalService.deleteSecteurInternational(id), HttpStatus.OK);

    }

    // —————————————————————————————————————————————————————————
    @Operation(summary = "Exporter la liste des Secteurs Internationaux au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<SecteurInternational> list = SecteurInternationalService.getAllSecteurInternational();

        // 2) Générer le PDF en mémoire
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
        Paragraph title = new Paragraph("Liste des Secteurs Internationaux",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // 2.d) Tableau
        PdfPTable table = new PdfPTable(new float[]{3,3,5,4,4,4,5});
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = {
                "Code ICB", "Code BVMT", "Codesupersecteur",
                "Libellé FR", "Libellé AR", "Libellé EN", "Description"
        };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41,128,185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (SecteurInternational si : list) {
            table.addCell(new PdfPCell(new Phrase(si.getCodeICB(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(si.getCodeBVMT(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(si.getCodesupersecteur(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(si.getLibellefr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(si.getLibellear(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(si.getLibelleen(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(
                    si.getDescription() != null ? si.getDescription() : "-", cellFont)));
        }
        document.add(table);
        document.close();

        // 3) Nom de fichier sécurisé
        String safeTs = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "secteurinternational-" + safeTs + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\SecteurInternational";
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
