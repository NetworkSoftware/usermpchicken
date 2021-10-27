package pro.network.mpchicken;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import pro.network.mpchicken.product.ProductListBean;

import java.util.List;

public class SearchProdcutAdapter extends RecyclerView.Adapter<SearchProdcutAdapter.SearchProductView> {

    private List<ProductListBean> searchList;

    public class SearchProductView extends RecyclerView.ViewHolder {
        public TextView title;

        public SearchProductView(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.pNAme);
        }
    }


    public SearchProdcutAdapter(List<ProductListBean> searchList) {
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public SearchProductView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_searchlist, parent, false);

        return new SearchProductView(itemView);
    }

    @Override
    public void onBindViewHolder(SearchProductView holder, int position) {
        ProductListBean ss = searchList.get(position);
        holder.title.setText(ss.getName());
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
}
