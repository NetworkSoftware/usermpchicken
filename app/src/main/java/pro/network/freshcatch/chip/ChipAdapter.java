package pro.network.freshcatch.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pro.network.freshcatch.R;

public class ChipAdapter extends RecyclerView.Adapter<pro.network.freshcatch.chip.ChipAdapter.MyViewHolder> {


    private final Context context;
    private final OnChip onBlock;
    private ArrayList<ChipBean> blogArrayList;
    private String selectedPosition;


    public ChipAdapter(Context context, ArrayList<ChipBean> blogArrayList, OnChip onBlock, String selectedPosition) {
        this.blogArrayList = blogArrayList;
        this.context = context;
        this.onBlock = onBlock;
        this.selectedPosition = selectedPosition;
    }

    public void notifyData(ArrayList<ChipBean> myList) {
        this.blogArrayList = myList;
        notifyDataSetChanged();
    }

    public void notifyData(String selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chip_activity, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ChipBean bean = blogArrayList.get(position);
        holder.action_chip.setText(bean.chip);
        if (bean.chip.equalsIgnoreCase(selectedPosition)) {
            holder.action_chip.setChipBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.chipSelected)));
            holder.action_chip.setChipStrokeColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
        } else {
            holder.action_chip.setChipBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.chipUnSelect)));
            holder.action_chip.setChipStrokeColor(ColorStateList.valueOf(context.getResources().getColor(R.color.light_gray)));
        }

        holder.action_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBlock.onItemClick(bean.chip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        com.google.android.material.chip.Chip action_chip;

        public MyViewHolder(View view) {
            super(view);
            action_chip = view.findViewById(R.id.action_chip);
        }
    }
}
