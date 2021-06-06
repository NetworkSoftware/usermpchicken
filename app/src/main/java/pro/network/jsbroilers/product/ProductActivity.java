package pro.network.jsbroilers.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pro.network.jsbroilers.R;
import pro.network.jsbroilers.app.AppConfig;
import pro.network.jsbroilers.app.BaseActivity;
import pro.network.jsbroilers.app.DbCart;
import pro.network.jsbroilers.cart.CartActivity;

import static pro.network.jsbroilers.app.AppConfig.mypreference;

public class ProductActivity extends BaseActivity implements ViewClick {
    public ArrayList<ProductListBean> productBeans;
    SharedPreferences sharedpreferences;
    TextView product_price, product_descrpition, product_name,outOfStock;
    ExtendedFloatingActionButton cart;
    int currentImage = 0;
    private DbCart db;
    private PhotoView photoView;
    private RecyclerView baseList;
    private AttachmentViewAdapter attachmentBaseAdapter;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_product_new);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        db = new DbCart(this);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);


        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_descrpition = findViewById(R.id.product_descrpition);
        outOfStock = findViewById(R.id.outOfStock);

        cart = findViewById(R.id.cart);


        final ProductListBean productBean = (ProductListBean) getIntent().getSerializableExtra("data");


        final ArrayList<String> urls = new Gson().fromJson(productBean.image, (Type) List.class);
        photoView = findViewById(R.id.product_image);
        Glide.with(ProductActivity.this)
                .load(AppConfig.getResizedImage(urls.get(currentImage), false))
                .into(photoView);
        product_price.setText("Rs. " + productBean.getPrice() +"/"+productBean.getRqty() + "" + productBean.getRqtyType());
        product_name.setText(productBean.getBrand());
        outOfStock.setText(productBean.getStock_update());

        if (productBean.getStock_update().equalsIgnoreCase("Currently Unavailable")) {
            outOfStock.setVisibility(View.VISIBLE);
            cart.setVisibility(View.GONE);
        } else {
            outOfStock.setVisibility(View.GONE);
            cart.setVisibility(View.VISIBLE);
        }

        product_descrpition.setText(productBean.getDescription());

        baseList = (RecyclerView) findViewById(R.id.attachmentList);
        attachmentBaseAdapter = new AttachmentViewAdapter(this, urls, this);
        baseList.setLayoutManager(new GridLayoutManager(this, 3));
        baseList.setAdapter(attachmentBaseAdapter);
        getSupportActionBar().setTitle(productBean.getModel());


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.isInCart(productBean.id, sharedpreferences.getString(AppConfig.user_id, ""))) {
                    startActivity(new Intent(ProductActivity.this, CartActivity.class));
                    finish();
                } else {
                    productBean.setQty("1");
                    long insert = db.insertProductInCart(productBean, sharedpreferences.getString(AppConfig.user_id, ""));
                    if (insert == 1) {
                        Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProductActivity.this, CartActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplication(), getString(R.string.sign), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (db.isInCart(productBean.id, sharedpreferences.getString(AppConfig.user_id, ""))) {
            cart.setText("Go to cart");
        } else {
            cart.setText("Add to cart");
        }


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onViewClick(String s) {
        Glide.with(ProductActivity.this)
                .load(AppConfig.getResizedImage(s, false))
                .into(photoView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, Bitmap> {
        public long totalSize = 0;
        String filepath;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (progress[0]));
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            filepath = params[0];
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private Bitmap uploadFile() {
            pDialog.setMessage("Sending ...");
            String responseString = null;

            Uri bmpUri = null;
            try {
                URL url = new URL(filepath);

                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "product_image" + ".png");
                file.getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                bmpUri = Uri.fromFile(file);
                return image;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            hideDialog();
            try {
                if (bitmap != null) {
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, bitmap);
                    intent.putExtra(Intent.EXTRA_TEXT, "Share");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/png");
                    startActivity(intent);
                }
            } catch (Error | Exception e) {
                Toast.makeText(getApplicationContext(), "Image not Sharing", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(bitmap);
        }

    }


}