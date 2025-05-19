package com.projet.administration.Controller.GestionReferentiels;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.ColumnText;
import com.projet.administration.Entity.GestionReferentiels.CategoriesDavoir;
import com.projet.administration.Service.GestionReferentiels.CategoriesDavoirService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.awt.*;

import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("categoriedavoir")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class CategoriesDavoirController {

    private final CategoriesDavoirService categoriesDavoirService;


    @Operation(summary = "Get all CategoriesDavoir", description = "Returns a list of all CategoriesDavoir entities")
    @GetMapping("/all")
    public ResponseEntity<List<CategoriesDavoir>> getAllCategoriesDavoir() {
        return ResponseEntity.ok(categoriesDavoirService.getAllCategoriesDavoir());
    }

    @Operation(summary = "add CategoriesDavoir")
    @PostMapping("/add")
    public ResponseEntity<CategoriesDavoir> addCategoriesDavoir(@RequestBody CategoriesDavoir CategoriesDavoir) {
        CategoriesDavoir createdCategoriesDavoir = categoriesDavoirService.addCategoriesDavoir(CategoriesDavoir);
        return new ResponseEntity<>(createdCategoriesDavoir, HttpStatus.CREATED);
    }


    @Operation(summary = "update CategoriesDavoir")
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoriesDavoir> updateCategoriesDavoir(@PathVariable Long id, @RequestBody CategoriesDavoir CategoriesDavoir) {
        CategoriesDavoir updatedCategoriesDavoir = categoriesDavoirService.updateCategoriesDavoirWithId(id, CategoriesDavoir);
        return updatedCategoriesDavoir != null ? ResponseEntity.ok(updatedCategoriesDavoir) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete CategoriesDavoir")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCategoriesDavoir(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(categoriesDavoirService.deleteCategoriesDavoir(id), HttpStatus.OK);

    }




    @Operation(summary = "Exporter la liste des CategoriesDavoir au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<CategoriesDavoir> list = categoriesDavoirService.getAllCategoriesDavoir();

        // 2) Générer le PDF en mémoire sur un A4 paysage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // 2.a) Logo (plus haut)
        String logoPath = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\src\\assets\\uploads-images\\aaaa.jpeg";
        Image logo = Image.getInstance(logoPath);
        logo.scaleAbsolute(50, 50);
        float pageHeight = document.getPageSize().getHeight();
        logo.setAbsolutePosition(15, pageHeight - 70);
        document.add(logo);

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

        Paragraph titlePara = new Paragraph("Liste des Catégories d'Avoir",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        int columnCount = 10;
        float[] widths = new float[columnCount];
        Arrays.fill(widths, 1f);
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = {
                "Code BVMT", "Code NSC", "Code Optique", "Code Tc",
                "Libellé FR","Libellé Courte FR",
                "Taux CTB","Taux RUS","Taux CEB_ENR","Taux RUS_ENR"
        };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41,128,185));
            cell.setPadding(4);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        for (CategoriesDavoir c : list) {
            table.addCell(new PdfPCell(new Phrase(c.getCodeBVMT(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(c.getCodeNSC(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(c.getCodeOptique(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(c.getCodeTc(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(c.getLibellefr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(c.getLibellecourtefr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getTauxreductionCTB()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getTauxreductionRUS()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getTauxreductionCEB_ENR()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getTauxreductionRUS_ENR()), cellFont)));
        }
        document.add(table);
        document.close();

        String ts = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "categories-davoir-" + ts + ".pdf";

        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\Categorie d'Avoir";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }





}
