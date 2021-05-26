package pro.network.jsbroilers.product;

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
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pro.network.jsbroilers.R;
import pro.network.jsbroilers.app.AppConfig;
import pro.network.jsbroilers.app.DatabaseHelperYalu;


public class SingleProductAdapter extends RecyclerView.Adapter<SingleProductAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    public ProductItemClick productItemClick;
    SharedPreferences preferences;
    int selectedPosition = 0;
    DatabaseHelperYalu databaseHelper;
    SharedPreferences sharedpreferences;
    private List<ProductListBean> productBeans;

    public SingleProductAdapter(Context mainActivityUser, List<ProductListBean> productBeans, ProductItemClick productItemClick, SharedPreferences sharedPreferences) {
        this.mainActivityUser = mainActivityUser;
        this.productBeans = productBeans;
        this.productItemClick = productItemClick;
        sharedpreferences = mainActivityUser.getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelperYalu(mainActivityUser);
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
        holder.rqty.setText(productBean.getRqty() + "" + productBean.getRqtyType());
        ArrayList<String> urls = new Gson().fromJson(productBean.image, (Type) List.class);
        Glide.with(mainActivityUser)
                .load(AppConfig.getResizedImage(urls.get(0), true))
                .into(holder.product_image);
        holder.product_rupee_final.setText("Rs." + productBean.getPrice());


        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productItemClick.onCartClick(productBean);
                holder.quantity.setText(productBean.getQty());
                if (productBean.getQty().equalsIgnoreCase("0")) {
                    holder.add_qty.setVisibility(View.INVISIBLE);
                } else {
                    holder.add_qty.setVisibility(View.VISIBLE);
                }
            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuan = Integer.parseInt(holder.quantity.getText().toString()) - 1;
                doCallCartChange(newQuan, holder, position);
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuan = Integer.parseInt(holder.quantity.getText().toString()) + 1;
                doCallCartChange(newQuan, holder, position);
            }
        });

        if (productBean.getStock_update().equalsIgnoreCase("In Stock")) {
            holder.outOfStock.setVisibility(View.GONE);
        } else {
            holder.outOfStock.setVisibility(View.VISIBLE);
        }

        holder.outOfStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });

        if (databaseHelper.isInCartyalu(productBean.id,
                sharedpreferences.getString(AppConfig.user_id, ""))) {
            holder.cart.setVisibility(View.GONE);
            holder.add_qty.setVisibility(View.VISIBLE);
            holder.quantity.setText(databaseHelper.getQuantityInCart(productBean.id,
                    sharedpreferences.getString(AppConfig.user_id, "")));
        } else {
            holder.cart.setVisibility(View.VISIBLE);
            holder.add_qty.setVisibility(View.GONE);
        }

        holder.product_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemClick.onProductClick(productBean);
            }
        });


    }

    private void doCallCartChange(int newQuan, SingleProductAdapter.MyViewHolder holder, int position) {
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

        private final View outOfStock;
        private final CardView product_card;
        private final ImageView product_image;
        private final ImageView minus;
        private final ImageView plus;
        private final TextView product_name;
        private final TextView product_rupee_final;
        private final TextView brand;
        private final TextView quantity;
        private final TextView rqty;
        private final LinearLayout add_qty;
        MaterialButton cart;

        public MyViewHolder(View view) {
            super((view));
            product_card = (CardView) view.findViewById(R.id.product_card);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            cart = view.findViewById(R.id.cart);
            product_name = (TextView) view.findViewById(R.id.product_name);
            rqty = (TextView) view.findViewById(R.id.rqty);
            quantity = (TextView) view.findViewById(R.id.quantity);
            brand = (TextView) view.findViewById(R.id.brand);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            add_qty = view.findViewById(R.id.add_qty);

            product_rupee_final = (TextView) view.findViewById(R.id.product_rupee_final);
            outOfStock = view.findViewById(R.id.outOfStock);
        }
    }

}


