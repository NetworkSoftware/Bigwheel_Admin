package pro.network.bringwheeladmin.shop;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rahul.media.model.Define;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.bringwheeladmin.R;
import pro.network.bringwheeladmin.app.AppController;
import pro.network.bringwheeladmin.app.Appconfig;
import pro.network.bringwheeladmin.order.MainActivityOrder;

import static pro.network.bringwheeladmin.app.Appconfig.SHOP_GET_ALL;
import static pro.network.bringwheeladmin.app.Appconfig.SHOP_UPDATE_STATUS;
import static pro.network.bringwheeladmin.app.Appconfig.mypreference;
import static pro.network.bringwheeladmin.app.Appconfig.shopIdKey;


public class MainActivityShop extends AppCompatActivity implements ShopAdapter.ContactsAdapterListener  {
    private static final String TAG = MainActivityShop.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Shop> contactList;
    private ShopAdapter mAdapter;
    private SearchView searchView;
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        setContentView(R.layout.activity_mainshop);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Define.MEDIA_PROVIDER = getString(R.string.image_provider);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shop");

        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new ShopAdapter(this, contactList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(addManager1);
        recyclerView.setAdapter(mAdapter);

        //fetchContacts();


        FloatingActionButton addShop = (FloatingActionButton) findViewById(R.id.addShop);
        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityShop.this, ShopRegister.class);
                startActivity(intent);
            }
        });
    }


    private void fetchContacts() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                SHOP_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        contactList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Shop shop = new Shop();
                            shop.setShopName(jsonObject.getString("shopName"));
                            shop.setGstNo(jsonObject.getString("gstNo"));
                            shop.setProductName(jsonObject.getString("productName"));
                            shop.setPhoneone(jsonObject.getString("phoneone"));
                            shop.setPhonetwo(jsonObject.getString("phonetwo"));
                            shop.setArea(jsonObject.getString("area"));
                            shop.setBuildingName(jsonObject.getString("buildingName"));
                            shop.setLandmark(jsonObject.getString("landmark"));
                            shop.setPincode(jsonObject.getString("pincode"));
                            shop.setCity(jsonObject.getString("city"));
                            shop.setState(jsonObject.getString("state"));
                            shop.setStatus(jsonObject.getString("status"));
                            shop.setDescription(jsonObject.getString("description"));
                            shop.setDescription(jsonObject.getString("description"));
                            shop.setId(jsonObject.getString("id"));
                            contactList.add(shop);
                        }
                        mAdapter.notifyData(contactList);
                        getSupportActionBar().setSubtitle(String.valueOf(contactList.size()) + "  Nos");

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
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        fetchContacts();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onContactSelected(Shop shop) {
        /*Intent intent = new Intent(MainActivityShop.this, ShopUpdate.class);
        intent.putExtra("data", shop);
        startActivity(intent);*/
    }

    @Override
    public void onStatusChanged(final Shop shop, final String status) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(MainActivityShop.this, R.style.RoundShapeTheme);
        LayoutInflater inflater = MainActivityShop.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        TextView title = dialogView.findViewById(R.id.title);
        final TextInputEditText address = dialogView.findViewById(R.id.address);
        final TextInputLayout addressText = dialogView.findViewById(R.id.addressText);
        addressText.setHint("Enter description");

        title.setText("* Do you want to confirm this status? If yes shop status will be updated");
        dialogBuilder.setTitle("Alert")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (address.getText().toString().length() > 0) {
                            changeShopStatus(shop, status, address.getText().toString(), dialog);

                        } else {
                            Toast.makeText(getApplicationContext(), "Enter valid address", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();
    }



    private void changeShopStatus(final Shop shop, final String status, final String reason, final DialogInterface dialog) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                SHOP_UPDATE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        dialog.cancel();
                        fetchContacts();
                    }
                    Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
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
                localHashMap.put("status", status);
                localHashMap.put("reason", reason);
                localHashMap.put("id", shop.getId());
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onDeleteClick(Shop shop) {


    }

    @Override
    public void onEditClick(Shop shop) {

    }

    @Override
    public void onStockClick(Shop shop) {
        storeIdInPreference(shop);
        Intent intent = new Intent(MainActivityShop.this, MainActivityProduct.class);
        startActivity(intent);
    }

    private void storeIdInPreference(Shop shop) {
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putString(shopIdKey,shop.getId());
        editor.commit();
    }

    @Override
    public void onBannerClick(Shop shop) {

    }

    @Override
    public void onMyorderClick(Shop shop) {
        storeIdInPreference(shop);
        Intent intent = new Intent(MainActivityShop.this, MainActivityOrder.class);
        startActivity(intent);
    }

    @Override
    public void onWalletClick(Shop shop) {
       /* storeIdInPreference(shop);
        Intent intent = new Intent(MainActivityShop.this, MainActivityWallet.class);
        startActivity(intent);*/
    }
}
