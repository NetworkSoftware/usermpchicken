package pro.network.villagemart.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pro.network.villagemart.R;
import pro.network.villagemart.app.AppConfig;
import pro.network.villagemart.product.ProductListBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyOrderListSubAdapter extends RecyclerView.Adapter<MyOrderListSubAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<ProductListBean> myorderBeans;
    SharedPreferences preferences;
    int selectedPosition = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView qty;


        public MyViewHolder(View view) {
            super((view));
            product_image = (ImageView) view.findViewById(R.id.product_image);
            qty = (TextView) view.findViewById(R.id.qty);

        }
    }

    public MyOrderListSubAdapter(Context mainActivityUser, ArrayList<ProductListBean> myorderBeans) {
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

    public MyOrderListSubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorders_list_sub, parent, false);

        return new MyOrderListSubAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductListBean myorderBean = myorderBeans.get(position);

        holder.qty.setText(myorderBean.getQty() + " "+myorderBean.getBrand());
        Picasso.with(mainActivityUser)
                .load(AppConfig.getResizedImage(myorderBean.getImage(), true))
                .placeholder(R.drawable.vivo)
                .error(R.drawable.vivo)
                .into(holder.product_image);

    }

    public int getItemCount() {
        return myorderBeans.size();
    }

}


