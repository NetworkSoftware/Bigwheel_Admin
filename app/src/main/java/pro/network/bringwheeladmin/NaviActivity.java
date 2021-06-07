package pro.network.bringwheeladmin;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;

import pro.network.bringwheeladmin.banner.MainActivityBanner;
import pro.network.bringwheeladmin.categories.MainActivityCategories;
import pro.network.bringwheeladmin.deliveryboy.MainActivityDelivery;
import pro.network.bringwheeladmin.feedback.MainActivityFeedback;
import pro.network.bringwheeladmin.order.MainActivityOrder;
import pro.network.bringwheeladmin.shop.MainActivityShop;

public class NaviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);


        CardView deliveryboy = (CardView) findViewById(R.id.deliveryboy);
        deliveryboy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityDelivery.class);
                startActivity(io);
            }
        });
        CardView catrgories = (CardView) findViewById(R.id.catrgories);
        catrgories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityCategories.class);
                startActivity(io);
            }
        });

        CardView stock = (CardView) findViewById(R.id.stock);
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityShop.class);
                startActivity(io);

            }
        });
        CardView banner = (CardView) findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityBanner.class);
                startActivity(io);

            }
        });

        CardView order = (CardView) findViewById(R.id.orders);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityOrder.class);
                startActivity(io);

            }
        });

        CardView feedback = (CardView) findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityFeedback.class);
                startActivity(io);

            }
        });

    }
}
