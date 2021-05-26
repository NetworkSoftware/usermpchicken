package pro.network.jsbroilers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pro.network.jsbroilers.app.AppConfig;
import pro.network.jsbroilers.app.AppController;
import pro.network.jsbroilers.app.BaseActivity;
import pro.network.jsbroilers.app.DatabaseHelperYalu;
import pro.network.jsbroilers.cart.CartActivity;
import pro.network.jsbroilers.product.ProductListBean;

import static pro.network.jsbroilers.app.AppConfig.REGISTER_USER;
import static pro.network.jsbroilers.app.AppConfig.mypreference;
import static pro.network.jsbroilers.app.AppConfig.phone;
import static pro.network.jsbroilers.app.AppConfig.user_id;
import static pro.network.jsbroilers.app.AppConfig.usernameKey;

public class SettingsActivity extends BaseActivity {
    final int UPI_PAYMENT = 0;
    TextView userNameHeader, phoneNameHeader;
    Button login, logout;
    LinearLayout loginLayout;
    DatabaseHelperYalu db;
    private SharedPreferences sharedpreferences;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_settings);
        userNameHeader = findViewById(R.id.userNameHeader);
        phoneNameHeader = findViewById(R.id.userPhoneHeader);
        login = findViewById(R.id.login);
        logout = findViewById(R.id.logout);
        loginLayout = findViewById(R.id.loginLayout);
        db = new DatabaseHelperYalu(SettingsActivity.this);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.logout(sharedpreferences, SettingsActivity.this);
                changeHeaderContent();
            }
        });
        ((LinearLayout) findViewById(R.id.contactus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 919790294942"));
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
        sharedpreferences = SettingsActivity.this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        findViewById(R.id.changePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePassword.class));
            }
        });

        pDialog = new ProgressDialog(SettingsActivity.this);
        pDialog.setCancelable(false);

        changeHeaderContent();
        return;
    }


    private void showBottomDialog() {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(SettingsActivity.this);
        LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_sheet_layout, null);


        final TextInputEditText username = dialogView.findViewById(R.id.username);
        final TextInputEditText phoneNumber = dialogView.findViewById(R.id.phoneNumber);
        final TextInputEditText password = dialogView.findViewById(R.id.password);

        TextInputLayout phoneNumberTxt = dialogView.findViewById(R.id.phoneNumberTxt);
        final TextInputLayout usernameTxt = dialogView.findViewById(R.id.usernameTxt);
        TextInputLayout passwordText = dialogView.findViewById(R.id.passwordText);


        final TextView accountTxt = dialogView.findViewById(R.id.accountTxt);
        final Button login = dialogView.findViewById(R.id.login);
        final Button request = dialogView.findViewById(R.id.request);

        TextView forgotPassword = dialogView.findViewById(R.id.forgotPassword);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountTxt.getText().toString().equalsIgnoreCase("No account? ")) {
                    accountTxt.setText("Have Account? ");
                    request.setText("Login");
                    login.setText("Sign up");
                    usernameTxt.setVisibility(View.VISIBLE);

                } else {
                    accountTxt.setText("No Account? ");
                    login.setText("Login");
                    request.setText("Sign up");
                    usernameTxt.setVisibility(View.GONE);


                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.hide();
                Intent intent = new Intent(new SettingsActivity(), ChangePassword.class);
                startActivity(intent);
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
                            username.getText().toString(), password.getText().toString(), mBottomSheetDialog);
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
    private void checkLogin(final String username, final String password, final RoundedBottomSheetDialog mBottomSheetDialog) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Login ...");
        showDialog();
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
                        editor.putString(AppConfig.phone, jObj.getString("phone"));
                        editor.putString(AppConfig.configKey, username);
                        editor.putString(AppConfig.usernameKey, name);
                        editor.putString(AppConfig.auth_key, auth_key);
                        editor.putString(AppConfig.user_id, user_id);
                        editor.commit();
                        mBottomSheetDialog.dismiss();
                        changeHeaderContent();
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

    private void registerUser(final String phoneNumber, final String username, final String password, final RoundedBottomSheetDialog mBottomSheetDialog) {
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

                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }


    private void changeHeaderContent() {
        if (sharedpreferences.getString(user_id, "guest").equals("guest")) {
            login.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            userNameHeader.setText("");
            phoneNameHeader.setText("");
        } else {
            login.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            userNameHeader.setText(sharedpreferences.getString(usernameKey, ""));
            phoneNameHeader.setText(sharedpreferences.getString(phone, ""));
         }

    }
}
