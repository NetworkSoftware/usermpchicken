package pro.network.mpchicken.recentproducts;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pro.network.mpchicken.R;
import pro.network.mpchicken.app.AppConfig;
import pro.network.mpchicken.app.DbCart;
import pro.network.mpchicken.product.ProductItemClick;
import pro.network.mpchicken.product.ProductListBean;

public class RecentProductAdapter extends RecyclerView.Adapter<RecentProductAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    public ProductItemClick productItemClick;
    SharedPreferences preferences;
    int selectedPosition = 0;
    DbCart databaseHelper;
    SharedPreferences sharedpreferences;
    private List<ProductListBean> productBeans;

    public RecentProductAdapter(Context mainActivityUser, List<ProductListBean> productBeans,
                                ProductItemClick productItemClick) {
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

    public RecentProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recent_item, parent, false);

        return new RecentProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentProductAdapter.MyViewHolder holder, final int position) {
        final ProductListBean productBean = productBeans.get(position);

        holder.product_name.setText(productBean.getModel());
        ArrayList<String> urls = new Gson().fromJson(productBean.image, (Type) List.class);
        Glide.with(mainActivityUser)
                .load(AppConfig.getResizedImage(urls.get(0), true))
                .into(holder.product_image);
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemClick.onCartClick(productBean);
            }
        });
        if (productBean.getStock_update().equalsIgnoreCase("In Stock")) {
            holder.outOfStock.setVisibility(View.GONE);
            holder.cart.setVisibility(View.VISIBLE);
        } else {
            holder.outOfStock.setVisibility(View.VISIBLE);
            holder.cart.setVisibility(View.GONE);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView product_card;
        private final ImageView product_image;
        private final TextView product_name;
        LinearLayout cart;
        View outOfStock;

        public MyViewHolder(View view) {
            super((view));
            product_card = view.findViewById(R.id.product_card);
            product_image =  view.findViewById(R.id.product_image);
            product_name =  view.findViewById(R.id.product_name);
            outOfStock = view.findViewById(R.id.outOfStock);
            cart = view.findViewById(R.id.cart);
        }
    }

}



