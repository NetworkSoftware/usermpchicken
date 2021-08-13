package pro.network.freshcatch.wallet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.AppController;

import static pro.network.freshcatch.app.AppConfig.GET_LIST_WALLET;
import static pro.network.freshcatch.app.AppConfig.WALLET_GET_ALL;


public class WalletActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<WalletBean> contactList;
    private WalletAdapter mAdapter;
    ProgressDialog progressDialog;
    TextView tol_amt;
    WalletBean walletBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallet);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        tol_amt=findViewById(R.id.tol_amt);
        tol_amt.setText(walletBean.getAmount());
        // toolbar fancy stuff
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setTitle(getIntent().getStringExtra("name") + " Stock");

        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new WalletAdapter(getApplication(), contactList,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(addManager1);
        recyclerView.setAdapter(mAdapter);
    }

    private void fetchContacts() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_LIST_WALLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        contactList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            WalletBean product = new WalletBean();
                            product.setId(jsonObject.getString("id"));
                            product.setCreatedon(jsonObject.getString("date"));
                            product.setOperation(jsonObject.getString("operation"));
                            product.setAmount(jsonObject.getString("createdon"));
                            contactList.add(product);
                        }
                     //   getSupportActionBar().setSubtitle("#" + jObj.getString("tqty"));
                        mAdapter.notifyData(contactList);
                    } else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("prodId", getIntent().getStringExtra("id"));
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchContacts();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
