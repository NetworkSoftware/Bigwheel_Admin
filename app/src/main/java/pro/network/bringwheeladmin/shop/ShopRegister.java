package pro.network.bringwheeladmin.shop;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pro.network.bringwheeladmin.R;
import pro.network.bringwheeladmin.app.AppController;
import pro.network.bringwheeladmin.app.Appconfig;

import static pro.network.bringwheeladmin.app.Appconfig.SHOP_CREATE;
import static pro.network.bringwheeladmin.app.Appconfig.SHOP_UPDATE;

/**
 * Created by user_1 on 11-07-2018.
 */

public class ShopRegister extends AppCompatActivity {


    TextInputEditText shopName;
    TextInputEditText gstNo;
    TextInputEditText phoneone,password;
    TextInputEditText phonetwo;
    TextInputEditText area;
    TextInputEditText buildingName;
    TextInputEditText landmark;
    TextInputEditText pincode;
    TextInputEditText city;
    TextInputEditText state,panNo,adharNo;

    TextInputLayout shopNameText;
    TextInputLayout gstNoText;
    TextInputLayout productNameText;
    TextInputLayout phoneoneText;
    TextInputLayout phonetwoText;
    TextInputLayout areaText;
    TextInputLayout buildingNameText;
    TextInputLayout landmarkText;
    TextInputLayout pincodeText;
    TextInputLayout cityText;
    TextInputLayout stateText,panText,adharText,idproofText,passwordText;

    BetterSpinner productName;
    private String[] PRODUCTNAME = new String[]{
            "Mobiles", "Fashion","Electronics","Home","Beauty","Appliances","Toy & Baby","Sports","Food & more",
    };

    BetterSpinner idproof;
    private String[] IDPROOF = new String[]{
            "PAN Card No", "GST No","Aadhaar Card No",
    };

    Button submit;
    String studentId = null;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_register);

        getSupportActionBar().setTitle("Shop - Register");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        shopName =  findViewById(R.id.shopName);
        gstNo =  findViewById(R.id.gstNo);
        phoneone =  findViewById(R.id.phoneone);
        phonetwo =  findViewById(R.id.phonetwo);
        area =  findViewById(R.id.area);
        buildingName =  findViewById(R.id.buildingName);
        landmark =  findViewById(R.id.landmark);
        pincode =  findViewById(R.id.pincode);
        city =  findViewById(R.id.city);
        state =  findViewById(R.id.state);
        panNo =  findViewById(R.id.panNo);
        panText =  findViewById(R.id.panText);
        idproof =  findViewById(R.id.idproof);
        idproofText =  findViewById(R.id.idproofText);
        adharNo =  findViewById(R.id.adharNo);
        adharText =  findViewById(R.id.adharText);

        shopNameText =  findViewById(R.id.shopNameText);
        gstNoText =  findViewById(R.id.gstNoText);
        productNameText =  findViewById(R.id.productNameText);
        phoneoneText =  findViewById(R.id.phoneoneText);
        phonetwoText =  findViewById(R.id.phonetwoText);
        areaText =  findViewById(R.id.areaText);
        buildingNameText =  findViewById(R.id.buildingNameText);
        landmarkText =  findViewById(R.id.landmarkText);
        pincodeText =  findViewById(R.id.pincodeText);
        cityText =  findViewById(R.id.cityText);
        stateText =  findViewById(R.id.stateText);
        password =  findViewById(R.id.password);
        passwordText=findViewById(R.id.passwordText);
        productName =  findViewById(R.id.productName);
        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,PRODUCTNAME);
        productName.setAdapter(titleAdapter);

        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,IDPROOF);
        idproof.setAdapter(Adapter);

        idproof.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (idproof.getText().toString().equalsIgnoreCase(("PAN Card No"))) {
                    panText.setVisibility(View.VISIBLE);
                    gstNoText.setVisibility(View.GONE);
                    adharText.setVisibility(View.GONE);
                }
                else  if (idproof.getText().toString().equalsIgnoreCase(("GST No"))) {
                    panText.setVisibility(View.GONE);
                    gstNoText.setVisibility(View.VISIBLE);
                    adharText.setVisibility(View.GONE);
                } else if (idproof.getText().toString().equalsIgnoreCase(("Aadhaar Card No"))) {
                    panText.setVisibility(View.GONE);
                    gstNoText.setVisibility(View.GONE);
                    adharText.setVisibility(View.VISIBLE);
                }
            }

        });


        submit =findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shopName.getText().toString().length() <= 0) {
                    shopName.setError("Enter the Shop Name");
                } else if (panNo.getText().toString().length()<=0 &&
                        gstNo.getText().toString().length() <= 0 && adharNo.getText().toString().length()<=0) {
                    gstNo.setError("Enter the GST or PAN or adhar Number");
                } else if (productName.getText().toString().length() <= 0) {
                    productName.setError("Select the Product");
                } else if (phoneone.getText().toString().length() <= 0) {
                    phoneone.setError("Enter the Phone Number");
                } else if (pincode.getText().toString().length() <= 0) {
                    pincode.setError("Enter the Pincode");
                }  else if (city.getText().toString().length() <= 0) {
                    city.setError("Enter the City");
                } else if (state.getText().toString().length() <= 0) {
                    state.setError("Enter the State");
                }else if (area.getText().toString().length() <= 0) {
                    area.setError("Enter the Locality / Area / Street");
                }else if (buildingName.getText().toString().length() <= 0) {
                    buildingName.setError("Enter the Flat no / Building Name");
                }else if (password.length() < 8) {
                    passwordText.setError("Enter Password at least 8 digits");
                }else {
                    registerUser();
                }

            }
        });

        try {

            Shop contact = (Shop) getIntent().getSerializableExtra("data");

            studentId = contact.id;
            shopName.setText(contact.shopName);
            gstNo.setText(contact.gstNo);
            productName.setText(contact.productName);
            phoneone.setText(contact.phoneone);
            phonetwo.setText(contact.phonetwo);
            area.setText(contact.area);
            buildingName.setText(contact.buildingName);
            landmark.setText(contact.landmark);
            pincode.setText(contact.pincode);
            city.setText(contact.city);
            state.setText(contact.state);
            password.setText(contact.password);

        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());

        }
    }

    private void registerUser() {
        String tag_string_req = "req_register";
       progressDialog.setMessage("Creating Shop");
        String url = SHOP_CREATE;
        if (studentId != null) {
            url = SHOP_UPDATE;
        } showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
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
                localHashMap.put("shopName", shopName.getText().toString());
                localHashMap.put("gstNo", gstNo.getText().toString());
                localHashMap.put("productName", productName.getText().toString());
                localHashMap.put("phoneone", phoneone.getText().toString());
                localHashMap.put("phonetwo", phonetwo.getText().toString());
                localHashMap.put("area", area.getText().toString());
                localHashMap.put("buildingName", buildingName.getText().toString());
                localHashMap.put("landmark", landmark.getText().toString());
                localHashMap.put("pincode", pincode.getText().toString());
                localHashMap.put("password", password.getText().toString());
                localHashMap.put("city", city.getText().toString());
                localHashMap.put("state", state.getText().toString());
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
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
    protected void onPause() {
        super.onPause();
        hideDialog();
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
