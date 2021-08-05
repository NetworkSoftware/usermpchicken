package pro.network.freshcatch.product;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.DbCart;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private List<ProductListBean> productBeans;
    SharedPreferences preferences;
    public ProductItemClick productItemClick;
    int selectedPosition = 0;
    DbCart databaseHelper;
    SharedPreferences sharedpreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final View outOfStock;
        private CardView product_card;
        private ImageView product_image;
        private TextView product_name, product_rupee_final;
        MaterialButton cart;

        public MyViewHolder(View view) {
            super((view));
            product_card = (CardView) view.findViewById(R.id.product_card);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            cart = view.findViewById(R.id.add_cart);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_rupee_final = (TextView) view.findViewById(R.id.product_rupee_final);
            outOfStock = (TextView) view.findViewById(R.id.outOfStock);
        }
    }

    public ProductListAdapter(Context mainActivityUser, List<ProductListBean> productBeans, ProductItemClick productItemClick, SharedPreferences sharedPreferences) {
        this.mainActivityUser = mainActivityUser;
        this.productBeans = productBeans;
        this.productItemClick = productItemClick;
        sharedpreferences = mainActivityUser.getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);
        databaseHelper = new DbCart(mainActivityUser);
    }


    public void notifyData(List<ProductListBean> productBeans) {
        this.productBeans = productBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public ProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_single_product, parent, false);

        return new ProductListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ProductListBean productBean = productBeans.get(position);

        holder.product_name.setText(productBean.getBrand() + " " + productBean.getModel());
        ArrayList<String> urls = new Gson().fromJson(productBean.image, (Type) List.class);
        Glide.with(mainActivityUser)
                .load(AppConfig.getResizedImage(urls.get(0), true))
                .into(holder.product_image);
        holder.product_rupee_final.setText("Rs." + productBean.getPrice());

        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemClick.onCartClick(productBean);
            }
        });
        if (productBean.getStock_update().equalsIgnoreCase("Currently Unavailable")) {
            holder.outOfStock.setVisibility(View.VISIBLE);
            holder.cart.setVisibility(View.GONE);
        } else {
            holder.outOfStock.setVisibility(View.GONE);
            holder.cart.setVisibility(View.VISIBLE);
        }
        holder.outOfStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });
        if (databaseHelper.isInCart(productBean.id, sharedpreferences.getString(AppConfig.user_id, ""))) {
            holder.cart.setVisibility(View.GONE);
        } else {
            holder.cart.setVisibility(View.VISIBLE);
        }

        holder.product_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemClick.onProductClick(productBean);
            }
        });


    }

    public int getItemCount() {
        return productBeans.size();
    }

}


