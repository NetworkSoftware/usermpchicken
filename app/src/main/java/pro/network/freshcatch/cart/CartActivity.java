package pro.network.freshcatch.cart;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.freshcatch.R;
import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.AppController;
import pro.network.freshcatch.app.DbCart;
import pro.network.freshcatch.orders.MyOrderListAdapter;
import pro.network.freshcatch.orders.MyorderBean;
import pro.network.freshcatch.payment.PaymentActivity;
import pro.network.freshcatch.product.ProductListBean;

import static pro.network.freshcatch.app.AppConfig.ORDER_CREATE;
import static pro.network.freshcatch.app.AppConfig.REGISTER_USER;
import static pro.network.freshcatch.app.AppConfig.decimalFormat;
import static pro.network.freshcatch.app.AppConfig.mypreference;
import static pro.network.freshcatch.app.AppConfig.user_id;

public class CartActivity extends AppCompatActivity implements CartItemClick {

    RecyclerView cart_list;
    CartListAdapter cartListAdapter;
    ProgressDialog pDialog;
    Button order;
    OnCartItemChange onCartItemChange;
    NestedScrollView nestedScrollView;
    CardView continueCard;
    LinearLayout empty_product;
    SharedPreferences sharedpreferences;
    private List<ProductListBean> productList = new ArrayList<>();
    private DbCart db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new DbCart(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        cart_list = findViewById(R.id.cart_list);
        cartListAdapter = new CartListAdapter(this, productList, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cart_list.setLayoutManager(addManager1);
        cart_list.setAdapter(cartListAdapter);

        order = findViewById(R.id.order);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedpreferences.getString(user_id, "guest").equals("guest")) {
                    showBottomDialog();
                } else {
                    getToPayment();
                }
            }
        });


        nestedScrollView = findViewById(R.id.nested);
        continueCard = findViewById(R.id.continueCard);
        empty_product = findViewById(R.id.empty_product);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAllCart();
    }


    private void showBottomDialog() {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(CartActivity.this);
        LayoutInflater inflater = CartActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_sheet_layout, null);


        final TextInputEditText username = dialogView.findViewById(R.id.username);
        final TextInputEditText phoneNumber = dialogView.findViewById(R.id.phoneNumber);
        final TextInputEditText password = dialogView.findViewById(R.id.password);

        TextInputLayout phoneNumberTxt = dialogView.findViewById(R.id.phoneNumberTxt);
        final TextInputLayout usernameTxt = dialogView.findViewById(R.id.usernameTxt);
        TextInputLayout passwordText = dialogView.findViewById(R.id.passwordText);
        final TextInputLayout emailTxt = dialogView.findViewById(R.id.emailTxt);
        final TextInputEditText email = dialogView.findViewById(R.id.email);

        final TextView accountTxt = dialogView.findViewById(R.id.accountTxt);
        final Button login = dialogView.findViewById(R.id.login);
        final Button request = dialogView.findViewById(R.id.request);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountTxt.getText().toString().equalsIgnoreCase("No account? ")) {
                    accountTxt.setText("Have Account? ");
                    request.setText("Login");
                    login.setText("Sign up");
                    usernameTxt.setVisibility(View.VISIBLE);
                    emailTxt.setVisibility(View.VISIBLE);
                } else {
                    accountTxt.setText("No Account? ");
                    login.setText("Login");
                    request.setText("Sign up");
                    usernameTxt.setVisibility(View.GONE);
                    emailTxt.setVisibility(View.GONE);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountTxt.getText().toString().equalsIgnoreCase("Have Account? ") &&
                        username.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid name", Toast.LENGTH_LONG).show();
                    return;
                } else if (phoneNumber.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Phone number", Toast.LENGTH_LONG).show();
                    return;
                } else if (password.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (accountTxt.getText().toString().equalsIgnoreCase("No account? ")) {
                    checkLogin(phoneNumber.getText().toString(),
                            password.getText().toString(), mBottomSheetDialog);
                } else {
                    registerUser(phoneNumber.getText().toString(),
                            username.getText().toString(), password.getText().toString(), email.getText().toString(),mBottomSheetDialog);
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

    private void getAllCart() {
        productList.clear();
        productList = db.getAllProductsInCart(sharedpreferences.getString(user_id, ""));
        float grandTotal = 0f;
        for (int i = 0; i < productList.size(); i++) {
            String qty = productList.get(i).qty;
            try {
                if (qty == null || !qty.matches("-?\\d+(\\.\\d+)?")) {
                    qty = "1";
                }
            } catch (Exception e) {

            }
            float startValue = Float.parseFloat(productList.get(i).price) * Integer.parseInt(qty);


            grandTotal = grandTotal + startValue;
        }
        ((TextView) findViewById(R.id.subtotal)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
        ((TextView) findViewById(R.id.grandtotal)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
        ((TextView) findViewById(R.id.total)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
        cart_list.setAdapter(cartListAdapter);
        cartListAdapter.notifyData(productList);
        if (productList.size() == 0) {
            empty_product.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            continueCard.setVisibility(View.GONE);
        } else {
            empty_product.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
            continueCard.setVisibility(View.VISIBLE);
        }

    }

    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }

    @Override
    public void OnQuantityChange(int position, int qty) {

        productList.get(position).setQty(qty + "");
        db.updateProductsInCart(productList.get(position), sharedpreferences.getString(user_id, ""));
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }

        getAllCart();
    }

    @Override
    public void ondeleteClick(int position) {
        db.deleteProductById(productList.get(position), sharedpreferences.getString(user_id, ""));
        productList.remove(position);
        cartListAdapter.notifyData(position);
        Toast.makeText(getApplication(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
        getAllCart();
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
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

    private void checkLogin(final String username, final String password, final RoundedBottomSheetDialog mBottomSheetDialog) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Login ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.LOGIN_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    String msg = jObj.getString("message");
                    if (success == 1) {
                        String auth_key = jObj.getString("auth_key");
                        String user_id = jObj.getString("user_id");
                        String name = jObj.getString("name");

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(AppConfig.isLogin, true);
                        editor.putString(AppConfig.configKey, username);
                        editor.putString(AppConfig.phone, jObj.getString("phone"));
                        editor.putString(AppConfig.usernameKey, name);
                        editor.putString(AppConfig.auth_key, auth_key);
                        editor.putString(AppConfig.user_id, user_id);
                        editor.putString(AppConfig.emailKey, jObj.getString("email"));
                        editor.commit();

                        ArrayList<ProductListBean> productListTemp = db.getAllProductsInCart("guest");
                        for (int k = 0; k < productListTemp.size(); k++) {
                            db.insertProductInCart(productListTemp.get(k), user_id);
                        }
                        db.deleteAllInCart("guest");

                        mBottomSheetDialog.dismiss();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getToPayment();
                            }
                        }, 500);
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
                hideDialog();

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
                localHashMap.put("phone", username);
                localHashMap.put("password", password);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void registerUser(final String phoneNumber, final String username, final String password, final String email, final RoundedBottomSheetDialog mBottomSheetDialog) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String msg = jsonObject.getString("message");
                    if (success == 1) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(AppConfig.configKey, phoneNumber);
                        editor.commit();
                        checkLogin(username, password, mBottomSheetDialog);
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("name", username);
                localHashMap.put("phone", phoneNumber);
                localHashMap.put("password", password);
                localHashMap.put("email", email);

                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public interface OnCartItemChange {
        void onCartChange();
    }
    private void getToPayment() {
        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
        intent.putExtra("total", ((TextView) findViewById(R.id.grandtotal)).getText().toString());
        startActivity(intent);
    }

}