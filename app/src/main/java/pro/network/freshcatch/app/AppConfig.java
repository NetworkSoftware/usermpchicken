package pro.network.freshcatch.app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pro.network.freshcatch.HomeActivity;

public class  AppConfig {

    public static DecimalFormat decimalFormat = new DecimalFormat("0");
    public static final String isLogin = "isLoginKey";
    public static final String userId = "userId";
    public static final String address = "address";
    public static final String phone = "phone";

    public static final String mypreference = "mypref";


    //YALU Mobiles_NetworkSoftware
   // public static final String ip = "http://thestockbazaar.com/admin/e-commerce/freshcatch";

    public static final String ip = "http://192.168.1.100:8103/prisma/freshcatch";

    public static final String configKey = "configKey";
    public static final String usernameKey = "usernameKey";
    public static final String user_id = "user_id";
    public static final String IMAGE_URL = ip + "/images/";
    public static final String auth_key = "auth_key";
    public static final String emailKey = "emailKey";
    //login and Register
    public static final String REGISTER_USER = ip + "/user_register.php";
    public static final String LOGIN_USER = ip + "/user_login.php";
    public static final String CHANGE_PASSWORD = ip + "/change_password.php";
    public static final String CATEGORIES_GET_ALL = ip + "/get_all_category.php";
    public static final String GET_ALL_CATEGORIES = ip + "/get_all_category.php";

    //Mail
    public static final String UPDATE_EMAIL = ip + "/update_email.php";

    //Product
    public static final String PRODUCT_GET_ALL = ip + "/dataFetchAll.php";
    //Banner
    public static final String BANNERS_GET_ALL = ip + "/dataFetchAll_banner.php";
    public static final String GET_PINCODE = ip + "/getpincode";
    //Wallet
    public static final String WALLET_GET_ALL = ip + "/get_all_wallet.php";
    public static final String GET_USER_WALLET = ip + "/get_user_wallet.php";


    //Order
    public static final String ORDER_GET_ALL = ip + "/dataFetchAll_order.php";
    public static final String ORDER_CREATE = ip + "/create_order.php";
    public static final String ORDER_CHANGE_STATUS = ip + "/order_change_status.php";
    //address
    public static final String ADD_ADDRESS = ip + "/create_address.php";
    public static final String UPDATE_ADDRESS = ip + "/update_address.php";
    public static final String GET_ALL_ADDRESS = ip + "/get_all_address.php";
    public static final String DELETE_ADDRESS = ip + "/delete_address.php";
    public static final String FETCH_ADDRESS = ip + "/fetch_address.php";

    public static void openPdfFile(Context context, String name) {
        File fileBrochure = new File(Environment.getExternalStorageDirectory() + "/" + name);
        if (!fileBrochure.exists()) {
            CopyAssetsbrochure(context, name);
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/" + name);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "NO Pdf Viewer", Toast.LENGTH_SHORT).show();
        }
    }
    public static void logout(SharedPreferences sharedPreferences, Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConfig.isLogin, true);
        editor.putString(AppConfig.configKey, "guest");
        editor.putString(AppConfig.usernameKey, "guest");
        editor.putString(AppConfig.auth_key, "guest");
        editor.putString(AppConfig.user_id, "guest");
        editor.commit();
        context.startActivity(new Intent(context, HomeActivity.class));
    }
    public static DefaultRetryPolicy getTimeOut() {
        return new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
    public static void sendSMS(String phoneNo, String msg, Context context) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        }catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private static void CopyAssetsbrochure(Context context, String name) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for (int i = 0; i < files.length; i++) {
            String fStr = files[i];
            if (fStr.equalsIgnoreCase(name)) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(files[i]);
                    out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + files[i]);
                    copyFileNew(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    break;
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
            }
        }
    }

    private static void copyFileNew(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static boolean isValidGSTNo(String str) {
        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
                + "[A-Z]{1}[1-9A-Z]{1}"
                + "Z[0-9A-Z]{1}$";

        Pattern p = Pattern.compile(regex);

        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);

        return m.matches();
    }

    public static String getResizedImage(String path, boolean isResized) {
        if (isResized) {
            return IMAGE_URL + "small/" + path.substring(path.lastIndexOf("/") + 1);
        }
        return path;
    }


    public static String convertTimeToLocal(String time) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(time);
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        } catch (Exception e) {
            return time;
        }
    }

    public static boolean checkReturnExpired(String time) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar c = Calendar.getInstance();
            c.setTime(df.parse(time));
            c.add(Calendar.DATE, 3);

            Calendar cNow = Calendar.getInstance();
            return c.getTime().compareTo(cNow.getTime()) == -1;

        } catch (Exception e) {
            return true;
        }
    }

    public static DefaultRetryPolicy getPolicy() {
        return new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
    public static boolean emailValidator(String email) {
        if (email == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static String dayInMonthFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
        return sdf.format(date);
    }
    public static String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
