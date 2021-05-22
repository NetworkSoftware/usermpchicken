package pro.network.jsbroilers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import pro.network.jsbroilers.app.AppConfig;
import pro.network.jsbroilers.app.AppController;
import pro.network.jsbroilers.app.DatabaseHelperYalu;
import pro.network.jsbroilers.cart.CartActivity;
import pro.network.jsbroilers.chip.OnChip;
import pro.network.jsbroilers.orders.MyOrderPage;
import pro.network.jsbroilers.product.BannerActivity;
import pro.network.jsbroilers.product.ProductActivity;
import pro.network.jsbroilers.product.ProductItemClick;
import pro.network.jsbroilers.product.ProductListBean;
import pro.network.jsbroilers.recentproducts.CategoryProduct;
import pro.network.jsbroilers.recentproducts.CategoryWiseProductAdapter;

import static pro.network.jsbroilers.app.AppConfig.mypreference;

public class HomeActivity extends AppCompatActivity implements ProductItemClick {


    BannerLayout banner;
    ProgressBar categoryProgress;
    SharedPreferences sharedpreferences;
    ArrayList<CategoryProduct> categoryProducts = new ArrayList<>();
    Map<String, ArrayList<ProductListBean>> categoryProduct = new HashMap<>();
    private DatabaseHelperYalu db;
    private CategoryWiseProductAdapter categoryWiseProductAdapter;
    private TextView cart_badge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(AppConfig.userId, sharedpreferences.getString(AppConfig.user_id, "guest"));
        edit.commit();
        categoryProgress = findViewById(R.id.categoryProgress);
        db = new DatabaseHelperYalu(this);
        banner = findViewById(R.id.Banner);
        fetchBanner();


        RecyclerView categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryWiseProductAdapter = new CategoryWiseProductAdapter(HomeActivity.this,
                categoryProducts, this);
        LinearLayoutManager addManager1 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        categoryRecyclerView.setLayoutManager(addManager1);
        categoryRecyclerView.setAdapter(categoryWiseProductAdapter);

        fetchProductList();

    }


    private void fetchBanner() {
        String tag_string_req = "req_register";
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.BANNERS_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
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
                                Intent intent = new Intent(getApplicationContext(), BannerActivity.class);
                                intent.putExtra("description", bannerBean.getDescription());
                                intent.putExtra("image", bannerBean.getImages());
                                startActivityForResult(intent, 100);
                            }
                        });
                        banner.setAdapter(webBannerAdapter);

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
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

    private void fetchProductList() {
        String tag_string_req = "req_register";
        categoryProgress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PRODUCT_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                categoryProgress.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    categoryProducts = new ArrayList<>();
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ProductListBean productListBean = new ProductListBean();
                            String category = jsonObject.getString("category");
                            productListBean.setId(jsonObject.getString("id"));
                            productListBean.setBrand(jsonObject.getString("brand"));
                            productListBean.setPrice(jsonObject.getString("price"));
                            productListBean.setRam(jsonObject.getString("ram"));
                            productListBean.setRom(jsonObject.getString("rom"));
                            productListBean.setModel(jsonObject.getString("model"));
                            productListBean.setImage(jsonObject.getString("image"));
                            productListBean.setDescription(jsonObject.getString("description"));
                            productListBean.setStock_update(jsonObject.getString("stock_update"));
                            addMapWithCategoryProduct(category, productListBean);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    for (Map.Entry<String, ArrayList<ProductListBean>> entry : categoryProduct.entrySet()) {
                        CategoryProduct categoryProduct1 = new CategoryProduct();
                        categoryProduct1.setTitle(entry.getKey());
                        categoryProduct1.setProductListBeans(entry.getValue());
                        categoryProducts.add(categoryProduct1);
                    }

                    categoryWiseProductAdapter.notifyData(categoryProducts);

                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                categoryProgress.setVisibility(View.GONE);
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

    private void addMapWithCategoryProduct(String category, ProductListBean productListBean) {
        ArrayList<ProductListBean> productListBeans = categoryProduct.containsKey(category) ?
                categoryProduct.get(category) : new ArrayList<ProductListBean>();
        productListBeans.add(productListBean);
        categoryProduct.put(category, productListBeans);
    }


    private void addtocart(ProductListBean productListBean) {
        productListBean.setQty("1");
        db.insertMainbeanyalu(productListBean, sharedpreferences.getString(AppConfig.user_id, ""));
        Toast.makeText(getApplication(), "Item added successfully", Toast.LENGTH_SHORT).show();
        setupBadge();
        categoryWiseProductAdapter.notifyDataSetChanged();
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

            case R.id.order:
                Intent intentk = new Intent(HomeActivity.this, MyOrderPage.class);
                startActivity(intentk);

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
        fetchProductList();
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

    private void startAllProduct(String view) {
        Intent intent = new Intent(HomeActivity.this, AllProductActivity.class);
        intent.putExtra("type", view);
        startActivity(intent);
    }
}
