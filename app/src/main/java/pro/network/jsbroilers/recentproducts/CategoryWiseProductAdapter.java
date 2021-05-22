package pro.network.jsbroilers.recentproducts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pro.network.jsbroilers.AllProductActivity;
import pro.network.jsbroilers.HomeActivity;
import pro.network.jsbroilers.R;
import pro.network.jsbroilers.app.AppConfig;
import pro.network.jsbroilers.app.DatabaseHelperYalu;
import pro.network.jsbroilers.product.ProductItemClick;
import pro.network.jsbroilers.product.ProductListAdapter;
import pro.network.jsbroilers.product.ProductListBean;


public class CategoryWiseProductAdapter extends RecyclerView.Adapter<CategoryWiseProductAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    public ProductItemClick productItemClick;
    private List<CategoryProduct> productBeans;

    public CategoryWiseProductAdapter(Context mainActivityUser, List<CategoryProduct> productBeans,
                                      ProductItemClick productItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.productBeans = productBeans;
        this.productItemClick = productItemClick;
    }

    public void notifyData(List<CategoryProduct> productBeans) {
        this.productBeans = productBeans;
        notifyDataSetChanged();
    }


    public CategoryWiseProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_category_recent, parent, false);

        return new CategoryWiseProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CategoryProduct productBean = productBeans.get(position);
        holder.itemName.setText(productBean.getTitle());

        RecentProductAdapter recentProductAdapter = new RecentProductAdapter(mainActivityUser, productBean.productListBeans, productItemClick);
        LinearLayoutManager addManager1 = new LinearLayoutManager(mainActivityUser,LinearLayoutManager.HORIZONTAL, false);
        holder.categoryProduct.setLayoutManager(addManager1);
        holder.categoryProduct.setAdapter(recentProductAdapter);

        holder.fullItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivityUser, AllProductActivity.class);
                intent.putExtra("type", productBean.getTitle());
                mainActivityUser.startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return productBeans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private final ImageView fullItems;
        private final TextView itemName;
        RecyclerView categoryProduct;

        public MyViewHolder(View view) {
            super((view));
            itemName = view.findViewById(R.id.itemName);
            fullItems = view.findViewById(R.id.fullItems);
            categoryProduct = view.findViewById(R.id.categoryProduct);
        }
    }

}


