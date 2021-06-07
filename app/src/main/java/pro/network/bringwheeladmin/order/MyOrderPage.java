package pro.network.bringwheeladmin.order;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import pro.network.bringwheeladmin.R;
import pro.network.bringwheeladmin.app.AppController;
import pro.network.bringwheeladmin.app.Appconfig;
import pro.network.bringwheeladmin.app.HeaderFooterPageEvent;
import pro.network.bringwheeladmin.app.PdfConfig;
import pro.network.bringwheeladmin.shop.StockList;

import static pro.network.bringwheeladmin.app.Appconfig.FETCH_ADDRESS;


public class MyOrderPage extends AppCompatActivity implements Ondelete {

    RecyclerView myorders_list;
    MyOrderListProAdapter myOrderListAdapter;
    TextView address;
    TextView coupon;
    TextView delivery;
    TextView payment;
    TextView grandtotal;
    TextView shippingTotal;
    TextView couponTotal;
    TextView subtotal, status, paymentId, deliveryTime, comments;
    LinkedHashMap<String, String> stringStringMap = new LinkedHashMap<>();
    ProgressDialog progressDialog;
    private ArrayList<StockList> myorderBeans = new ArrayList<>();
    private Button invoice;
    private Order myorderBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setTitle("My Orders");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        myorders_list = findViewById(R.id.myorders_list);
        myOrderListAdapter = new MyOrderListProAdapter(getApplicationContext(), myorderBeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        myorders_list.setLayoutManager(addManager1);
        myorders_list.setAdapter(myOrderListAdapter);

        address = findViewById(R.id.address);
        coupon = findViewById(R.id.coupon);
        delivery = findViewById(R.id.delivery);
        payment = findViewById(R.id.payment);
        grandtotal = findViewById(R.id.grandtotal);
        shippingTotal = findViewById(R.id.shippingTotal);
        couponTotal = findViewById(R.id.couponTotal);
        subtotal = findViewById(R.id.subtotal);
        status = findViewById(R.id.status);
        paymentId = findViewById(R.id.paymentId);
        comments = findViewById(R.id.comments);
        deliveryTime = findViewById(R.id.deliveryTime);
        invoice = findViewById(R.id.invoice);

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MyOrderPage.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyOrderPage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    printFunction();
                }
            }
        });

        getValuesFromIntent();
    }

    private void getValuesFromIntent() {
        try {

            myorderBean = (Order) getIntent().getSerializableExtra("data");
            address.setText(myorderBean.address);
            stringStringMap.put("Order Id", (myorderBean.getDelivery()
                    .equalsIgnoreCase("express") ? "ECF" : "SCF") + myorderBean.getId());
            stringStringMap.put("Address", myorderBean.address);
            coupon.setText(myorderBean.coupon);
            stringStringMap.put("Coupon", myorderBean.coupon);
            status.setText(myorderBean.status);
            stringStringMap.put("Status", myorderBean.status);
            delivery.setText(myorderBean.delivery);
            stringStringMap.put("Delivery", myorderBean.delivery);
            payment.setText(myorderBean.payment);
            stringStringMap.put("Payment mode", myorderBean.payment);
            paymentId.setText(myorderBean.paymentId);
            stringStringMap.put("Payment ID", myorderBean.paymentId);
            comments.setText(myorderBean.comments);
            stringStringMap.put("Comments", myorderBean.comments);
            deliveryTime.setText(myorderBean.deliveryTime);
            stringStringMap.put("Delivery time", myorderBean.deliveryTime);
            grandtotal.setText(myorderBean.grandCost);
            stringStringMap.put("Date", myorderBean.createdon);
            myorderBeans = myorderBean.productBeans;
            for (int i = 0; i < myorderBeans.size(); i++) {
                StockList productListBean = myorderBeans.get(i);
                String qty = productListBean.qty;
                try {
                    if (qty == null || !qty.matches("-?\\d+(\\.\\d+)?")) {
                        qty = "1";
                    }
                    float startValue = Float.parseFloat(productListBean.getPrice()) * Integer.parseInt(qty);
                    String s = productListBean.getQty() + "*" + productListBean.getPrice();
                    stringStringMap.put(productListBean.getProductName(), s);
                } catch (Exception e) {

                }
            }
            stringStringMap.put("Grand total", myorderBean.grandCost);
            shippingTotal.setText(myorderBean.shipCost);
            stringStringMap.put("Shipping total", myorderBean.shipCost);
            couponTotal.setText(myorderBean.couponCost);
            stringStringMap.put("Coupon total", myorderBean.couponCost);
            subtotal.setText(myorderBean.amount);
            stringStringMap.put("Sub total", myorderBean.amount);
            myOrderListAdapter.notifyData(myorderBeans, myorderBean.status);

            if (paymentId != null) {
                paymentId.setText(myorderBean.getPaymentId());
            }

            getSupportActionBar().setTitle("Order id:#" + (myorderBean.getDelivery()
                    .equalsIgnoreCase("express") ? "ECF" : "SCF")+myorderBean.getId());

            fetchAddressById(myorderBean.address);

        } catch (Exception e) {
            Log.e("xxxxxxxxx", e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getValuesFromIntent();
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

    private void deleteBookedItems(final int position) {
//        String tag_string_req = "req_register_add";
//        progressDialog.setMessage("Deleting...");
//        showDialog();
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                UPDATE_ITEMS, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                hideDialog();
//                Log.d("Register Response: ", response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        myorderBeans.remove(position);
//                        myOrderListAdapter.notifyData(myorderBeans, myorderBean.status);
//                    }
//                } catch (JSONException e) {
//                    Log.e("Xxxxxxxxx", "Something went wrong");
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideDialog();
//                Log.e("Xxxxxxxxx", "Something went wrong");
//            }
//        }) {
//            protected Map<String, String> getParams() {
//                HashMap localHashMap = new HashMap();
//                localHashMap.put("id", myorderBean.getId());
//                ArrayList<Product> productListBeans = myorderBeans;
//                try {
//                    productListBeans.remove(position);
//                } catch (Exception e) {
//                }
//                localHashMap.put("items", new Gson().toJson(productListBeans));
//                return localHashMap;
//            }
//        };
//        strReq.setRetryPolicy(Appconfig.getPolicy());
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void fetchAddressById(final String id) {
        String tag_string_req = "req_register_add";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                FETCH_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(jsonObject.getString("name")).append("\n");
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1.append(jsonObject.getString("address")).append("\n");
                        stringBuilder1.append(jsonObject.getString("mobile")).append("\n");
                        stringBuilder1.append(jsonObject.getString("alternativeMobile")).append("\n");
                        stringBuilder1.append(jsonObject.getString("landmark")).append("\n");
                        stringBuilder1.append(jsonObject.getString("pincode"));
                        address.setText(stringBuilder.toString());
                        address.setText(stringBuilder.append(stringBuilder1).toString());
                        myorderBean.setAddressOrg(stringBuilder1.toString());
                        stringStringMap.put("Address", stringBuilder.toString());

                    }
                } catch (JSONException e) {
                    Log.e("Xxxxxxxxx", "Something went wrong");

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Xxxxxxxxx", "Something went wrong");
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("id", id);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void printFunction() {
        try {
            String path = getExternalCacheDir().getPath() + "/PDF";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Log.d("PDFCreator", "PDF Path: " + path);
            File file = new File(dir, System.currentTimeMillis()+"_"+stringStringMap.get("Order Id") + ".pdf");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fOut = new FileOutputStream(file);


            Document document = new Document(PageSize.A4, 30, 28, 40, 119);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, fOut);

            document.open();
            PdfConfig.addMetaData(document);
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            pdfWriter.setPageEvent(event);
            PdfConfig.addContent(document, myorderBean, MyOrderPage.this);
            document.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } else {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file), "application/pdf");
                Intent intent = Intent.createChooser(target, "Open File");
                startActivity(intent);
            }

        } catch (Error | Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                printFunction();
            } else {
                Toast.makeText(MyOrderPage.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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
    public void ondeleteItem(int position) {
        AlertDialog diaBox = AskOption(position);
        diaBox.show();
    }

    private AlertDialog AskOption(final int position) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteBookedItems(position);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}
