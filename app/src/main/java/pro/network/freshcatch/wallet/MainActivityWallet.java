package pro.network.freshcatch.wallet;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.wisnu.datetimerangepickerandroid.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.AppController;

import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.RANGE;
import static pro.network.freshcatch.app.AppConfig.WALLET_GET_ALL;
import static pro.network.freshcatch.app.AppConfig.mypreference;

public class MainActivityWallet extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = MainActivityWallet.class.getSimpleName();
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    TextInputEditText amountwallet;
    RoundedBottomSheetDialog mBottomSheetDialog;
    private RecyclerView recyclerView;
    private List<WalletBean> contactList;
    private WalletAdapter mAdapter;
    private SearchView searchView;
    private TextView selectedDate;
    private CardView selectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wallet);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wallet");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Define.MEDIA_PROVIDER = getString(R.string.image_provider);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        // toolbar fancy stuff

        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
      //  mAdapter = new WalletAdapter(this, contactList, this);

        // white background notification bar
        selectDate = findViewById(R.id.selectDate);
        selectedDate = findViewById(R.id.selectedDate);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalandarView();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(addManager1);
        recyclerView.setAdapter(mAdapter);

        //fetchContacts();

        FloatingActionButton addWallet = (FloatingActionButton) findViewById(R.id.addWallet);
        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
    }


    private void getAllWallet(final String startDate, final String endDate) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WALLET_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    int total = 0;
                    contactList = new ArrayList<>();
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            WalletBean walletBean = new WalletBean();
                            walletBean.setId(jsonObject.getString("id"));
                            walletBean.setPaymentId(jsonObject.getString("paymentId"));
                            String amount = jsonObject.getString("amount");
                            String type = jsonObject.getString("type");
                            if (type.equalsIgnoreCase("in")) {
                                total = total + Integer.parseInt(amount);
                            } else {
                                total = total - Integer.parseInt(amount);

                            }
                            walletBean.setAmount(amount);
                            walletBean.setType(type);
                            walletBean.setCreatedon(jsonObject.getString("createdon"));

                            contactList.add(walletBean);
                        }

                    }
                    else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.notifyData(contactList);
                    getSupportActionBar().setSubtitle("Balance: Rs." +total);

                }

                catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("shopId", sharedpreferences.getString(AppConfig.shopIdKey, ""));
                if (startDate != null) {
                    localHashMap.put("startDate", startDate);
                }
                if (endDate != null) {
                    localHashMap.put("endDate", endDate);
                } return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        getAllWallet(null,null);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    private void showBottomDialog() {
        mBottomSheetDialog = new RoundedBottomSheetDialog(MainActivityWallet.this);
        LayoutInflater inflater = MainActivityWallet.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_wallet_layout, null);

        amountwallet = dialogView.findViewById(R.id.amountwallet);
        final RadioButton online = dialogView.findViewById(R.id.online);
        final RadioButton gpay = dialogView.findViewById(R.id.gpay);


        final Button addwallettype = dialogView.findViewById(R.id.addwallettype);
        online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    online.setChecked(true);
                    gpay.setChecked(false);
                } else {
                    online.setChecked(false);
                }
            }
        });
        gpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gpay.setChecked(true);
                    online.setChecked(false);
                } else {
                    gpay.setChecked(false);
                }
            }
        });


        addwallettype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountwallet.getText().toString().length() <= 0) {
                    Toast.makeText(MainActivityWallet.this, "Enter valid Amount", Toast.LENGTH_SHORT).show();
                } else {
                    callGpayOrOnline(gpay.isChecked());
                }

            }
        });

        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mBottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RoundedBottomSheetDialog d = (RoundedBottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 0);
            }
        });
        mBottomSheetDialog.show();
    }

    private void addWallet(final String paymentId) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Add Walllet ...");
        showDialog();
        String addAddress = AppConfig.CREATE_WALLE;

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
                        if (mBottomSheetDialog != null) {
                            mBottomSheetDialog.cancel();
                        }
                        getAllWallet(null,null);
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
                localHashMap.put("shopId", sharedpreferences.getString(AppConfig.shopIdKey, ""));
                localHashMap.put("type", "in");
                if (amountwallet != null) {
                    localHashMap.put("amount", amountwallet.getText().toString());
                }
                localHashMap.put("paymentId", paymentId);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void callGpayOrOnline(boolean isGpay) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_fZYbCrkPhxDhyk");
        checkout.setImage(R.drawable.fresh_icon);
        JSONObject object = new JSONObject();
        try {

            object.put("name", getString(R.string.app_name));
            object.put("description", "Reference No. #" +
                    sharedpreferences.getString(AppConfig.shopIdKey, "") + "_" + System.currentTimeMillis());

            JSONObject theme = new JSONObject();
            object.put("color", "#3399cc");
            object.put("theme", theme);

            object.put("currency", "INR");
            object.put("send_sms_hash", false);
//            object.put("amount", Float.parseFloat(amountwallet.getText().toString().replace(
//                    "₹", "").replace(".","")));//pass amount in currency subunits
            object.put("amount", "100");//pass amount in currency subunits

            String string1 = "dummy@email.com";
            JSONObject prefill = new JSONObject();
            prefill.put("email", string1);
            String string = sharedpreferences.getString(AppConfig.usernameKeyclient, "");
            prefill.put("contact", string);

            if (isGpay) {
                prefill.put("method", "upi");
            }

            object.put("prefill", prefill);

            JSONObject readonly = new JSONObject();
            readonly.put("email", prefill);
            readonly.put("contact", prefill);
            object.put("readonly", readonly);

            checkout.open(MainActivityWallet.this, object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        addWallet(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();

    }


    private void showCalandarView() {
        mBottomSheetDialog = new RoundedBottomSheetDialog(MainActivityWallet.this);
        LayoutInflater inflater = MainActivityWallet.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_calandar, null);

        MaterialButton submit = dialogView.findViewById(R.id.submit);
        MaterialButton clear = dialogView.findViewById(R.id.clear);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Calendar preYear = Calendar.getInstance();
        preYear.add(Calendar.YEAR, -1);

        final CalendarPickerView calendar = dialogView.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(preYear.getTime(), nextYear.getTime())
                .inMode(RANGE)
                .withSelectedDate(today);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.cancel();
                }
                if (calendar.getSelectedDates().size() > 1) {
                    changeAsPerDate(true, calendar.getSelectedDates());
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.cancel();
                }
                changeAsPerDate(false, null);
            }
        });


        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mBottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BottomSheetDialog d = (BottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 0);
            }
        });
        mBottomSheetDialog.show();
    }

    private void changeAsPerDate(boolean isManual, List<Date> selectedDates) {
        if (selectedDates != null) {
            selectedDate.setText(AppConfig.dayInMonthFormat(selectedDates.get(0))
                    + " - " + AppConfig.dayInMonthFormat(selectedDates.get(selectedDates.size() - 1)));
            getAllWallet(AppConfig.getDate(selectedDates.get(0)),
                    AppConfig.getDate(selectedDates.get(selectedDates.size() - 1)));
        }else{
            getAllWallet(null,null);
        }
    }
}
