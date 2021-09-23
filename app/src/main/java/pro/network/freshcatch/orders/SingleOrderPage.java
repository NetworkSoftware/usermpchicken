package pro.network.freshcatch.orders;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import pro.network.freshcatch.HomeActivity;
import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.AppController;
import pro.network.freshcatch.app.HeaderFooterPageEvent;
import pro.network.freshcatch.app.PdfConfig;
import pro.network.freshcatch.product.ProductListBean;

import static pro.network.freshcatch.app.AppConfig.FETCH_ADDRESS;
import static pro.network.freshcatch.app.AppConfig.decimalFormat;


public class SingleOrderPage extends AppCompatActivity implements ReturnOnClick, OnMapReadyCallback {

    RecyclerView myorders_list;
    MyOrderListProAdapter myOrderListAdapter;
    SharedPreferences sharedpreferences;
    TextView address;
    TextView delivery;
    TextView payment;
    TextView grandtotal;
    TextView shippingTotal;
    TextView subtotal, status, paymentId, deliveryTime, comments, walletTotal;
    ProgressDialog pDialog;
    LinkedHashMap<String, String> stringStringMap = new LinkedHashMap<>();
    private ArrayList<ProductListBean> myorderBeans = new ArrayList<>();

    private MaterialButton shopMore, invoice;
    private MyorderBean myorderBean;
    private GoogleMap mMap;
    private Marker marker_in_sydney;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        sharedpreferences = getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setTitle("My Orders");

        myorders_list = findViewById(R.id.myorders_list);
        myOrderListAdapter = new MyOrderListProAdapter(getApplicationContext(), myorderBeans);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        myorders_list.setLayoutManager(addManager1);
        myorders_list.setAdapter(myOrderListAdapter);
        invoice = findViewById(R.id.invoice);

        address = findViewById(R.id.address);
        delivery = findViewById(R.id.delivery);
        payment = findViewById(R.id.payment);
        grandtotal = findViewById(R.id.grandtotal);
        shippingTotal = findViewById(R.id.shippingTotal);
        subtotal = findViewById(R.id.subtotal);
        status = findViewById(R.id.status);
        shopMore = findViewById(R.id.shopMore);
        paymentId = findViewById(R.id.paymentId);
        comments = findViewById(R.id.comments);
        deliveryTime = findViewById(R.id.deliveryTime);
        walletTotal = findViewById(R.id.walletTotal);

        shopMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleOrderPage.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SingleOrderPage.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SingleOrderPage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    printFunction();
                }
            }
        });
        getValuesFromIntent();
    }

    private void getValuesFromIntent() {
        try {
            showShopMore();
            myorderBean = (MyorderBean) getIntent().getSerializableExtra("data");
            address.setText(myorderBean.address);
            stringStringMap.put("Order Id", "SCF" + myorderBean.getId());
            stringStringMap.put("Address", myorderBean.address);
            status.setText(myorderBean.status);
            stringStringMap.put("Status", myorderBean.status);
            delivery.setText(myorderBean.delivery);
            stringStringMap.put("Delivery", myorderBean.delivery);
            payment.setText(myorderBean.payment);
            stringStringMap.put("Payment mode", myorderBean.payment);
            paymentId.setText(myorderBean.paymentId);
            stringStringMap.put("Payment ID", myorderBean.paymentId);
            comments.setText(myorderBean.comments);
            stringStringMap.put("Comments", myorderBean.comments);
            deliveryTime.setText(myorderBean.deliveryTime);
            walletTotal.setText(myorderBean.walletAmount);
            stringStringMap.put("Wallet Cost", myorderBean.walletAmount);
            stringStringMap.put("Delivery time", myorderBean.deliveryTime);
            if (myorderBean.getDeliveryTime().equalsIgnoreCase("NA")) {
                deliveryTime.setVisibility(View.GONE);
            } else {
                deliveryTime.setVisibility(View.VISIBLE);
            }
            grandtotal.setText(myorderBean.grandCost);
            stringStringMap.put("Date", myorderBean.createdOn);
            myorderBeans = myorderBean.productBeans;
            for (int i = 0; i < myorderBeans.size(); i++) {
                ProductListBean productListBean = myorderBeans.get(i);
                String qty = productListBean.qty;
                try {
                    if (qty == null || !qty.matches("-?\\d+(\\.\\d+)?")) {
                        qty = "1";
                    }
                    float startValue = Float.parseFloat(productListBean.getPrice()) * Float.parseFloat(qty);
                    String s = productListBean.getQty() + "*" + productListBean.getPrice() + "/" +
                            productListBean.getQty() + " " + productListBean.getPrice() +
                            "=" + "₹" + decimalFormat.format(startValue) + ".00";
                    stringStringMap.put(productListBean.getCategory() + " " + productListBean.getBrand(),
                            s);
                } catch (Exception e) {

                }
            }
            stringStringMap.put("Grand total", myorderBean.grandCost);
            shippingTotal.setText(myorderBean.shipCost);
            stringStringMap.put("Shipping total", myorderBean.shipCost);
            subtotal.setText(myorderBean.amount);
            stringStringMap.put("Sub total", myorderBean.amount);
            myOrderListAdapter.notifyData(myorderBeans);

            if (paymentId != null) {
                paymentId.setText(myorderBean.getPaymentId());
                if (myorderBean.getPayment().equalsIgnoreCase("NA")) {
                    paymentId.setVisibility(View.GONE);
                } else {
                    paymentId.setVisibility(View.VISIBLE);
                }
            }

            getSupportActionBar().setTitle("Order id:#" + (myorderBean.getId()));

            fetchAddressById(myorderBean.address);

        } catch (Exception e) {
            Log.e("xxxxxxxxx", e.toString());
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

  /*  private void showShopMore() {
        if (getIntent().getStringExtra("from") != null &&
                getIntent().getStringExtra("from").equalsIgnoreCase("payment")) {
            shopMore.setVisibility(View.VISIBLE);
        } else {
            shopMore.setVisibility(View.GONE);
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getValuesFromIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("from") != null &&
                getIntent().getStringExtra("from").equalsIgnoreCase("payment")) {
            shopMore.callOnClick();
        } else {
            finish();
        }
    }

    private void fetchAddressById(final String id) {
        String tag_string_req = "req_register_add";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                FETCH_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(jsonObject.getString("name")).append("\n");
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1.append(jsonObject.getString("address")).append("\n");
                        stringBuilder1.append(jsonObject.getString("mobile")).append("\n");
                        stringBuilder1.append(jsonObject.getString("alternativeMobile")).append("\n");
                        stringBuilder1.append(jsonObject.getString("landmark")).append("\n");
                        stringBuilder1.append(jsonObject.getString("pincode"));
                        address.setText(stringBuilder.toString() + "\n" + stringBuilder1.toString());
                        myorderBean.setAddressOrg(stringBuilder1.toString());
                        myorderBean.setName(jsonObject.getString("name"));
                        myorderBean.setPhone(jsonObject.getString("mobile"));

                        if(mMap!=null && marker_in_sydney!=null){
                            try {
                                String[] split = jsonObject.getString("latlon").split(",");
                                double lat = Double.parseDouble(split[0]);
                                double lon = Double.parseDouble(split[1]);
                                marker_in_sydney = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                                        .title(jsonObject.getString("name")));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
                            }catch (Exception e){

                            }
                        }
                        stringStringMap.put("Address", stringBuilder.toString());
                    }
                } catch (JSONException e) {
                    Log.e("Xxxxxxxxx", "Something went wrong");

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Xxxxxxxxx", "Something went wrong");
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("id", id);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!this.pDialog.isShowing()) this.pDialog.show();

    }

    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }


    public void printFunction() {
        try {
            String path = getExternalCacheDir().getPath() + "/PDF";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Log.d("PDFCreator", "PDF Path: " + path);
            File file = new File(dir, stringStringMap.get("Order Id") + "_" + System.currentTimeMillis() + ".pdf");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fOut = new FileOutputStream(file);


            Document document = new Document(PageSize.A4, 30, 28, 40, 119);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, fOut);

            document.open();
            PdfConfig.addMetaData(document);
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            pdfWriter.setPageEvent(event);
            PdfConfig.addContent(document, myorderBean, SingleOrderPage.this);
            document.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } else {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file), "application/pdf");
                Intent intent = Intent.createChooser(target, "Open File");
                startActivity(intent);
            }

        } catch (Error | Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        hideDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // printFunction();
            } else {
                Toast.makeText(SingleOrderPage.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onReturnClick(String id) {

    }

    @Override
    public void onCartClick(MyorderBean position) {
        //
    }

    private void showShopMore() {
        if (getIntent().getStringExtra("from") != null &&
                getIntent().getStringExtra("from").equalsIgnoreCase("payment")) {

        } else {

        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng sydney = AppConfig.houseLatlon;
        marker_in_sydney = mMap.addMarker(new MarkerOptions().position(sydney).title("Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
    }
}
