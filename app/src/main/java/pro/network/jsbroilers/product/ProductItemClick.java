package pro.network.jsbroilers.product;

public interface ProductItemClick {

    void onProductClick(ProductListBean position);
    void OnQuantityChange(int position,int qty);
    void onCartClick(ProductListBean position);

}


