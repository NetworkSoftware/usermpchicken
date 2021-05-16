package pro.network.webring.product;

import java.io.Serializable;

public class CategoryBeen implements Serializable {
    String id;
    int image;
    String category;
    String stock_upate;
    String stockcount;

    public String getStockcount() {
        return stockcount;
    }

    public void setStockcount(String stockcount) {
        this.stockcount = stockcount;
    }

    public CategoryBeen(int image, String category) {
        this.image = image;
        this.category = category;
    }

    public String getStock_upate() {
        return stock_upate;
    }

    public void setStock_upate(String stock_upate) {
        this.stock_upate = stock_upate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
