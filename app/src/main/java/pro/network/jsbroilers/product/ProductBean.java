package pro.network.jsbroilers.product;

import java.io.Serializable;

public class ProductBean implements Serializable {
    public static final String TABLE_NAME = "yalu_mobile_table";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRO_ID = "proid";
    public static final String COLUMN_NAME = "name";
    public static final String USER_ID = "userId";
    public static final String COLUMN_DETAIL = "detail";
    public static final String COLUMN_IMAGES = "images";
    public static final String COLUMN_PRODUCTRUPEEFINAL = "productrupeefinal";
    public static final String COLUMN_PRODUCTRUPEEMRP = "productrupeemrp";
    public static final String COLUMN_CART = "cart";
    public static final String COLUMN_SUBCATEGORY = "subCategory";
    public static final String COLUMN_SUB_CAT_STATUS = "sub_cat_status";
    public static final String COLUMN_TAX = "tax";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_P_NO = "p_no";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_OFFER = "offer";
    public static final String COLUMN_IMG1 = "img1";
    public static final String COLUMN_IMG2 = "img2";
    public static final String COLUMN_IMG3 = "img3";
    public static final String COLUMN_IMG4 = "img4";
    public static final String COLUMN_IMG5 = "img5";
    public static final String COLUMN_IMG6 = "img6";
    public static final String COLUMN_TAX_TYPE = "tax_type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_VIDEO = "video";
    public static final String COLUMN_WEIGHT = "weight";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " name,"
                    + COLUMN_PRO_ID + " proid,"
                    + USER_ID + " userId,"
                    + COLUMN_DETAIL + " detail,"
                    + COLUMN_IMAGES + " images,"
                    + COLUMN_PRODUCTRUPEEFINAL + " productrupeefinal,"
                    + COLUMN_PRODUCTRUPEEMRP + " productrupeemrp,"
                    + COLUMN_CART + " cart,"
                    + COLUMN_SUBCATEGORY + " subCategory,"
                    + COLUMN_SUB_CAT_STATUS + " sub_cat_status,"
                    + COLUMN_TAX + " tax,"
                    + COLUMN_QTY + " qty,"
                    + COLUMN_P_NO + " p_no,"
                    + COLUMN_CURRENCY + " currency,"
                    + COLUMN_OFFER + " offer,"
                    + COLUMN_IMG1 + " img1,"
                    + COLUMN_IMG2 + " img2,"
                    + COLUMN_IMG3 + " img3,"
                    + COLUMN_IMG4 + " img4,"
                    + COLUMN_IMG5 + " img5,"
                    + COLUMN_IMG6 + " img6,"
                    + COLUMN_TAX_TYPE + " tax_type,"
                    + COLUMN_DESCRIPTION + " description,"
                    + COLUMN_VIDEO + " video,"
                    + COLUMN_WEIGHT+ " weight"

                    + ")";

    public String id;
    public String name;
    public String detail;
    public String images;
    public String productrupeefinal;
    public String productrupeemrp;
    public String cart;
    public String subCategory;
    public String sub_cat_status;
    public String tax;
    public String qty;
    public String p_no;
    public String currency;
    public String offer;
    public String img1;
    public String img2;
    public String img3;
    public String img4;
    public String img5;
    public String img6;
    public String tax_type;
    public String description;
    public String video;
    public String weight;



    public ProductBean() {
    }

    public ProductBean(String id, String name, String detail, String images,
                       String productrupeefinal, String productrupeemrp, String cart,
                       String subCategory, String sub_cat_status, String tax, String qty,
                       String p_no, String currency, String offer, String img1, String img2,
                       String img3, String img4, String img5, String img6, String tax_type,
                       String description, String video,String weight) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.images = images;
        this.productrupeefinal = productrupeefinal;
        this.productrupeemrp = productrupeemrp;
        this.cart = cart;
        this.subCategory = subCategory;
        this.sub_cat_status = sub_cat_status;
        this.tax = tax;
        this.qty = qty;
        this.p_no = p_no;
        this.currency = currency;
        this.offer = offer;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
        this.img6 = img6;
        this.tax_type = tax_type;
        this.description = description;
        this.video = video;
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnName() {
        return COLUMN_NAME;
    }

    public static String getColumnDetail() {
        return COLUMN_DETAIL;
    }

    public static String getColumnImages() {
        return COLUMN_IMAGES;
    }

    public static String getColumnProductrupeefinal() {
        return COLUMN_PRODUCTRUPEEFINAL;
    }

    public static String getColumnProductrupeemrp() {
        return COLUMN_PRODUCTRUPEEMRP;
    }

    public static String getColumnCart() {
        return COLUMN_CART;
    }

    public static String getColumnSubcategory() {
        return COLUMN_SUBCATEGORY;
    }

    public static String getColumnSubCatStatus() {
        return COLUMN_SUB_CAT_STATUS;
    }

    public static String getColumnTax() {
        return COLUMN_TAX;
    }

    public static String getColumnQty() {
        return COLUMN_QTY;
    }

    public static String getColumnPNo() {
        return COLUMN_P_NO;
    }

    public static String getColumnCurrency() {
        return COLUMN_CURRENCY;
    }

    public static String getColumnOffer() {
        return COLUMN_OFFER;
    }

    public static String getColumnImg1() {
        return COLUMN_IMG1;
    }

    public static String getColumnImg2() {
        return COLUMN_IMG2;
    }

    public static String getColumnImg3() {
        return COLUMN_IMG3;
    }

    public static String getColumnImg5() {
        return COLUMN_IMG5;
    }

    public static String getColumnImg6() {
        return COLUMN_IMG6;
    }

    public static String getColumnTaxType() {
        return COLUMN_TAX_TYPE;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getProductrupeefinal() {
        return productrupeefinal;
    }

    public void setProductrupeefinal(String productrupeefinal) {
        this.productrupeefinal = productrupeefinal;
    }

    public String getProductrupeemrp() {
        return productrupeemrp;
    }

    public void setProductrupeemrp(String productrupeemrp) {
        this.productrupeemrp = productrupeemrp;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSub_cat_status() {
        return sub_cat_status;
    }

    public void setSub_cat_status(String sub_cat_status) {
        this.sub_cat_status = sub_cat_status;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getP_no() {
        return p_no;
    }

    public void setP_no(String p_no) {
        this.p_no = p_no;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    public String getImg6() {
        return img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }

    public String getTax_type() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

    public static String getColumnProId() {
        return COLUMN_PRO_ID;
    }

    public static String getUserId() {
        return USER_ID;
    }

    public static String getColumnDescription() {
        return COLUMN_DESCRIPTION;
    }

    public static String getColumnVideo() {
        return COLUMN_VIDEO;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public static String getColumnImg4() {
        return COLUMN_IMG4;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }
}