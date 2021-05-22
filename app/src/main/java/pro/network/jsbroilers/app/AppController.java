package pro.network.jsbroilers.app;

import android.app.Application;
import android.os.StrictMode;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    private Locale firstLaunchLocale;
    private HashSet<Locale> supportedLocales;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mInstance = this;
        init();
        super.onCreate();

    }


    private void init() {
        AutomatedSupportedLocales();
        manualSupportedLocales();


    }

    private void AutomatedSupportedLocales() {

    }

    private void manualSupportedLocales() {
        // This is the locale that you wanna your app to launch with.
        firstLaunchLocale = new Locale("en");

        // You can use a HashSet<String> instead and call 'setSupportedStringLocales()' :)
        supportedLocales = new HashSet<>();
        supportedLocales.add(new Locale("en"));
        supportedLocales.add(new Locale("ta"));
        supportedLocales.add(new Locale("ml"));
        supportedLocales.add(new Locale("kn"));
        supportedLocales.add(new Locale("te"));
        supportedLocales.add(new Locale("mr"));
        supportedLocales.add(new Locale("or"));
        supportedLocales.add(new Locale("bn"));
        supportedLocales.add(new Locale("as"));
        supportedLocales.add(new Locale("my"));
        supportedLocales.add(new Locale("th"));
        supportedLocales.add(new Locale("ms"));
        supportedLocales.add(new Locale("km"));
        supportedLocales.add(new Locale("vi"));
        supportedLocales.add(new Locale("zh"));
        supportedLocales.add(new Locale("pt"));
        supportedLocales.add(new Locale("fr"));
        supportedLocales.add(new Locale("es"));
        supportedLocales.add(new Locale("sw"));
    }

    Map<String, String> lang = new HashMap<>();

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
/* Location:           D:\Gopal\downloads\Apk decompile java\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     smart.breed.contest.app.AppController
 * JD-Core Version:    0.6.0
 */