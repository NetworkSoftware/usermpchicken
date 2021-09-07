package pro.network.freshcatch.payment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.AppController;

import static pro.network.freshcatch.app.AppConfig.mypreference;

public class AddAddressActivity extends AppCompatActivity implements OnMapReadyCallback {
    ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    Address addressOld;
    boolean isUpdate;

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
            isUpdate = getIntent().getBooleanExtra("isUpdate",false);

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
                            addOrUpdateAddress(address1, isUpdate);
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

    private void addOrUpdateAddress(final Address address1, boolean isUpdate) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Updating Address ...");
        showDialog();
        String addAddress = AppConfig.ADD_ADDRESS;

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
                localHashMap.put("userId", sharedpreferences.getString(AppConfig.user_id, ""));
                localHashMap.put("name", address1.name);
                localHashMap.put("address", address1.address);
                localHashMap.put("pincode", address1.pincode);
                localHashMap.put("mobile", address1.mobile);
                localHashMap.put("alternativeMobile", address1.alternateMobile);
                localHashMap.put("landmark", address1.landmark);
                localHashMap.put("pincode", address1.pincode);
                localHashMap.put("comments", address1.comments);
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

    }
}
