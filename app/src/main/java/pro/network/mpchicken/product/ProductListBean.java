package pro.network.mpchicken.product;

import java.io.Serializable;

public class ProductListBean implements Serializable {
    public static final String TABLE_NAME = "mpchicken";
    public static final String COLUMN_PRO_ID = "proid";
    public static final String COLUMN_ID = "id";
    public static final String USER_ID = "userId";
    public static final String COLUMN_CART = "cart";
    public static final String COLUMN_RQTY_TYPE = "rqtyType";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ROM = "rom";
    public static final String COLUMN_RQTY = "rqty";
    public static final String COLUMN_RAM = "ram";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_STOCKUPDATE = "stock_update";
    public static final String COLUMN_CATEGORY = "category";





    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PRO_ID + " proid,"
                    + USER_ID + " userId,"
                    + COLUMN_CART + " cart,"
                    + COLUMN_BRAND + " brand,"
                    + COLUMN_NAME + " name,"
                    + COLUMN_RQTY + " rqty,"
                    + COLUMN_ROM + " rom,"
                    + COLUMN_RAM + " ram,"
                    + COLUMN_PRICE + " price,"
                    + COLUMN_MODEL + " model,"
                    + COLUMN_IMAGE + " image,"
                    + COLUMN_DESCRIPTION + " description,"
                    + COLUMN_QTY + " qty,"
                    + COLUMN_STOCKUPDATE + " stock_update,"
                    + COLUMN_CATEGORY + " category,"
                    + COLUMN_RQTY_TYPE + " rqtyType"
                    + ")";

    public String id;

    public String userId;
    public String cart;
    public String brand;
    public String name;
    public String rom;
    public String ram;
    public String price;
    public String model;
    public String image;
    public String rqty;
    public String description;
    public String qty;
    public String stock_update;
    public String category;
    public String rqtyType;
    public String expressStock;

    public ProductListBean() {
    }

    public ProductListBean(String userId, String cart, String brand, String rqty, String name, String rom, String ram,
                           String price, String model, String image, String description, String qty, String stock_update,
                           String category, String rqtyType) {
        this.userId = userId;
        this.cart = cart;
        this.brand = brand;
        this.name = name;
        this.rom = rom;
        this.ram = ram;
        this.price = price;
        this.model = model;
        this.rqty = rqty;
        this.image = image;
        this.description = description;
        this.qty = qty;
        this.stock_update = stock_update;
        this.category = category;
        this.rqtyType = rqtyType;
    }


    public ProductListBean(String userId, String cart, String rqty, String brand, String name, String rom, String ram, String price,
                           String model, String image, String description, String qty, String stock_update, String category) {
        this.userId = userId;
        this.cart = cart;
        this.brand = brand;
        this.name = name;
        this.rom = rom;
        this.rqty = rqty;
        this.ram = ram;
        this.price = price;
        this.model = model;
        this.image = image;
        this.description = description;
        this.qty = qty;
        this.stock_update = stock_update;
        this.category = category;
    }

    public static String getColumnRqty() {
        return COLUMN_RQTY;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getUserId() {
        return USER_ID;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static String getColumnCart() {
        return COLUMN_CART;
    }

    public static String getColumnBrand() {
        return COLUMN_BRAND;
    }

    public static String getColumnName() {
        return COLUMN_NAME;
    }

    public static String getColumnRom() {
        return COLUMN_ROM;
    }

    public static String getColumnRam() {
        return COLUMN_RAM;
    }

    public static String getColumnPrice() {
        return COLUMN_PRICE;
    }

    public static String getColumnModel() {
        return COLUMN_MODEL;
    }

    public static String getColumnImage() {
        return COLUMN_IMAGE;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

    public static String getColumnProId() {
        return COLUMN_PRO_ID;
    }

    public static String getColumnDescription() {
        return COLUMN_DESCRIPTION;
    }

    public static String getColumnQty() {
        return COLUMN_QTY;
    }

    public static String getColumnStockupdate() {
        return COLUMN_STOCKUPDATE;
    }

    public static String getColumnCategory() {
        return COLUMN_CATEGORY;
    }

    public String getRqtyType() {
        return rqtyType;
    }

    public void setRqtyType(String rqtyType) {
        this.rqtyType = rqtyType;
    }

    public String getRqty() {
        return rqty;
    }

    public void setRqty(String rqty) {
        this.rqty = rqty;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStock_update() {
        return stock_update;
    }

    public void setStock_update(String stock_update) {
        this.stock_update = stock_update;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpressStock() {
        return expressStock;
    }

    public void setExpressStock(String expressStock) {
        this.expressStock = expressStock;
    }
}