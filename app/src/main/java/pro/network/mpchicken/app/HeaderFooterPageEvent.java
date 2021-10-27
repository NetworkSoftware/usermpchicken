package pro.network.mpchicken.app;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.ArrayList;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
    private static BaseFont urName;
    private static BaseColor greenBase = new BaseColor(14, 157, 31);
    private static final Font subFont = new Font(urName, 8, Font.NORMAL);
    private static final Font poweredFont = new Font(urName, 6, Font.ITALIC, new BaseColor(10, 242, 255));
    private static final Font PagenoFont = new Font(urName, 8, Font.NORMAL);
    private static Font PagenoFont1 = new Font(urName, 8, Font.BOLD, greenBase);
    private static final Font webPrint = new Font(urName, 8, Font.BOLD, new BaseColor(66, 133, 244));


    {
        try {
            urName = BaseFont.createFont("font/sans.TTF", "UTF-8", BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HeaderFooterPageEvent() {
        PagenoFont1 = new Font(urName, 8, Font.BOLD, greenBase);
    }

    public void onStartPage(PdfWriter writer, Document document) {
    }

    public void onEndPage(PdfWriter writer, Document document) {

        Phrase phrase0 = new Phrase();
        phrase0.add("");
        Chunk chunk0 = new Chunk("W: wa.me/918124881156", webPrint);
        chunk0.setAnchor("https://wa.me/918883438888");
        phrase0.add(chunk0);

        Phrase phrase1 = new Phrase();
        phrase1.add("");
        Chunk chunk1 = new Chunk("mpchicken8888@gmail.com", webPrint);
        chunk1.setAnchor("https://mail.google.com/mail/u/0/?tab\\u003drm\\u0026ogbl#inbox?compose\\u003dnew");
        phrase1.add(chunk1);

      /*  Phrase phrase2 = new Phrase();
        phrase2.add("");
        Chunk chunk2 = new Chunk("www.mpchicken.com", webPrint);
        chunk2.setAnchor("https://www.mpchicken.com/#");
        phrase2.add(chunk2);
*/
        ArrayList<Phrase> phrases = new ArrayList<>();
        phrases.add(phrase0);
        phrases.add(phrase1);
        //phrases.add(phrase2);
        for (int i = 0; i < phrases.size(); i++) {
            int y = 20 + (i * 15);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, phrases.get(i), 30, y, 0);
        }

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("THANK YOU FOR YOUR BUSINESS!", subFont),
                PageSize.A4.getWidth() / 2, 20, 0);
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("powered by", poweredFont),
                PageSize.A4.getWidth() / 2 + 10, 10, 0);

        /*ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("For " + "MP Chicken" + " ", PagenoFont1),
                PageSize.A4.getWidth() - 30, 60, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("Authorized Signatory ", PagenoFont),
                PageSize.A4.getWidth() - 30, 20, 0);

*/
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(document.getPageSize().getWidth() - 30,
                document.getPageSize().getHeight() - 40);
        rect.setBorder(Rectangle.BOX); // left, right, top, bottom border
        rect.setBorderWidth(1); // a width of 5 user units
        rect.setBorderColor(BaseColor.BLACK); // a red border
        rect.setBottom(122);
        rect.setLeft(30);
        rect.setUseVariableBorders(false); // the full width will be visible
        canvas.rectangle(rect);
    }

}