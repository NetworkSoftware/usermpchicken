package pro.network.freshcatch.product;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.DbCart;


public class SingleProductAdapter extends RecyclerView.Adapter<SingleProductAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    public ProductItemClick productItemClick;
    int selectedPosition = 0;
    DbCart databaseHelper;
    SharedPreferences sharedpreferences;
    private List<ProductListBean> productBeans;

    public SingleProductAdapter(Context mainActivityUser, List<ProductListBean> productBeans, ProductItemClick productItemClick, SharedPreferences sharedPreferences) {
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

    public SingleProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_product, parent, false);

        return new SingleProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductListBean productBean = productBeans.get(position);

        holder.product_name.setText(productBean.getModel());
        holder.brand.setText(productBean.getBrand());
        ArrayList<String> urls = new Gson().fromJson(productBean.image, (Type) List.class);
        Glide.with(mainActivityUser)
                .load(AppConfig.getResizedImage(urls.get(0), true))
                .into(holder.product_image);
        holder.product_rupee_final.setText("Rs." + productBean.getPrice() + " / " + productBean.getRqty() + "" + productBean.getRqtyType());


        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productItemClick.onCartClick(productBean);
                holder.quantity.setText(productBean.getQty());
            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float newQuan = Float.parseFloat(holder.quantity.getText().toString()) - 0.5f;
                doCallCartChange(newQuan, holder, position);
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float newQuan = Float.parseFloat(holder.quantity.getText().toString()) + 0.5f;
                doCallCartChange(newQuan, holder, position);
            }
        });

        if (productBean.getStock_update().equalsIgnoreCase("Currently Unavailable")) {
            holder.hideDrop.setVisibility(View.VISIBLE);
        } else {
            holder.hideDrop.setVisibility(View.GONE);
        }

        holder.linearPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!productBean.getStock_update().equalsIgnoreCase("Currently Unavailable")) {
                    productItemClick.onProductClick(productBean);
                }
            }
        });

        holder.hideDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        if (databaseHelper.isInCart(productBean.id,
                sharedpreferences.getString(AppConfig.user_id, ""))) {
            holder.cart.setVisibility(View.GONE);
            holder.add_qty.setVisibility(View.VISIBLE);
            holder.quantity.setText(databaseHelper.getQuantityInCart(productBean.id,
                    sharedpreferences.getString(AppConfig.user_id, "")));

        } else {
            holder.cart.setVisibility(View.VISIBLE);
            holder.add_qty.setVisibility(View.GONE);
            holder.quantity.setText("0");
        }

        holder.quantityDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                float val=0;
                for (int k = 0; k < 20; k++) {
                    val=val+0.5f;
                    popup.getMenu().add(String.valueOf(val));
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        float newQuan = Float.parseFloat(item.getTitle().toString());
                        doCallCartChange(newQuan, holder, position);
                        return true;
                    }
                });
                popup.show();
            }
        });


    }

    private void doCallCartChange(float newQuan, SingleProductAdapter.MyViewHolder holder, int position) {
        holder.quantity.setText(newQuan + "");
        if (newQuan == 0) {
            holder.cart.setVisibility(View.VISIBLE);
            holder.add_qty.setVisibility(View.GONE);
        } else {
            holder.cart.setVisibility(View.GONE);
            holder.add_qty.setVisibility(View.VISIBLE);
        }
        productItemClick.OnQuantityChange(position, newQuan);
    }

    public int getItemCount() {
        return productBeans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView product_card;
        private final ImageView product_image;
        private final ImageView minus;
        private final ImageView plus;
        private final TextView product_name;
        private final TextView product_rupee_final;
        private final TextView brand;
        private final LinearLayout add_qty, linearPro, quantityDrop;
        private final LinearLayout hideDrop;
        MaterialButton cart;
        private final TextView quantity;

        public MyViewHolder(View view) {
            super((view));

            product_card = view.findViewById(R.id.product_card);
            product_image = view.findViewById(R.id.product_image);
            cart = view.findViewById(R.id.add_cart);
            product_name = view.findViewById(R.id.product_name);
            quantity = view.findViewById(R.id.quantity);
            brand = view.findViewById(R.id.brand);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            add_qty = view.findViewById(R.id.add_qty);
            linearPro = view.findViewById(R.id.linearPro);
            hideDrop=view.findViewById(R.id.hideDrop);
            quantityDrop = view.findViewById(R.id.quantityDrop);
            product_rupee_final = view.findViewById(R.id.product_rupee_final);
        }
    }

}


