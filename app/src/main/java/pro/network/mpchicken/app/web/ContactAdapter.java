package pro.network.mpchicken.app.web;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pro.network.mpchicken.R;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Contact> moviesList;
    private OnContactListener onAboutListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemLinear;
        TextView title, summary;
        ImageView list_item_icon;
        public MyViewHolder(View view) {
            super(view);
            itemLinear = view.findViewById(R.id.itemLinear);
            list_item_icon = view.findViewById(R.id.list_item_icon);
            title = view.findViewById(R.id.title);
            summary = view.findViewById(R.id.summary);
        }
    }


    public ContactAdapter(Context mainActivityUser, ArrayList<Contact> moviesList, OnContactListener onAboutListener) {
        this.moviesList = moviesList;
        this.mainActivityUser = mainActivityUser;
        this.onAboutListener = onAboutListener;

    }

    public void notifyData(ArrayList<Contact> myList) {
        this.moviesList = myList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Contact bean = moviesList.get(position);
        holder.title.setText(bean.getTitle());
        holder.list_item_icon.setImageDrawable(bean.getImage());
        holder.summary.setText(bean.getSummary());
        holder.itemLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAboutListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
