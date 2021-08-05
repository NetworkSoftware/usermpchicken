package pro.network.freshcatch;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pro.network.freshcatch.app.AppConfig;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageZoomActivity extends AppCompatActivity {

LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_zoom);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
       TextView product_name=(TextView)findViewById(R.id.product_name) ;
       product_name.setText(getIntent().getStringExtra("product_name"));
        Picasso.with(ImageZoomActivity.this)
                .load(AppConfig.getResizedImage(getIntent().getStringExtra("image"),false))
                .into(photoView);
        //photoView.setImageResource(R.drawable.image);

    }





}