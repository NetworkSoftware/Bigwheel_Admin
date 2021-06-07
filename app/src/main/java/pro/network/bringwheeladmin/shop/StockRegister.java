package pro.network.bringwheeladmin.shop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.rahul.media.main.MediaFactory;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.bringwheeladmin.R;
import pro.network.bringwheeladmin.app.ActivityMediaOnline;
import pro.network.bringwheeladmin.app.AndroidMultiPartEntity;
import pro.network.bringwheeladmin.app.AppController;
import pro.network.bringwheeladmin.app.Appconfig;

import static pro.network.bringwheeladmin.app.Appconfig.CATEGORIES_GET_ALL;
import static pro.network.bringwheeladmin.app.Appconfig.CATEGORY;
import static pro.network.bringwheeladmin.app.Appconfig.PRODUCT_CREATE;
import static pro.network.bringwheeladmin.app.Appconfig.mypreference;

public class StockRegister extends AppCompatActivity implements ImageClick {


    private final String[] OWNERTYPE = new String[]{
            "Wholesale", "Retail",
    };

    public static String[] OFFERS = new String[]{
            "Deal of day", "Discount for you", "Sponsored", "Seasonal Offers", "Top Offers","General"
    };
    AddImageAdapter maddImageAdapter;
    TextInputEditText productName;
    TextInputEditText price;
    TextInputEditText mrprice;
    TextInputEditText discountName;
    TextInputEditText totalstock, description,shipCost;
    TextInputLayout ownertypeText;
    TextInputLayout subcategoryText;
    TextInputLayout productNameText;
    TextInputLayout priceText;
    TextInputLayout mrpriceText;
    TextInputLayout discountText;
    TextInputLayout totalstockText, descriptionText, offersText;
    SharedPreferences sharedpreferences;
    BetterSpinner ownertype;
    BetterSpinner subcategory;
    BetterSpinner offers;
    TextInputEditText secondcategory;
    CardView itemsAdd;
    Button submit;
    ArrayList<String> all_path = new ArrayList<>();
    String studentId = null;
    private RecyclerView imagelist;
    private ArrayList<String> samplesList = new ArrayList<>();
    private ProgressDialog pDialog;
    private String imageUrl = "";
    private MediaFactory mediaFactory;

    public Button addSize;
    private RecyclerView sizelist;
    ArrayList<String> sizes = new ArrayList<>();
    SizeAdapter sizeAdapter;

    public static String round(double d, int decimalPlace) {
        return String.format("%.2f", d);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stocklist_register);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        getSupportActionBar().setTitle("Stock - Register");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemsAdd = (CardView) findViewById(R.id.itemsAdd);
        ImageView image_wallpaper = (ImageView) findViewById(R.id.image_wallpaper);

        addSize = findViewById(R.id.addSize);
        sizelist = findViewById(R.id.sizelist);
        sizes = new ArrayList<>();
        addSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSizeBottom();
            }
        });
        sizeAdapter = new SizeAdapter(this, sizes, new ImageClick() {
            @Override
            public void onImageClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                sizes.remove(position);
                sizeAdapter.notifyData(sizes);
            }
        }, true);
        final LinearLayoutManager sizeManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        sizelist.setLayoutManager(sizeManager);
        sizelist.setAdapter(sizeAdapter);

        productName = findViewById(R.id.productName);
        price = findViewById(R.id.price);
        mrprice = findViewById(R.id.mrprice);
        discountName = findViewById(R.id.discountName);
        totalstock = findViewById(R.id.totalstock);

        ownertypeText = findViewById(R.id.ownertypeText);
        subcategoryText = findViewById(R.id.subcategoryText);
        productNameText = findViewById(R.id.productNameText);
        priceText = findViewById(R.id.priceText);
        mrpriceText = findViewById(R.id.mrpriceText);
        discountText = findViewById(R.id.discountText);
        totalstockText = findViewById(R.id.totalstockText);
        description = findViewById(R.id.description);
        descriptionText = findViewById(R.id.descrpitionext);
        offers = findViewById(R.id.offers);
        shipCost=findViewById(R.id.shipCost);




        mrprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable q) {
                doDsicountandApply();
            }
        });
        discountName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable p) {
                doDsicountandApply();
            }
        });


        image_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker();
            }
        });

        samplesList = new ArrayList<>();
        imagelist = (RecyclerView) findViewById(R.id.imagelist);
        maddImageAdapter = new AddImageAdapter(this, samplesList, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imagelist.setLayoutManager(addManager1);
        imagelist.setAdapter(maddImageAdapter);
        subcategory = findViewById(R.id.subcategory);

       /* ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Appconfig.BRAND);
        subcategory.setAdapter(titleAdapter);

        subcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(StockRegister.this,
                        android.R.layout.simple_dropdown_item_1line, Appconfig.getArrayFromCategory(Appconfig.CATEGORY[position]));
                secondcategory.setAdapter(titleAdapter);
            }
        });
*/
        secondcategory = findViewById(R.id.secondcategory);

        ownertype = findViewById(R.id.ownertype);

        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, OWNERTYPE);
        ownertype.setAdapter(stockAdapter);
        ownertype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        ArrayAdapter<String> offerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, OFFERS);
        offers.setAdapter(offerAdapter);
        offers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Appconfig.CATEGORY);
        subcategory.setAdapter(titleAdapter);
        subcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(StockRegister.this,
                        android.R.layout.simple_dropdown_item_1line, Appconfig.getSubCatFromCat(CATEGORY[position]));
                subcategory.setAdapter(brandAdapter);
                subcategory.setThreshold(1);
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ownertype.getText().toString().length() <= 0) {
                    ownertypeText.setError("Select the Type");
                } else if (subcategory.getText().toString().length() <= 0) {
                    subcategory.setError("Select the SubCategory");
                } else if (productName.getText().toString().length() <= 0) {
                    productName.setError("Enter the Product Name");
                } else if (price.getText().toString().length() <= 0) {
                    price.setError("Enter the Price");
                } else if (mrprice.getText().toString().length() <= 0) {
                    mrprice.setError("Select the MRP");
                } else if (totalstock.getText().toString().length() <= 0) {
                    totalstock.setError("Select the Stock");
                } else if (samplesList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Upload the Images!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser();
                }

            }
        });
        getAllCategories();


        try {

            StockList contact = (StockList) getIntent().getSerializableExtra("data");

            studentId = contact.id;
            ownertype.setText(contact.ownertype);
            subcategory.setText(contact.subcategory);
            productName.setText(contact.productName);
            price.setText(contact.price);
            mrprice.setText(contact.mrprice);
            discountName.setText(contact.discountName);
            totalstock.setText(contact.totalstock);
            description.setText(contact.description);
            secondcategory.setText(contact.secondcategory);
            shipCost.setText(contact.shipCost);
            imageUrl = contact.image;
            if (imageUrl == null) {
                imageUrl = "";
            } else {
                samplesList = new Gson().fromJson(imageUrl, (Type) List.class);
            }

            if (contact.getSizes() == null || contact.getSizes().equalsIgnoreCase("null")) {
                sizes = new ArrayList<>();
            } else {
                try {
                    sizes = new Gson().fromJson(contact.getSizes(), (Type) List.class);
                }catch (Exception e){
                    Log.e("xxxxxxxxxx",e.toString());
                }
            }
            sizeAdapter.notifyData(sizes);

            offers.setText(contact.offers);
            maddImageAdapter.notifyData(samplesList);

        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());

        }

    }

    private void doDsicountandApply() {
        if (mrprice.getText().toString().length() > 0 &&
                discountName.getText().toString().length() > 0) {
            float mrpPriceF = Float.parseFloat(mrprice.getText().toString());
            float discountF = Float.parseFloat(discountName.getText().toString());
            price.setText(round(mrpPriceF - (mrpPriceF * (discountF / 100)), 2));
        }
    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Createing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                PRODUCT_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response.split("0000")[1]);
                    boolean success = jsonObject.getBoolean("success");
                    String msg = jsonObject.getString("message");
                    if (success) {
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                if (studentId != null) {
                    localHashMap.put("id", studentId);
                }
                localHashMap.put("type", ownertype.getText().toString());
                localHashMap.put("category", subcategory.getText().toString());
                localHashMap.put("offers", offers.getText().toString());
                localHashMap.put("productname", productName.getText().toString());
                localHashMap.put("price", price.getText().toString());
                localHashMap.put("mrpprice", mrprice.getText().toString());
                localHashMap.put("discount", discountName.getText().toString());
                localHashMap.put("stock_update", totalstock.getText().toString());
                localHashMap.put("description", description.getText().toString());
                localHashMap.put("subcategory", secondcategory.getText().toString());
                localHashMap.put("shopid", sharedpreferences.getString(Appconfig.shopIdKey, ""));
                localHashMap.put("image", new Gson().toJson(samplesList));
                localHashMap.put("sizes", new Gson().toJson(sizes));
                localHashMap.put("shipCost", Integer.parseInt(shipCost.getText().toString())<50
                        ?"50":shipCost.getText().toString());
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
    }

    @Override
    public void onImageClick(int position) {
        Intent localIntent = new Intent(StockRegister.this, ActivityMediaOnline.class);
        localIntent.putExtra("filePath", samplesList.get(position));
        localIntent.putExtra("isImage", true);
        startActivity(localIntent);
    }

    @Override
    public void onDeleteClick(int position) {
        samplesList.remove(position);
        maddImageAdapter.notifyData(samplesList);
    }

    private void performUpload() {
        if (all_path.size() > 0) {
            String path = all_path.get(0);
            all_path.remove(0);
            showDialog();
            new UploadFileToServer().execute(Appconfig.compressImage(path, StockRegister.this));
        } else {
            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
            hideDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            all_path = new ArrayList<>();
            all_path = mediaFactory.onActivityResult(requestCode, resultCode, data);
            performUpload();
        } catch (Exception e) {
            Log.e("xxxxxxxx", e.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void imagePicker() {
        if (samplesList.size() <= Appconfig.MAX_COUNT) {
            final CharSequence[] items;
            if (Appconfig.isDeviceSupportCamera(StockRegister.this)) {
                items = new CharSequence[2];
                items[0] = "Camera";
                items[1] = "Gallery";
            } else {
                items = new CharSequence[1];
                items[0] = "Gallery";
            }

            android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(StockRegister.this);
            alertdialog.setTitle("Add Image");
            alertdialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Camera")) {
                        MediaFactory.MediaBuilder mediaBuilder = new MediaFactory.MediaBuilder(StockRegister.this)
                                .fromCamera()
                                .setPickCount(Appconfig.MAX_COUNT - samplesList.size())
                                .isSquareCrop(false)
                                .doCropping();
                        mediaFactory = MediaFactory.create().start(mediaBuilder);
                    } else if (items[item].equals("Gallery")) {
                        MediaFactory.MediaBuilder mediaBuilder = new MediaFactory.MediaBuilder(StockRegister.this)
                                .fromGallery()
                                .setPickCount(Appconfig.MAX_COUNT - samplesList.size())
                                .isSquareCrop(false)
                                .doCropping();
                        mediaFactory = MediaFactory.create().start(mediaBuilder);
                    }
                }
            });
            alertdialog.show();
        }
    }

    private void sendNotification(String title, String description) {
        String tag_string_req = "req_register";
        showDialog();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("to", "/topics/allDevices");
            jsonObject.put("priority", "high");
            JSONObject dataObject = new JSONObject();
            dataObject.put("title", title);
            dataObject.put("message", description);
            jsonObject.put("data", dataObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("Content-Type", "application/json");
                hashMap.put("Authorization", "key=AAAAj5XpsiY:APA91bHTw6GeFODTwPdfE5FWwxxGsI6N4eiYTD2vMQxU_XP2jrp8bgtqxBClC88IkYWmfEp1jPp7L1s44X6wisbDlA3RONCVJuzlZH3sdZJrjXnhaE-i8FyHkfm56zbBgyTwEkHjzgef");
                return hashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        public long totalSize = 0;
        String filepath;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (progress[0]));
        }

        @Override
        protected String doInBackground(String... params) {
            filepath = params[0];
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Appconfig.URL_IMAGE_UPLOAD);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filepath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;

                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (!jsonObject.getBoolean("error")) {
                    imageUrl = Appconfig.ip + "/images/" + filepath.substring(filepath.lastIndexOf('/') + 1);
                    samplesList.add(imageUrl);
                    maddImageAdapter.notifyData(samplesList);
                } else {
                    imageUrl = null;
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Error | Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            // showing the server response in an alert dialog
            //showAlert(result);

            performUpload();

            super.onPostExecute(result);
        }

    }

    private void showSizeBottom() {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(StockRegister.this);
        LayoutInflater inflater = StockRegister.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_size_layout, null);


        final TextInputEditText size = dialogView.findViewById(R.id.size);
        final Button submit = dialogView.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Size", Toast.LENGTH_LONG).show();
                    return;
                }
                sizes.add(size.getText().toString());
                sizeAdapter.notifyData(sizes);
                mBottomSheetDialog.cancel();
            }
        });
        size.requestFocus();

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
    private void getAllCategories() {
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                CATEGORIES_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        CATEGORY = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CATEGORY[i] = jsonArray.getJSONObject(i).getString("title");
                        }
                        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(StockRegister.this,
                                android.R.layout.simple_dropdown_item_1line, CATEGORY);
                        subcategory.setAdapter(titleAdapter);
                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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


}
