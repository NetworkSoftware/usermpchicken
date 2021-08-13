package pro.network.freshcatch.wallet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.freshcatch.R;

/**
 * Created by ravi on 16/11/17.
 */

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.MyViewHolder> {
    private Context context;
    private List<WalletBean> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  operation, date;
        ImageView indicator;

        public MyViewHolder(View view) {

            super(view);
            operation = view.findViewById(R.id.operation);
            date = view.findViewById(R.id.date);
            indicator = view.findViewById(R.id.indicator);
        }
    }


    public WalletListAdapter(Context context, List<WalletBean> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final WalletBean product = productList.get(position);
        holder.date.setText(product.getCreatedon());

        if (product.getOperation().equalsIgnoreCase("add")) {
            holder.operation.setText("In");
        } else {
            holder.operation.setText("Out");
        }

        if (product.getOperation().equalsIgnoreCase("add")) {
            holder.indicator.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24));
            holder.indicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            holder.indicator.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24));
            holder.indicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public void notifyData(List<WalletBean> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
}
