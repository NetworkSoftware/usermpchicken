package pro.network.spirtual.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pro.network.spirtual.R;
import pro.network.spirtual.app.AppConfig;
import pro.network.spirtual.product.ProductListBean;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static pro.network.spirtual.app.AppConfig.decimalFormat;


public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private List<ProductListBean> productBeans;
    SharedPreferences preferences;
    public CartItemClick cartItemClick;
    int selectedPosition = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image, minus, plus;
        private TextView product_name, product_price, product_total_price, quantity;
        LinearLayout delete;

        public MyViewHolder(View view) {
            super((view));
            product_image = (ImageView) view.findViewById(R.id.product_image);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            product_total_price = view.findViewById(R.id.product_total_price);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            quantity = view.findViewById(R.id.quantity);
            delete = view.findViewById(R.id.delete);

        }
    }

    public CartListAdapter(Context mainActivityUser, List<ProductListBean> productBeans, CartItemClick cartItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.productBeans = productBeans;
        this.cartItemClick = cartItemClick;
    }

    public void notifyData(List<ProductListBean> productBeans) {
        this.productBeans = productBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public CartListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cart_list, parent, false);

        return new CartListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductListBean productBean = productBeans.get(position);

        holder.product_name.setText(productBean.getBrand() + " - " + productBean.getModel());

        try {
            ArrayList<String> urls = new Gson().fromJson(productBean.image, (Type) List.class);
            Glide.with(mainActivityUser)
                    .load(AppConfig.getResizedImage(urls.get(0), true))
                    .into(holder.product_image);
        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());
        }

        float total = Float.parseFloat(productBean.price) * Integer.parseInt(productBean.getQty());
        holder.product_total_price.setText(productBean.getQty() + " * " + (productBean.price));
        holder.product_price.setText("₹" + decimalFormat.format(total) + ".00");

        holder.quantity.setText(productBean.getQty());
        if (productBean.getQty().equalsIgnoreCase("1")) {
            holder.minus.setVisibility(View.INVISIBLE);
        } else {
            holder.minus.setVisibility(View.VISIBLE);
        }
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuan = Integer.parseInt(holder.quantity.getText().toString()) - 1;

                doCallCartChange(newQuan, productBean, holder, position);

            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuan = Integer.parseInt(holder.quantity.getText().toString()) + 1;
                doCallCartChange(newQuan, productBean, holder, position);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItemClick.ondeleteClick(position);

            }
        });


    }

    private void doCallCartChange(int newQuan, ProductListBean productBean, MyViewHolder holder, int position) {
        float total = Float.parseFloat(productBean.price) * newQuan;
        holder.product_total_price.setText("Total: ₹" + decimalFormat.format(total) + ".00");
        holder.quantity.setText(newQuan + "");
        if (newQuan <= 1) {
            holder.minus.setVisibility(View.INVISIBLE);
        } else {
            holder.minus.setVisibility(View.VISIBLE);
        }
        cartItemClick.OnQuantityChange(position, newQuan);
    }

    public int getItemCount() {
        return productBeans.size();
    }

}


