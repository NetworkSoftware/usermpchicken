package pro.network.mpchicken.product;

public interface ProductItemClick {

    void onProductClick(ProductListBean position);
    void OnQuantityChange(int position,float qty);
    void onCartClick(ProductListBean position);
    void onDropQnt(ProductListBean position);

}


