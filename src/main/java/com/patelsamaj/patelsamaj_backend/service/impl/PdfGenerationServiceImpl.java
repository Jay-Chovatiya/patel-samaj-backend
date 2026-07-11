package com.patelsamaj.patelsamaj_backend.service.impl;

import com.patelsamaj.patelsamaj_backend.dto.EventDTO;
import com.patelsamaj.patelsamaj_backend.dto.ItemSelectionDTO;
import com.patelsamaj.patelsamaj_backend.service.PdfGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfGenerationServiceImpl implements PdfGenerationService  {

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

    private String toGujaratiWords(Long number) {

        if (number == 0)
            return WORDS[0];

        return convert(number).trim() + "પુરા";
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

    public byte[] generateBillPdf(EventDTO event) {
        // 1. Prepare Thymeleaf processing context variables
        Context context = new Context();
        context.setVariable("event", event);
        boolean flag = true;

        // 2. Split the list of items into left (1-32) and right (33-64) matrices
        List<ItemSelectionDTO> leftItems = new ArrayList<>();
        List<ItemSelectionDTO> rightItems = new ArrayList<>();
        List<ItemSelectionDTO> activeItems = event.getItems();

        for (ItemSelectionDTO item : activeItems) {
            if(flag)
                leftItems.add(item);
            else
                rightItems.add(item);
            flag = !flag;
        }
        context.setVariable("leftItems", leftItems);
        context.setVariable("rightItems", rightItems);
        context.setVariable("totalBillInGujarati", toGujaratiWords(event.getTotalAmount()));

        // 3. Process data values through the Thymeleaf template file
        String processedHtml = templateEngine.process("bill-template", context);

        // 4. Render processed XHTML string context into a raw PDF binary byte stream
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate bill print document binary structure", e);
        }
    }


}
