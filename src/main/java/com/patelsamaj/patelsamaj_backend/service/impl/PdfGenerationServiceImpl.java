package com.patelsamaj.patelsamaj_backend.service.impl;

import com.lowagie.text.pdf.BaseFont;
import com.patelsamaj.patelsamaj_backend.dto.EventDTO;
import com.patelsamaj.patelsamaj_backend.dto.ItemSelectionDTO;
import com.patelsamaj.patelsamaj_backend.service.PdfGenerationService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PdfGenerationServiceImpl implements PdfGenerationService {

    private final TemplateEngine templateEngine;

    private static final String[] WORDS = {
            "શૂન્ય",
            "એક", "બે", "ત્રણ", "ચાર", "પાંચ", "છ", "સાત", "આઠ", "નવ",
            "દસ", "અગિયાર", "બાર", "તેર", "ચૌદ", "પંદર", "સોળ", "સત્તર", "અઢાર", "ઓગણીસ",
            "વીસ", "એકવીસ", "બાવીસ", "તેવીસ", "ચોવીસ", "પચ્ચીસ", "છવ્વીસ", "સત્તાવીસ", "અઠ્ઠાવીસ", "ઓગણત્રીસ",
            "ત્રીસ", "એકત્રીસ", "બત્રીસ", "તેત્રીસ", "ચોત્રીસ", "પાંત્રીસ", "છત્રીસ", "સડત્રીસ", "આડત્રીસ", "ઓગણચાલીસ",
            "ચાલીસ", "એકતાલીસ", "બેતાલીસ", "ત્રેતાલીસ", "ચુમ્માલીસ", "પિસ્તાલીસ", "છેતાલીસ", "સુડતાલીસ", "અડતાલીસ", "ઓગણપચાસ",
            "પચાસ", "એકાવન", "બાવન", "ત્રેપન", "ચોપન", "પંચાવન", "છપ્પન", "સત્તાવન", "અઠ્ઠાવન", "ઓગણસાઠ",
            "સાઠ", "એકસઠ", "બાસઠ", "ત્રેસઠ", "ચોસઠ", "પાંસઠ", "છાસઠ", "સડસઠ", "અડસઠ", "ઓગણસિત્તેર",
            "સિત્તેર", "એકોતેર", "બોતેર", "તેરોતેર", "ચુમોતેર", "પંચોતેર", "છોતેર", "સિત્યોતેર", "અઠ્યોતેર", "ઓગણએંસી",
            "એંસી", "એક્યાસી", "બ્યાસી", "ત્ર્યાસી", "ચોર્યાસી", "પંચ્યાસી", "છ્યાસી", "સિત્યાસી", "અઠ્યાસી", "નેવ્યાસી",
            "નેવું", "એકાણું", "બાણું", "ત્રાણું", "ચોરાણું", "પંચાણું", "છન્નું", "સત્તાણું", "અઠ્ઠાણું", "નવ્વાણું"
    };

    private static final String[] HUNDREDS = {"", "એકસો", "બસો", "ત્રણસો", "ચારસો", "પાંચસો", "છસો", "સાતસો", "આઠસો", "નવસો" };

    private static final long[] VALUES = {
            1000000000L, // અરબ
            10000000L,   // કરોડ
            100000L,     // લાખ
            1000L         // હજાર
    };

    private static final String[] NAMES = { "અરબ", "કરોડ", "લાખ", "હજાર" };

    private static final char[] GUJARATI_DIGITS = {
            '૦', '૧', '૨', '૩', '૪', '૫', '૬', '૭', '૮', '૯'
    };

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BillRow {
        private String srNoLeft;
        private String nameLeft;
        private String rateLeft;
        private String qtyLeft;
        private String rupeesLeft;
        private String paisaLeft;

        private String srNoRight;
        private String nameRight;
        private String rateRight;
        private String qtyRight;
        private String rupeesRight;
        private String paisaRight;
    }

    private String toGujaratiDigits(Object input) {
        if (input == null) {
            return "";
        }
        String str = String.valueOf(input);
        StringBuilder result = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                result.append(GUJARATI_DIGITS[ch - '0']);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private String formatDateToGujarati(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return toGujaratiDigits(date.format(formatter));
    }

    private String toGujaratiWords(Long number) {
        if (number == null || number == 0) {
            return WORDS[0] + " પૂરા";
        }
        return convert(number).trim() + " પૂરા";
    }

    private static String convert(long number) {
        if (number < 100) {
            return WORDS[(int) number];
        }

        if (number < 1000) {
            int hundreds = (int) (number / 100);
            int remainder = (int) (number % 100);

            if (remainder == 0)
                return HUNDREDS[hundreds];

            return HUNDREDS[hundreds] + " " + convert(remainder);
        }

        for (int i = 0; i < VALUES.length; i++) {
            if (number >= VALUES[i]) {
                long quotient = number / VALUES[i];
                long remainder = number % VALUES[i];

                String result = convert(quotient) + " " + NAMES[i];

                if (remainder != 0)
                    result += " " + convert(remainder);

                return result;
            }
        }

        return "";
    }

    @Override
    public byte[] generateBillPdf(EventDTO event) {
        // 1. Prepare Thymeleaf processing context variables
        Context context = new Context();
        context.setVariable("event", event);

        // 2. Prepare dynamic symmetric rows (Left: 0..totalRows-1, Right: totalRows..2*totalRows-1)
        List<ItemSelectionDTO> items = event.getItems() != null ? event.getItems() : new ArrayList<>();
        int totalRows = Math.max(32, (int) Math.ceil((double) items.size() / 2.0));
        List<BillRow> rows = new ArrayList<>(totalRows);

        for (int r = 0; r < totalRows; r++) {
            BillRow.BillRowBuilder rowBuilder = BillRow.builder();

            // Left side
            int leftIndex = r;
            rowBuilder.srNoLeft(toGujaratiDigits(leftIndex + 1));
            if (leftIndex < items.size()) {
                ItemSelectionDTO leftItem = items.get(leftIndex);
                rowBuilder.nameLeft(leftItem.getItemName() != null ? leftItem.getItemName() : "");
                if (leftItem.getQuantity() != null && leftItem.getQuantity() > 0) {
                    float price = leftItem.getPriceAtBooking();
                    BigDecimal priceBd = BigDecimal.valueOf(price);
                    BigDecimal lineTotal = priceBd.multiply(BigDecimal.valueOf(leftItem.getQuantity())).setScale(2, RoundingMode.HALF_UP);
                    long rupees = lineTotal.longValue();
                    int paise = lineTotal.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).abs().intValue();

                    rowBuilder.rateLeft(toGujaratiDigits(String.format(Locale.US, "%.2f", price)));
                    rowBuilder.qtyLeft(toGujaratiDigits(leftItem.getQuantity()));
                    rowBuilder.rupeesLeft(toGujaratiDigits(rupees));
                    rowBuilder.paisaLeft(toGujaratiDigits(String.format(Locale.US, "%02d", paise)));
                } else {
                    rowBuilder.rateLeft("");
                    rowBuilder.qtyLeft("");
                    rowBuilder.rupeesLeft("");
                    rowBuilder.paisaLeft("");
                }
            } else {
                rowBuilder.nameLeft("");
                rowBuilder.rateLeft("");
                rowBuilder.qtyLeft("");
                rowBuilder.rupeesLeft("");
                rowBuilder.paisaLeft("");
            }

            // Right side
            int rightIndex = totalRows + r;
            rowBuilder.srNoRight(toGujaratiDigits(rightIndex + 1));
            if (rightIndex < items.size()) {
                ItemSelectionDTO rightItem = items.get(rightIndex);
                rowBuilder.nameRight(rightItem.getItemName() != null ? rightItem.getItemName() : "");
                if (rightItem.getQuantity() != null && rightItem.getQuantity() > 0) {
                    float price = rightItem.getPriceAtBooking();
                    BigDecimal priceBd = BigDecimal.valueOf(price);
                    BigDecimal lineTotal = priceBd.multiply(BigDecimal.valueOf(rightItem.getQuantity())).setScale(2, RoundingMode.HALF_UP);
                    long rupees = lineTotal.longValue();
                    int paise = lineTotal.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).abs().intValue();

                    rowBuilder.rateRight(toGujaratiDigits(String.format(Locale.US, "%.2f", price)));
                    rowBuilder.qtyRight(toGujaratiDigits(rightItem.getQuantity()));
                    rowBuilder.rupeesRight(toGujaratiDigits(rupees));
                    rowBuilder.paisaRight(toGujaratiDigits(String.format(Locale.US, "%02d", paise)));
                } else {
                    rowBuilder.rateRight("");
                    rowBuilder.qtyRight("");
                    rowBuilder.rupeesRight("");
                    rowBuilder.paisaRight("");
                }
            } else {
                rowBuilder.nameRight("");
                rowBuilder.rateRight("");
                rowBuilder.qtyRight("");
                rowBuilder.rupeesRight("");
                rowBuilder.paisaRight("");
            }

            rows.add(rowBuilder.build());
        }

        context.setVariable("rows", rows);
        context.setVariable("eventDateGujarati", formatDateToGujarati(event.getStartDate()));
        context.setVariable("contactNumberGujarati", toGujaratiDigits(event.getContactNumber()));
        context.setVariable("billNumberGujarati", toGujaratiDigits(event.getEventId()));

        long roundedTotal = event.getTotalAmount() != null ? Math.round(event.getTotalAmount().doubleValue()) : 0L;
        context.setVariable("totalAmountInWords", toGujaratiWords(roundedTotal));
        context.setVariable("totalAmountGujaratiDigits", toGujaratiDigits(roundedTotal));

        // 3. Process data values through the Thymeleaf template file
        String processedHtml = templateEngine.process("bill-template", context);

        // 4. Render processed XHTML string context into a raw PDF binary byte stream
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            registerFonts(renderer);
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate bill print document binary structure", e);
        }
    }

    private void registerFonts(ITextRenderer renderer) {
        registerClasspathFont(renderer, "fonts/Nirmala.ttf");
        registerClasspathFont(renderer, "fonts/NirmalaB.ttf");
        registerClasspathFont(renderer, "fonts/MuktaVaani-Regular.ttf");
        registerClasspathFont(renderer, "fonts/MuktaVaani-Bold.ttf");
        registerClasspathFont(renderer, "fonts/NotoSansGujarati-Regular.ttf");
        registerClasspathFont(renderer, "fonts/NotoSansGujarati-Bold.ttf");
    }

    private void registerClasspathFont(ITextRenderer renderer, String resourcePath) {
        try {
            Resource resource = new ClassPathResource(resourcePath);
            if (resource.exists()) {
                try {
                    File file = resource.getFile();
                    renderer.getFontResolver().addFont(file.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    return;
                } catch (Exception ignored) {
                    // When running from inside a packaged JAR file
                }

                File tempFontFile = File.createTempFile("font-", ".ttf");
                tempFontFile.deleteOnExit();
                try (java.io.InputStream is = resource.getInputStream();
                     java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFontFile)) {
                    is.transferTo(fos);
                }
                renderer.getFontResolver().addFont(tempFontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not register font from " + resourcePath + ": " + e.getMessage(), e);
        }
    }
}
