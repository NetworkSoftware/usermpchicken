package pro.network.mpchicken.wallet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.mpchicken.R;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder>{
    private List<WalletBean> productList;
    private Context context;

    public WalletAdapter(Context context, List<WalletBean> contactList) {
        this.context = context;
        this.productList = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WalletBean walletBean = productList.get(position);
        holder.dated.setText(walletBean.getCreatedon());
        holder.amount.setText("Rs. " + walletBean.getAmt());
        if (walletBean.operation.equalsIgnoreCase("add")) {
            holder.type.setText("Credited");
            holder.amount.setTextColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            holder.type.setText("Debited");
            holder.amount.setTextColor(ColorStateList.valueOf(Color.parseColor("#F44336")));
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


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, dated, type;

        public MyViewHolder(View view) {
            super(view);
            amount = view.findViewById(R.id.amount);
            dated = view.findViewById(R.id.dated);
            type = view.findViewById(R.id.type);
        }
    }


}
