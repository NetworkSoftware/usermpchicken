package pro.network.freshcatch.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import pro.network.freshcatch.R;
import pro.network.freshcatch.orders.MyorderBean;
import pro.network.freshcatch.product.ProductListBean;


public class PdfConfig {

    private static final BaseColor whiteBase = new BaseColor(255, 255, 255);
    private static final BaseColor gray = new BaseColor(237, 237, 237);
    private static BaseFont urName;
    private static final Font titleFont = new Font(urName, 11, Font.UNDERLINE | Font.BOLD);
    private static final Font nameFont = new Font(urName, 11, Font.BOLD);
    private static final Font catFont = new Font(urName, 9, Font.BOLD);
    private static final Font catFontWhite = new Font(urName, 9, Font.BOLD);
    private static final Font catNormalFont = new Font(urName, 9, Font.NORMAL);
    private static final Font subFont = new Font(urName, 7, Font.NORMAL);
    private static BaseColor greenLightBase;
    private static BaseColor greenBase = new BaseColor(14, 157, 31);
    private static Font invoiceFont = new Font(urName, 20, Font.BOLD, greenBase);

    {
        try {
            urName = BaseFont.createFont("font/sans.TTF", "UTF-8", BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public static void addContent(Document document, MyorderBean myorderBean, Context context) throws Exception {

        greenBase = new BaseColor(context.getResources().getColor(R.color.colorPrimaryDark));
        greenLightBase = new BaseColor(context.getResources().getColor(R.color.colorpdf));
        invoiceFont = new Font(urName, 20, Font.BOLD, greenBase);

        PdfPTable tableAll = new PdfPTable(1);
        tableAll.setWidthPercentage(100);
        tableAll.setWidths(new int[]{1});

        PdfPTable table01 = new PdfPTable(3);
        table01.setWidthPercentage(100);
        table01.setWidths(new int[]{1, 1, 1});
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.fresh_icon);
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Image img = Image.getInstance(byteArray);
        img.scaleAbsolute(50, 50);
        table01.addCell(createTextImage(img));

        PdfPTable table000 = new PdfPTable(1);
        table000.setWidthPercentage(100);
        table000.addCell(createTextRight("Invoice", invoiceFont));
        table000.addCell(createTextRight(myorderBean.getCreatedOn(), catNormalFont));

        table01.addCell(createTextRight("", catNormalFont));
        table01.addCell(createTable(table000, 0, whiteBase, false));

        table01.setKeepTogether(true);
        tableAll.addCell(createTable(table01, 0, whiteBase, false));


        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(100);
        table2.setWidths(new int[]{1, 1});

        PdfPTable table20 = new PdfPTable(1);
        table20.setWidthPercentage(100);
        table20.setWidths(new int[]{1});
        table20.addCell(createTextCell("Customers Details", nameFont));
        table20.addCell(createTextCell("Name :" + myorderBean.getName(), catNormalFont));
        table20.addCell(createTextCell("Phone :" + myorderBean.getPhone(), catNormalFont));
        table20.addCell(createTextCell("Address :" + myorderBean.getAddress(), catNormalFont));
        table20.addCell(createTextCell("Comments :" + myorderBean.getComments(), catNormalFont));
        table20.addCell(createTextCell("\n", nameFont));
        table20.addCell(createTextCell("Payment Information", nameFont));
        table20.addCell(createTextCell("Mode of Payment :" + myorderBean.getPayment(), catNormalFont));
        table20.addCell(createTextCell("Payment ID :" + (myorderBean.getPaymentId() == null ? "COD" : myorderBean.getPaymentId()), catNormalFont));
        table20.setKeepTogether(true);

        PdfPTable table21 = new PdfPTable(1);
        table21.setWidthPercentage(100);
        table21.setWidths(new int[]{1});
        table21.addCell(createTextCell("Order Details", nameFont));
        table21.addCell(createTextCell("Order Id :" + (myorderBean.getDelivery()
                .equalsIgnoreCase("express") ? "ECF" : "SCF") + myorderBean.getId(), catNormalFont));
        //table21.addCell(createTextCell("Coupon Details :" + myorderBean.getCouponCost(), catNormalFont));
        table21.addCell(createTextCell("\n", nameFont));
        table21.addCell(createTextCell("Delivery Details", nameFont));
        table21.addCell(createTextCell("Mode :" + myorderBean.getDelivery(), catNormalFont));
        if (myorderBean.getCreatedOn() != null && myorderBean.getCreatedOn().length() > 0) {
            table21.addCell(createTextCell("Date:" + myorderBean.getCreatedOn().split(" ")[0], catNormalFont));
        } else {
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            table21.addCell(createTextCell("Date:" + s.toString(), catNormalFont));
        }

        table21.setKeepTogether(true);


        table2.addCell(createTable(table20, 0, whiteBase, false));
        table2.addCell(createTable(table21, 0, whiteBase, false));

        table2.addCell(createTextCell("\n", catNormalFont));
        table2.addCell(createTextCell("\n", catNormalFont));

        tableAll.addCell(createTable(table2, 0, whiteBase, false));


        PdfPTable table3 = new PdfPTable(5);
        table3.setWidthPercentage(100);
        table3.setWidths(new float[]{0.5f, 2f, 0.6f, 0.8f, 1.1f});
        table3.addCell(createTextCellBorder("S.NO", catFont));
        table3.addCell(createTextCellBorder("Product", catFont));
        table3.addCell(createTextCellBorder("Qty", catFont));
        table3.addCell(createTextCellBorder("Rate", catFont));
        table3.addCell(createTextCellBorder("Total", catFont));

        for (int i = 0; i < myorderBean.getProductBeans().size(); i++) {
            ProductListBean productListBean = myorderBean.getProductBeans().get(i);
            int quan = Integer.parseInt(productListBean.getQty());
            int perQuanPri = Integer.parseInt(productListBean.getPrice());
            int total = quan * perQuanPri;
            if(i==myorderBean.getProductBeans().size()-1){
                table3.addCell(createTextCellBottomWithRight(String.valueOf(i + 1), catNormalFont));
                table3.addCell(createTextCellBottomWithRight(productListBean.getBrand() + " " + productListBean.getModel(), catFont));
                table3.addCell(createTextCellqutWithBottom(productListBean.getQty(), catNormalFont));
                table3.addCell(createTextCelltotalWithBottom(productListBean.getPrice(), catNormalFont));
                table3.addCell(createTextCelltotalWithBottom(round(total, 2), catNormalFont));
            }else {
                table3.addCell(createTextCellBottom(String.valueOf(i + 1), catNormalFont));
                table3.addCell(createTextCellBottom(productListBean.getBrand() + " " + productListBean.getModel(), catFont));
                table3.addCell(createTextCellqut(productListBean.getQty(), catNormalFont));
                table3.addCell(createTextCelltotal(productListBean.getPrice(), catNormalFont));
                table3.addCell(createTextCelltotal(round(total, 2), catNormalFont));
            }
        }

        tableAll.addCell(createTable(table3, 0, whiteBase, false));


        PdfPTable table4 = new PdfPTable(2);
        table4.setWidthPercentage(100);
        table4.setWidths(new float[]{1.6f, 0.4f});

        table4.addCell(TableTextRight("Grand Total :", catNormalFont));
        table4.addCell(TableTextRightpaddind(myorderBean.getGrandCost(), catNormalFont));

        table4.addCell(TableTextRight("Shipping Charge :", catNormalFont));
        table4.addCell(TableTextRightpaddind(myorderBean.getShipCost(), catNormalFont));

       // table4.addCell(TableTextRight("Coupon :", catNormalFont));
        //table4.addCell(TableTextRightpaddind(myorderBean.getCouponCost(), catNormalFont));

        table4.addCell(TableTextRight("Sub Total :", nameFont));
        table4.addCell(TableTextRightpaddind(myorderBean.getAmount(), nameFont));

        table4.setKeepTogether(true);
        //document.add(table03);
        tableAll.addCell(createTable(table4, 0, whiteBase, false));
        tableAll.setSplitLate(false);
        document.add(tableAll);

    }

    public static PdfPCell createTextCell(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(15);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell createTextCelltotal(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.RIGHT);
        return cell;
    }
    private static PdfPCell createTextCelltotalWithBottom(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);

        return cell;
    }
    private static PdfPCell createTextCellqut(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.RIGHT);
        return cell;
    }
    private static PdfPCell createTextCellqutWithBottom(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);

        return cell;
    }
    public static PdfPCell TableTextRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingLeft(15);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
    public static PdfPCell TableTextRightpaddind(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingLeft(15);
        cell.setPaddingRight(15);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
    private static PdfPCell createTextCellBottom(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.RIGHT);
        return cell;
    }
    private static PdfPCell createTextCellBottomWithRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
        return cell;
    }
    public static PdfPCell createTextCellBorder(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setBackgroundColor(greenLightBase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(10);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }
    public static PdfPCell createTable(PdfPTable pTable, int padding, BaseColor baseColor, boolean isBorder) throws DocumentException, IOException {

        PdfPCell cell = new PdfPCell();
        cell.addElement(pTable);
        cell.setPaddingTop(0);
        cell.setPaddingBottom(0);
        cell.setPaddingLeft(padding);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        if (isBorder) {
            cell.setCellEvent(new DottedCell(PdfPCell.BOX));
        }
        cell.setBackgroundColor(baseColor);
        return cell;
    }
    public static PdfPCell createTextRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setPaddingLeft(5);
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
    public static PdfPCell createTextImage(Image image) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setUseAscender(true);
        cell.setPaddingTop(10);
        cell.setVerticalAlignment(Element.ALIGN_RIGHT);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
    public static String round(double d, int decimalPlace) {
        return String.format("%.2f", d);
    }
    static class DottedCell implements PdfPCellEvent {
        private int border = 0;

        public DottedCell(int border) {
            this.border = border;
        }

        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.saveState();
            canvas.setColorStroke(greenBase);
            canvas.setLineDash(2, 6, 2);
            if ((border & PdfPCell.TOP) == PdfPCell.TOP) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
            }
            if ((border & PdfPCell.BOTTOM) == PdfPCell.BOTTOM) {
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            if ((border & PdfPCell.RIGHT) == PdfPCell.RIGHT) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
            }
            if ((border & PdfPCell.LEFT) == PdfPCell.LEFT) {
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            canvas.stroke();
            canvas.restoreState();
        }
    }

}
