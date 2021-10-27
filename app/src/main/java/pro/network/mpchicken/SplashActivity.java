package pro.network.mpchicken;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import pro.network.mpchicken.app.AppConfig;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(AppConfig.mypreference,
                Context.MODE_PRIVATE);

        Log.d("TOken ", "" + FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");


        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                } finally {
                    if (!(sharedpreferences.contains(AppConfig.isLogin)
                            && sharedpreferences.getBoolean(AppConfig.isLogin, false))) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(AppConfig.isLogin, true);
                        editor.putString(AppConfig.configKey, "guest");
                        editor.putString(AppConfig.usernameKey, "guest");
                        editor.putString(AppConfig.auth_key, "guest");
                        editor.putString(AppConfig.user_id, "guest");
                        editor.commit();
                    }
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }
        };
        logoTimer.start();

    }
}
