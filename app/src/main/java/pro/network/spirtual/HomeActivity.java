package pro.network.spirtual;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pro.network.spirtual.app.AppConfig;
import pro.network.spirtual.app.AppController;
import pro.network.spirtual.app.BaseActivity;
import pro.network.spirtual.app.DatabaseHelperYalu;
import pro.network.spirtual.cart.CartActivity;
import pro.network.spirtual.orders.MyOrderPage;
import pro.network.spirtual.product.BannerActivity;
import pro.network.spirtual.product.ProductActivity;
import pro.network.spirtual.product.ProductItemClick;
import pro.network.spirtual.product.ProductListAdapter;
import pro.network.spirtual.product.ProductListBean;

import com.google.android.material.button.MaterialButton;
import com.network.moeidbannerlibrary.banner.BannerBean;
import com.network.moeidbannerlibrary.banner.BannerLayout;
import com.network.moeidbannerlibrary.banner.BaseBannerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity implements ProductItemClick {


    ProgressDialog pDialog;
    private String TAG = getClass().getSimpleName();

    private DatabaseHelperYalu db;


    LinearLayout ruthrasam,ratna, yantars,onlinepujaservice,spiritualmals,godidols,siddhparad,booksandcd,chakravastu,others;

    RecyclerView recycler_product;
    private List<ProductListBean> productList = new ArrayList<>();
    ProductListAdapter productListAdapter;

    SharedPreferences sharedpreferences;
    CartActivity.OnCartItemChange onCartItemChange;
    BannerLayout banner;
    private MaterialButton viewAllBtn;
    private TextView cart_badge;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_home);
        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setCancelable(false);
        sharedpreferences = getApplicationContext().getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);

        if (!sharedpreferences.contains(AppConfig.isLogin) || !sharedpreferences.getBoolean(AppConfig.isLogin, false)) {
            /*startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();*/
        }

        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(AppConfig.userId, sharedpreferences.getString(AppConfig.user_id, "guest"));
        edit.commit();



        if (sharedpreferences.contains(AppConfig.usernameKey)) {
            getSupportActionBar().setSubtitle(sharedpreferences.getString(AppConfig.usernameKey, ""));
        }
        db = new DatabaseHelperYalu(getApplicationContext());
        recycler_product = findViewById(R.id.recycler_product);

        productList = new ArrayList<>();

        productListAdapter = new ProductListAdapter(getApplication(), productList, this, sharedpreferences);
        final GridLayoutManager addManager1 = new GridLayoutManager(getApplication(), 4);
        recycler_product.setLayoutManager(addManager1);
        recycler_product.setAdapter(productListAdapter);

        ruthrasam= findViewById(R.id.ruthrasam);
        ratna = findViewById(R.id.ratnavedicgemstones);
        yantars = findViewById(R.id.yantrasandmahayantras);
        onlinepujaservice = findViewById(R.id.onlinepujaservices);
        spiritualmals = findViewById(R.id.spiritualjewellery);
        godidols = findViewById(R.id.godidols);
        siddhparad = findViewById(R.id.siddhparad);
        booksandcd= findViewById(R.id.booksandcd);
        chakravastu= findViewById(R.id.chakravastu);
         others= findViewById(R.id.others);


        ruthrasam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        ratna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        yantars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        onlinepujaservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        siddhparad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        spiritualmals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        godidols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        siddhparad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        booksandcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        chakravastu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });


        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MyOrderPage.class));
            }
        });

        banner = findViewById(R.id.Banner);

        viewAllBtn = findViewById(R.id.viewAllBtn);
        fetchBanner();

        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AllProductActivity.class);
                startActivity(intent);
            }
        });



    }

    private void startAllProduct(View view) {
        Intent intent = new Intent(HomeActivity.this, AllProductActivity.class);
        intent.putExtra("type", view.getTag().toString());
        startActivity(intent);
    }


    private void fetchProductList(final String searchKey) {
        String tag_string_req = "req_register";
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PRODUCT_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        productList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ProductListBean productListBean = new ProductListBean();
                            productListBean.setId(jsonObject.getString("id"));
                            productListBean.setBrand(jsonObject.getString("brand"));
                            productListBean.setPrice(jsonObject.getString("price"));
                            productListBean.setRam(jsonObject.getString("ram"));
                            productListBean.setRom(jsonObject.getString("rom"));
                            productListBean.setModel(jsonObject.getString("model"));
                            productListBean.setImage(jsonObject.getString("image"));
                            productListBean.setDescription(jsonObject.getString("description"));
                            productListBean.setStock_update(jsonObject.getString("stock_update"));
                            productList.add(productListBean);
                            if (i == 5) {
                                break;
                            }
                        }

                        productListAdapter.notifyData(productList);
                        viewAllBtn.setText("View  Products");
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
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("searchKey", searchKey);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void fetchBanner() {
        String tag_string_req = "req_register";
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.BANNERS_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        List<BannerBean> bannerBeans = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            BannerBean bannerBean = new BannerBean();
                            bannerBean.setImages(jsonObject.getString("banner"));
                            bannerBean.setId(jsonObject.getString("id"));
                            bannerBean.setDescription(jsonObject.getString("description"));
                            bannerBeans.add(bannerBean);
                        }
                        BaseBannerAdapter webBannerAdapter = new BaseBannerAdapter(getApplicationContext(), bannerBeans);
                        webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                            @Override
                            public void onItemClick(BannerBean bannerBean) {
                                Intent intent = new Intent(HomeActivity.this, BannerActivity.class);
                                intent.putExtra("description", bannerBean.getDescription());
                                intent.putExtra("image", bannerBean.getImages());
                                startActivityForResult(intent,100);
                            }
                        });
                        banner.setAdapter(webBannerAdapter);

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
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void addtocart(ProductListBean productListBean) {
        productListBean.setQty("1");
        db.insertMainbeanyalu(productListBean, sharedpreferences.getString(AppConfig.user_id, ""));
        Toast.makeText(getApplication(), "Item added successfully", Toast.LENGTH_SHORT).show();
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }
        setupBadge();
        productListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onProductClick(ProductListBean productListBean) {

        Intent intent = new Intent(getApplication(), ProductActivity.class);
        intent.putExtra("data", productListBean);
        startActivity(intent);
    }

    @Override
    public void onCartClick(ProductListBean productListBean) {

        addtocart(productListBean);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.front_menu, menu);


        final MenuItem menuItem = menu.findItem(R.id.cart);

        View actionView = menuItem.getActionView();
        cart_badge = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intenti = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intenti);
                return true;
            case R.id.cart:
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        int cartCountYalu = db.getCartCountYalu(sharedpreferences.getString(AppConfig.user_id, ""));
        if (cart_badge != null) {
            if (cartCountYalu == 0) {
                if (cart_badge.getVisibility() != View.GONE) {
                    cart_badge.setVisibility(View.GONE);
                }
            } else {
               cart_badge.setText(String.valueOf(Math.min(cartCountYalu, 99)));
                if (cart_badge.getVisibility() != View.VISIBLE) {
                    cart_badge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*   setupBadge();*/
        fetchProductList("");
        try {
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.putString(AppConfig.userId, sharedpreferences.getString(AppConfig.user_id, "guest"));
            edit.commit();
            if (sharedpreferences.contains(AppConfig.usernameKey)) {
                getSupportActionBar().setSubtitle(sharedpreferences.getString(AppConfig.usernameKey, ""));
            }
            setupBadge();
        } catch (Exception e) {

        }
    }
}
