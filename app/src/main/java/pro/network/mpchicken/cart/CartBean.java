package pro.network.mpchicken.cart;

import pro.network.mpchicken.product.ProductListBean;

import java.util.ArrayList;

public class CartBean {
    String order_date;
    String cart_id;
    String total_amount;
    String currency;
    ArrayList<ProductListBean> data;

    public CartBean(String order_date, String cart_id, String total_amount, String currency, ArrayList<ProductListBean> data) {
        this.order_date = order_date;
        this.cart_id = cart_id;
        this.total_amount = total_amount;
        this.currency = currency;
        this.data = data;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ArrayList<ProductListBean> getData() {
        return data;
    }

    public void setData(ArrayList<ProductListBean> data) {
        this.data = data;
    }
}