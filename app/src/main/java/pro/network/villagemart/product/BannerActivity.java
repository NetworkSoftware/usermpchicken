package pro.network.villagemart.product;

import android.content.ActivityNotFoundException;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import pro.network.villagemart.R;
import pro.network.villagemart.app.AppConfig;
import pro.network.villagemart.app.BaseActivity;
import pro.network.villagemart.app.DatabaseHelperYalu;

import static pro.network.villagemart.app.AppConfig.mypreference;

public class BannerActivity extends BaseActivity {
    private DatabaseHelperYalu db;
    SharedPreferences sharedpreferences;

    TextView product_descrpition;
    private PhotoView photoView;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_banner);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ((LinearLayout) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((LinearLayout) findViewById(R.id.whatsapp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getCallingActivity() != null &&
                        getCallingActivity().getPackageName().equals("pro.network.villagemart")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=+919952507579"
                                + "&text=" + "Hi VillageMart, I would like buy this " + getIntent().getStringExtra("description") + " Place"));
                        intent.setPackage("com.whatsapp.w4b");
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=+919952507579"
                                + "&text=" + "Hi VillageMart, I would like buy this " + getIntent().getStringExtra("description") + " Place"));
                        intent.setPackage("com.whatsapp");
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Whatsapp Not found", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        db = new DatabaseHelperYalu(this);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);


        product_descrpition = findViewById(R.id.product_descrpition);

        photoView = findViewById(R.id.product_image);
        Glide.with(BannerActivity.this)
                .load(AppConfig.getResizedImage(getIntent().getStringExtra("image"), false))
                .into(photoView);
        product_descrpition.setText(getIntent().getStringExtra("description"));


//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setSubtitle(productBean.getBrand());

    }

    private class UploadFileToServer extends AsyncTask<String, Integer, Bitmap> {
        String filepath;
        public long totalSize = 0;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (String.valueOf(progress[0])));
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
                    Intent intent = new Intent(Intent.ACTION_SEND);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


}