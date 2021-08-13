package pro.network.freshcatch.wallet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pro.network.freshcatch.R;
import pro.network.freshcatch.product.ProductListBean;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<WalletBean> productListFiltered;
    private List<WalletBean> productList;
    private ContactsAdapterListener listener;

    public WalletAdapter(Context context, List<WalletBean> contactList, WalletActivity mainActivityProduct) {
        this.context = context;
        this.listener = listener;
        this.productList = contactList;
        this.productListFiltered = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WalletBean walletBean = productListFiltered.get(position);
        holder.dated.setText(walletBean.getCreatedon());
        holder.type.setText(walletBean.getType());
        holder.paymentId.setText("Payment Id: # " + walletBean.getPaymentId());
        holder.amount.setText("Rs. " + walletBean.getAmount());
        if (walletBean.getType().equalsIgnoreCase("in")) {
            holder.amount.setTextColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            holder.amount.setTextColor(ColorStateList.valueOf(Color.parseColor("#F44336")));
        }

    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<WalletBean> filteredList = new ArrayList<>();
                    for (WalletBean row : productList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String val = row.getType();
                        if (val.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else if (row.getCreatedon().contains(charString.toLowerCase())) {
                            filteredList.add(row);

                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<WalletBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyData(List<WalletBean> productList) {
        this.productListFiltered = productList;
        this.productList = productList;
        notifyDataSetChanged();
    }

    public interface ContactsAdapterListener {
        void onContactSelected(ProductListBean product);

        void onStatusChanged(ProductListBean product, String status);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, dated, type,paymentId;

        public MyViewHolder(View view) {
            super(view);
            amount = view.findViewById(R.id.amount);

            dated = view.findViewById(R.id.dated);
            type = view.findViewById(R.id.type);
            paymentId=view.findViewById(R.id.paymentId);
        }
    }


}
