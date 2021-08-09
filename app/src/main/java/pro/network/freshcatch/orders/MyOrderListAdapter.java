package pro.network.freshcatch.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.product.ProductListBean;

import java.util.ArrayList;


public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<MyorderBean> myorderBean;
    ReturnOnClick returnOnClick;

    public void notifyData(ArrayList<MyorderBean> myorderBean) {
        this.myorderBean = myorderBean;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, quantity, amount, status, createdon, reason;
        Button return_product;
        RecyclerView cart_sub_list;
        CardView cart;

        public MyViewHolder(View view) {
            super((view));
            name = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            amount = (TextView) view.findViewById(R.id.amount);
            status = (TextView) view.findViewById(R.id.status);
            createdon = (TextView) view.findViewById(R.id.createdon);
            cart_sub_list = view.findViewById(R.id.cart_sub_list);
            reason = view.findViewById(R.id.reason);
            return_product = view.findViewById(R.id.return_product);
            cart = view.findViewById(R.id.cart);


        }
    }

    public MyOrderListAdapter(Context mainActivityUser,
                              ArrayList<MyorderBean> myorderBean, ReturnOnClick returnOnClick) {
        this.mainActivityUser = mainActivityUser;
        this.myorderBean = myorderBean;
        this.returnOnClick = returnOnClick;
    }




    public MyOrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorders_list, parent, false);

        return new MyOrderListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyorderBean been = myorderBean.get(position);


        holder.amount.setText(been.getAmount());
        holder.quantity.setText(been.getQuantity());
        holder.status.setText(been.getStatus());
        holder.createdon.setText(AppConfig.convertTimeToLocal(been.getCreatedon()));
        holder.reason.setText(been.getReson());
holder.cart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        returnOnClick.onCartClick(been);
    }
});
        holder.return_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnOnClick.onReturnClick(been.id);
            }
        });

        if (been.getStatus().equalsIgnoreCase("Delivered")
                && !AppConfig.checkReturnExpired(been.createdon)) {
            holder.return_product.setVisibility(View.VISIBLE);
        } else {
            holder.return_product.setVisibility(View.GONE);
        }

        MyOrderListSubAdapter myOrderListAdapter = new MyOrderListSubAdapter(mainActivityUser, been.getProductBeans());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(mainActivityUser, LinearLayoutManager.HORIZONTAL, false);
        holder.cart_sub_list.setLayoutManager(addManager1);
        holder.cart_sub_list.setAdapter(myOrderListAdapter);


    }

    public int getItemCount() {
        return myorderBean.size();
    }

}


