package pro.network.freshcatch.map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnMapClickListener {

    Button confirm_location;
    private GoogleMap mMap;
    private Marker marker_in_sydney;
    private TextView name, addressTxt,pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        name = findViewById(R.id.name);
        addressTxt = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        confirm_location = findViewById(R.id.confirm_location);

        confirm_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("data", (Serializable) new LocalPlace(addressTxt.getText().toString(),name.getText().toString(),
                        marker_in_sydney.getPosition(),pincode.getText().toString()));
                setResult(2,intent);
                finish();//finishing activity
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng sydney = AppConfig.houseLatlon;
        marker_in_sydney = mMap.addMarker(new MarkerOptions().position(sydney).title("Select Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));
        mMap.setOnCameraIdleListener(this);
        mMap.setOnMapClickListener(this);
    }


    @Override
    public void onCameraIdle() {
        LatLng midLatLng = mMap.getCameraPosition().target;

        getaddress(midLatLng);
    }

    private void getaddress(LatLng midLatLng) {
        try {
            marker_in_sydney.setPosition(midLatLng);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(MapsActivity.this,
                    Locale.getDefault());
            addresses = geocoder.getFromLocation(midLatLng.latitude, midLatLng.longitude, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            name.setText(address.split(",")[0]);
            addressTxt.setText(address);
            pincode.setText(postalCode);

        } catch (Exception e) {
            Log.e("xxxxxx", e.toString());
        }
    }


    @Override
    public void onMapClick(@NonNull @NotNull LatLng midLatLng) {
        getaddress(midLatLng);
    }
}