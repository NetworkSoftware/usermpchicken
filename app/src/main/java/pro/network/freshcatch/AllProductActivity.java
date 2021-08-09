package pro.network.freshcatch;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.freshcatch.app.AppConfig;
import pro.network.freshcatch.app.AppController;
import pro.network.freshcatch.app.BaseActivity;
import pro.network.freshcatch.app.DbCart;
import pro.network.freshcatch.cart.CartActivity;
import pro.network.freshcatch.chip.ChipAdapter;
import pro.network.freshcatch.chip.ChipBean;
import pro.network.freshcatch.chip.OnChip;
import pro.network.freshcatch.product.ProductActivity;
import pro.network.freshcatch.product.ProductItemClick;
import pro.network.freshcatch.product.ProductListBean;
import pro.network.freshcatch.product.SingleProductAdapter;

public class AllProductActivity extends BaseActivity implements ProductItemClick, OnChip {
    private final String TAG = getClass().getSimpleName();
    private final ArrayList<ChipBean> chipBeans = new ArrayList<>();
    ProgressDialog pDialog;
    RecyclerView recycler_product;
    SingleProductAdapter productListAdapter;
    SharedPreferences sharedpreferences;
    CartActivity.OnCartItemChange onCartItemChange;
    RecyclerView recycler_chips;
    ChipAdapter chipAdapter;
    private DbCart db;
    private List<ProductListBean> productList = new ArrayList<>();
    private SearchView searchView;
    private TextView cart_badge, title;
    private String selectedType = "COMBO PACK";
    private ImageView bannerImage;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_all_product);
        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setCancelable(false);
        sharedpreferences = getApplicationContext().getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(AppConfig.userId, sharedpreferences.getString(AppConfig.user_id, ""));
        edit.commit();

        db = new DbCart(getApplicationContext());
        recycler_product = findViewById(R.id.recycler_product);
        productList = new ArrayList<>();
        productListAdapter = new SingleProductAdapter(getApplicationContext(), productList, this, sharedpreferences);
        final GridLayoutManager addManager1 = new GridLayoutManager(getApplication(), 2);
        recycler_product.setLayoutManager(addManager1);
        recycler_product.setAdapter(productListAdapter);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String type = getIntent().getStringExtra("type");
        getSupportActionBar().setTitle(type != null ? type : getString(R.string.app_name));
        selectedType = type;
    }


    private void fetchProductList(final String searchKey) {
        String tag_string_req = "req_register";
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PRODUCT_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                    productList = new ArrayList<>();
                        JSONArray jsonArray = jObj.getJSONArray("data");
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
                            productListBean.setRqtyType(jsonObject.getString("rqtyType"));
                            productListBean.setRqty(jsonObject.getString("rqty"));
                            productListBean.setStock_update(jsonObject.getString("stock_update"));
                            productList.add(productListBean);
                        }
                    } else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    productListAdapter.notifyData(productList);
                    getSupportActionBar().setSubtitle(productList.size() + " Products");
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
                if (selectedType != null) {
                    localHashMap.put("category", selectedType);
                }
                if (sharedpreferences.contains("location")) {
                    localHashMap.put("locationSearch", sharedpreferences.getString("location", ""));
                }
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void addToCart(ProductListBean productListBean) {
        productListBean.setQty("1");
        db.insertProductInCart(productListBean, sharedpreferences.getString(AppConfig.user_id, ""));
        Toast.makeText(getApplication(), "Item added successfully", Toast.LENGTH_SHORT).show();
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }
        setupBadge();
        productListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onProductClick(ProductListBean productListBean) {
        try {
            Intent intent = new Intent(getApplication(), ProductActivity.class);
            intent.putExtra("data", productListBean);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Wait, Try again", Toast.LENGTH_SHORT).show();
            Log.e("xxxxxxxxxxxxx", e.toString());
        }
    }

    @Override
    public void OnQuantityChange(int position, int qty) {
        if (qty <= 0) {
            db.deleteProductById(productList.get(position)
                    , sharedpreferences.getString(AppConfig.user_id, ""));
        } else {
            productList.get(position).setQty(qty + "");
            db.insertProductInCart(productList.get(position),
                    sharedpreferences.getString(AppConfig.user_id, ""));
            if (onCartItemChange != null) {
                onCartItemChange.onCartChange();
            }
        }
        setupBadge();
        productListAdapter.notifyData(productList);
    }

    @Override
    public void onCartClick(ProductListBean productListBean) {
        addToCart(productListBean);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.product_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                fetchProductList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                fetchProductList(query);
                return false;
            }
        });

        final MenuItem menuItem = menu.findItem(R.id.cart);

        View actionView = menuItem.getActionView();
        cart_badge = actionView.findViewById(R.id.cart_badge);

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
            case R.id.cart:
                Intent intent = new Intent(AllProductActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
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
        fetchProductList("");
        setupBadge();
    }

    @Override
    public void onItemClick(String type) {
        selectedType = type;
        getSupportActionBar().setTitle(selectedType);
        chipAdapter.notifyData(selectedType);
        fetchProductList("");
    }

}
