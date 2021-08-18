package pro.network.freshcatch.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pro.network.freshcatch.R;
import pro.network.freshcatch.product.ProductListBean;

import static pro.network.freshcatch.app.AppConfig.decimalFormat;


public class MyOrderListProAdapter extends RecyclerView.Adapter<MyOrderListProAdapter.MyViewHolder> {

    int selectedPosition = 0;
    private final Context mainActivityUser;
    private ArrayList<ProductListBean> myorderBeans;

    public MyOrderListProAdapter(Context mainActivityUser, ArrayList<ProductListBean> myorderBeans) {
        this.mainActivityUser = mainActivityUser;
        this.myorderBeans = myorderBeans;
    }

    public void notifyData(ArrayList<ProductListBean> myorderBeans) {
        this.myorderBeans = myorderBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_payment_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductListBean myorderBean = myorderBeans.get(position);

        holder.title.setText(myorderBean.getBrand() + " " + myorderBean.model);
        String qty = myorderBean.qty;
        try {
            if (qty == null || !qty.matches("-?\\d+(\\.\\d+)?")) {
                qty = "1";
            }
        } catch (Exception e) {

        }
        float startValue = Float.parseFloat(myorderBean.getPrice()) * Float.parseFloat(qty);
        holder.subtitle.setText(myorderBean.getQty() + "*" + myorderBean.getPrice() + "/" +
                myorderBean.getRqty() + " " + myorderBean.getRqtyType() +
                "=" + "₹" + decimalFormat.format(startValue) + ".00");

    }

    public int getItemCount() {
        return myorderBeans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView subtitle;


        public MyViewHolder(View view) {
            super((view));
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);

        }
    }

}


