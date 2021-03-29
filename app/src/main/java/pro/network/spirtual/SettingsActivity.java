package pro.network.spirtual;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.network.spirtual.app.AppConfig;
import pro.network.spirtual.app.BaseActivity;

public class SettingsActivity extends BaseActivity {
    final int UPI_PAYMENT = 0;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_settings);

        ((LinearLayout) findViewById(R.id.changePassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePassword.class));
            }
        });

        ((LinearLayout) findViewById(R.id.contactus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 91 90258 65487"));
                startActivity(intent);
            }
        });

        ((TextView) findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(AppConfig.isLogin, true);
                    editor.putString(AppConfig.configKey, "guest");
                    editor.putString(AppConfig.usernameKey, "guest");
                    editor.putString(AppConfig.auth_key, "guest");
                    editor.putString(AppConfig.user_id, "guest");
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                    finishAffinity();
                }

        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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






}
