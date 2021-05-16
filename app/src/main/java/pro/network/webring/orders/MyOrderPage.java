package pro.network.webring.orders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pro.network.webring.R;
import pro.network.webring.app.AppController;
import pro.network.webring.app.BaseActivity;
import pro.network.webring.product.ProductListBean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pro.network.webring.app.AppConfig;

import static pro.network.webring.app.AppConfig.ORDER_CHANGE_STATUS;

public class MyOrderPage extends BaseActivity implements ReturnOnClick {
    private static final int FINE_LOCATION_CODE = 199;
    ProgressDialog pDialog;

    RecyclerView myorders_list;
    private ArrayList<MyorderBean> myorderBeans = new ArrayList<>();
    MyOrderListAdapter myOrderListAdapter;
    SharedPreferences sharedpreferences;
    NestedScrollView scroll;
    LinearLayout empty_product;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_myorder);
        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setCancelable(false);
        sharedpreferences = getApplicationContext().getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);

        myorders_list = findViewById(R.id.myorders_list);
        myOrderListAdapter = new MyOrderListAdapter(getApplicationContext(), myorderBeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        myorders_list.setLayoutManager(addManager1);
        myorders_list.setAdapter(myOrderListAdapter);
        scroll = findViewById(R.id.scroll);
        empty_product = findViewById(R.id.empty_product);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setTitle("My Orders");

        getAllOrder();
    }

    private void getAllOrder() {
        String tag_string_req = "req_register";
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ORDER_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        myorderBeans = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MyorderBean order = new MyorderBean();
                            order.setId(jsonObject.getString("id"));
                            order.setAmount(jsonObject.getString("price"));
                            order.setQuantity(jsonObject.getString("quantity"));
                            order.setStatus(jsonObject.getString("status"));
                            order.setitems(jsonObject.getString("items"));
                            order.setReson(jsonObject.getString("reason"));
                            order.setCreatedon(jsonObject.getString("createdon"));
                            ObjectMapper mapper = new ObjectMapper();
                            Object listBeans = new Gson().fromJson(jsonObject.getString("items"),
                                    Object.class);
                            ArrayList<ProductListBean> accountList = mapper.convertValue(
                                    listBeans,
                                    new TypeReference<ArrayList<ProductListBean>>() {
                                    }
                            );
                            order.setProductBeans(accountList);
                            myorderBeans.add(order);

                        }
                        myOrderListAdapter.notifyData(myorderBeans);
                        getSupportActionBar().setSubtitle("Orders - " + myorderBeans.size());

                        if (myorderBeans.size() == 0) {
                            empty_product.setVisibility(View.VISIBLE);
                            scroll.setVisibility(View.GONE);
                        } else {
                            empty_product.setVisibility(View.GONE);
                            scroll.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("user", sharedpreferences.getString(AppConfig.user_id, ""));
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
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
    public void onReturnClick(final String id) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(MyOrderPage.this, R.style.RoundShapeTheme);
        LayoutInflater inflater = MyOrderPage.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        TextView title = dialogView.findViewById(R.id.title);
        final TextInputEditText address = dialogView.findViewById(R.id.address);
        address.setHint("Reason for return");

        final TextInputLayout addressText = dialogView.findViewById(R.id.addressText);
        addressText.setHint("Reason for return");

        title.setText("* Do you want to return this order? If yes Order will be returned and Spirtual admin will contact you shortly.");
        dialogBuilder.setTitle("Alert")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (address.getText().toString().length() > 0) {
                            statusChange(id, "Returned", address.getText().toString());
                            dialog.cancel();
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter valid Reason", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();
    }

    private void statusChange(final String id, final String status, final String reason) {
        String tag_string_req = "req_register";
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ORDER_CHANGE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        getAllOrder();
                    } else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    hideDialog();
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("id", id);
                localHashMap.put("status", status);
                localHashMap.put("reason", reason);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
