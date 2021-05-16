package pro.network.webring.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pro.network.webring.HomeActivity;

import static pro.network.webring.app.AppConfig.mypreference;

public abstract class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedpreferences;
    protected ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        startDemo();
    }

    protected void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void setActionBar(int theme) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(theme);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(theme));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }
    protected void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(AppConfig.isLogin, true);
        editor.putString(AppConfig.configKey, "guest");
        editor.putString(AppConfig.usernameKey, "guest");
        editor.putString(AppConfig.auth_key, "guest");
        editor.putString(AppConfig.user_id, "guest");
        editor.commit();
        startActivity(new Intent(BaseActivity.this, HomeActivity.class));
        finishAffinity();
    }
    protected abstract void startDemo();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialog();
    }

    protected void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hideDialog() {
        if (pDialog!=null && pDialog.isShowing())
            pDialog.dismiss();
    }
}