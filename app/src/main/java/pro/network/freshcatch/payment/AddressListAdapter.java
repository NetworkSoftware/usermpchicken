package pro.network.freshcatch.payment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.freshcatch.R;


public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    public AddressItemClick addressItemClick;
    int selectedPosition = 0;
    private List<Address> productBeans;

    public AddressListAdapter(Context mainActivityUser, List<Address> productBeans, AddressItemClick addressItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.productBeans = productBeans;
        this.addressItemClick = addressItemClick;
    }

    public void notifyData(List<Address> productBeans) {
        this.productBeans = productBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_address_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Address addressItem = productBeans.get(position);
        holder.mobile.setText(addressItem.mobile + ",");
        holder.alterMobile.setText(addressItem.alternateMobile);
        holder.address.setText(addressItem.address);
        holder.pincode.setText(addressItem.pincode);
        holder.name.setText(addressItem.name);
        holder.comments.setText(addressItem.comments);
        holder.comments.setVisibility(View.VISIBLE);
        if(addressItem.comments.equalsIgnoreCase("NA")){
            holder.comments.setVisibility(View.GONE);
        }

        holder.itemLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressItemClick.onSelectItem(position);
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mainActivityUser, holder.more);
                popup.inflate(R.menu.popup_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                addressItemClick.onEditClick(position);
                                return true;
                            case R.id.delete:
                                addressItemClick.ondeleteClick(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });

        if (selectedPosition == position) {
            holder.itemLinear.
                    setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(R.color.colorPrimary)));
        } else {
            holder.itemLinear.
                    setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(android.R.color.transparent)));

        }
    }

    public int getItemCount() {
        return productBeans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mobile;
        private final TextView alterMobile;
        private final TextView address;
        private final TextView pincode;
        private final TextView name;
        private final TextView comments;
        LinearLayout itemLinear;
        ImageView more;

        public MyViewHolder(View view) {
            super((view));
            name = view.findViewById(R.id.name);
            mobile = view.findViewById(R.id.mobile);
            alterMobile = view.findViewById(R.id.alterMobile);
            address = view.findViewById(R.id.address);
            pincode = view.findViewById(R.id.pincode);
            comments = view.findViewById(R.id.comments);
            more = view.findViewById(R.id.more);
            itemLinear = view.findViewById(R.id.itemLinear);

        }
    }

}


