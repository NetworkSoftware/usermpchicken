package pro.network.mpchicken.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pro.network.mpchicken.R;
import pro.network.mpchicken.app.AppConfig;


public class AttachmentViewAdapter extends RecyclerView.Adapter<AttachmentViewAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<String> moviesList;
    private ViewClick attachmentClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview;

        public MyViewHolder(View view) {
            super(view);
            imageview = (ImageView) view.findViewById(R.id.imageview);
        }
    }


    public AttachmentViewAdapter(Context mainActivityUser, ArrayList<String> moviesList, ViewClick attachmentClick) {
        this.moviesList = moviesList;
        this.mainActivityUser = mainActivityUser;
        this.attachmentClick = attachmentClick;

    }

    public void notifyData(ArrayList<String> myList) {
        this.moviesList = myList;
        notifyDataSetChanged();
    }

    public void notifyDataItem(ArrayList<String> myList, int position) {
        this.moviesList = myList;
        notifyItemChanged(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_attachment_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String bean = moviesList.get(position);
        Glide.with(mainActivityUser)
                .load(AppConfig.getResizedImage(bean, true))
                .into(holder.imageview);
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentClick.onViewClick(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
