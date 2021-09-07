package pro.network.freshcatch.payment;

import static pro.network.freshcatch.app.AppConfig.mypreference;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.AppController;

public class AddAddressActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    Address addressOld;
    boolean isUpdate;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    GoogleMap mMap;
    private Marker marker_in_sydney;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_address_layout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        final TextInputEditText name = findViewById(R.id.name);
        final TextInputEditText address = findViewById(R.id.address);
        final TextInputEditText mobile = findViewById(R.id.mobile);
        final TextInputEditText alternateMobile = findViewById(R.id.alternateMobile);
        final TextInputEditText landmark = findViewById(R.id.landmark);
        final TextInputEditText pincode = findViewById(R.id.pincode);
        final TextInputLayout pincodeTxt = findViewById(R.id.pincodeTxt);
        final TextInputEditText comments = findViewById(R.id.comments);
        final ProgressBar pincodeProgress = findViewById(R.id.pincodeProgress);
        final Button addAddress = findViewById(R.id.addAddress);

        try {
            addressOld = (Address) getIntent().getSerializableExtra("data");
            isUpdate = getIntent().getBooleanExtra("isUpdate", false);

            if (isUpdate) {
                name.setText(addressOld.name);
                address.setText(addressOld.address);
                mobile.setText(addressOld.mobile);
                alternateMobile.setText(addressOld.alternateMobile);
                landmark.setText(addressOld.landmark);
                pincode.setText(addressOld.pincode);
                comments.setText(addressOld.comments);
                addAddress.setText("Update");
            }
        } catch (Exception e) {
            Log.e("xxxxxxxxx", e.toString());
        }
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length() > 0 &&
                        address.getText().toString().length() > 0 &&
                        mobile.getText().toString().length() > 0 &&
                        alternateMobile.getText().toString().length() > 0 &&
                        landmark.getText().toString().length() > 0 &&
                        pincode.getText().toString().length() > 0) {

                    Address address1 = new Address(
                            name.getText().toString(),
                            address.getText().toString(),
                            mobile.getText().toString(),
                            alternateMobile.getText().toString(),
                            landmark.getText().toString(),
                            pincode.getText().toString(),
                            comments.getText().toString()
                    );
                    if (isUpdate) {
                        address1.setId(addressOld.id);
                    }
                    validatePincode(pincodeProgress, pincode, pincodeTxt, address1, isUpdate);
                }
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        if (ContextCompat.checkSelfPermission(AddAddressActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddAddressActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }
    }

    private void validatePincode(final ProgressBar progressBar,
                                 final TextInputEditText addressTxt,
                                 final TextInputLayout addressText,

                                 final Address address1, final boolean isUpdate) {

        String tag_string_req = "req_register";
        progressBar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.GET_PINCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        String pincode = jObj.getString("pincode");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        boolean isValidPincode = false;
                        if (pincode.length() > 0) {
                            editor.putString("pincode", pincode);
                            isValidPincode = true;
                        }
                        if (isValidPincode) {
                            editor.putString("pincode", addressTxt.getText().toString());
                            addressText.setError(null);
                            editor.commit();

                            if (isUpdate) {
                                try {
                                    String[] split = addressOld.getLatlon().split(",");
                                    double lat = Double.parseDouble(split[0]);
                                    double lon = Double.parseDouble(split[1]);
                                    if (lat != mLastLocation.getLatitude() || lon != mLastLocation.getLongitude()) {
                                        AlertDialog diaBox = AskOption(address1, isUpdate);
                                        diaBox.show();
                                    }
                                } catch (Exception e) {
                                }
                                return;
                            }
                            updateAddress(address1, isUpdate);
                        } else {
                            addressText.setError(getString(R.string.pincodeError));
                        }
                        editor.commit();
                    } else {
                        addressText.setError(getString(R.string.pincodeError));
                    }
                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("pincode", addressTxt.getText().toString());
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void updateAddress(Address address1, boolean isUpdate) {
        address1.setLatlon(mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
        addOrUpdateAddress(address1, isUpdate);
    }

    private void addOrUpdateAddress(final Address address1, final boolean isUpdate) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Updating Address ...");
        showDialog();
        String addAddress = AppConfig.ADD_ADDRESS;
        if (isUpdate) {
            addAddress = AppConfig.UPDATE_ADDRESS;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, addAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String msg = jObj.getString("message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    if (success) {
                        Intent intent = new Intent();
                        intent.putExtra("success", true);
                        setResult(2, intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "Slow network found.Try again later", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                if (isUpdate) {
                    localHashMap.put("id", address1.id);
                }
                localHashMap.put("userId", sharedpreferences.getString(AppConfig.user_id, ""));
                localHashMap.put("name", address1.name);
                localHashMap.put("address", address1.address);
                localHashMap.put("pincode", address1.pincode);
                localHashMap.put("mobile", address1.mobile);
                localHashMap.put("alternativeMobile", address1.alternateMobile);
                localHashMap.put("landmark", address1.landmark);
                localHashMap.put("pincode", address1.pincode);
                localHashMap.put("comments", address1.comments);
                localHashMap.put("latlon", address1.latlon);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng sydney = AppConfig.houseLatlon;
        marker_in_sydney = mMap.addMarker(new MarkerOptions().position(sydney).title("Select Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
    }


    /*Ending the updates for the location service*/
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*Method to get the enable location settings dialog*/
    public void settingRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(AddAddressActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Location Service not Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            /*Getting the location after aquiring location service*/
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                refreshLocation();
            } else {
                /*if there is no last known location. Which means the device has no data for the loction currently.
                 * So we will get the current location.
                 * For this we'll implement Location Listener and override onLocationChanged*/
                Log.i("Current Location", "No data for location found");

                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, AddAddressActivity.this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        refreshLocation();
    }

    private void refreshLocation() {
        if (mMap != null && marker_in_sydney != null) {
            LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            marker_in_sydney.setPosition(latlng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.0f));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect();
                }
            } else {
                Toast.makeText(AddAddressActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private AlertDialog AskOption(final Address address1, final boolean isUpdate) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Update")
                .setMessage("Do you want to Update location")
                .setIcon(R.drawable.ic_baseline_info_24)

                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        updateAddress(address1, isUpdate);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addOrUpdateAddress(address1, isUpdate);
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }

}
