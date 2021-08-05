package pro.network.freshcatch.product;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.network.moeidbannerlibrary.banner.Utils;

import java.util.ArrayList;
import java.util.Objects;

import pro.network.freshcatch.R;


class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    ArrayList<String> images;

    // Layout Inflater
    LayoutInflater mLayoutInflater;


    // Viewpager Constructor
    public ViewPagerAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.banner_product_item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView =  itemView.findViewById(R.id.image);

        // setting the image in the imageView
      //  imageView.setImageResource(images[position]);

        String resizedPath = Utils.getResizedImage(images.get(position), true);
        Log.e("xxxxx", resizedPath);
        Glide.with(context).load(resizedPath).into(imageView);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout) object);
    }
}