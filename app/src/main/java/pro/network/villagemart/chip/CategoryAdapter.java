package pro.network.villagemart.chip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pro.network.villagemart.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    private List<ChipBean> categories;
    public OnChip onCategoryClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;
        LinearLayout linear;

        public MyViewHolder(View view) {
            super((view));
            linear = view.findViewById(R.id.linear);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
        }
    }

    public CategoryAdapter(Context mainActivityUser, List<ChipBean> productBeans, OnChip onCategoryClick) {
        this.mainActivityUser = mainActivityUser;
        this.categories = productBeans;
        this.onCategoryClick = onCategoryClick;
    }

    public void notifyData(List<ChipBean> productBeans) {
        this.categories = productBeans;
        notifyDataSetChanged();
    }


    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ChipBean productBean = categories.get(position);

        holder.title.setText(productBean.getTitle());
        holder.image.setImageDrawable(mainActivityUser.getResources().getDrawable(productBean.image));

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryClick.onCategoryItem(position);
            }
        });
    }

    public int getItemCount() {
        return categories.size();
    }

}