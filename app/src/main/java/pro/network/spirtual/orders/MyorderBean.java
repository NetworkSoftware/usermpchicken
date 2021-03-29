package pro.network.spirtual.orders;

import pro.network.spirtual.product.ProductListBean;

import java.util.ArrayList;

public class MyorderBean {
    String quantity, id, amount, status, items,createdon,reson;
    ArrayList<ProductListBean> productBeans;

    public MyorderBean() {
    }

    public MyorderBean(String quantity, String amount, String status, String items, String createdon, String reson, ArrayList<ProductListBean> productBeans) {
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
        this.items = items;
        this.createdon = createdon;
        this.reson = reson;
        this.productBeans = productBeans;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getitems() {
        return items;
    }

    public void setitems(String items) {
        this.items = items;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public ArrayList<ProductListBean> getProductBeans() {
        return productBeans;
    }

    public void setProductBeans(ArrayList<ProductListBean> productBeans) {
        this.productBeans = productBeans;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }
}