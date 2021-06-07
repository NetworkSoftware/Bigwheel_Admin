package pro.network.bringwheeladmin.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.analytics.ecommerce.Product;

import java.util.ArrayList;

import pro.network.bringwheeladmin.R;
import pro.network.bringwheeladmin.app.Appconfig;
import pro.network.bringwheeladmin.app.GlideApp;
import pro.network.bringwheeladmin.shop.StockList;

public class OrderListSubAdapter extends RecyclerView.Adapter<OrderListSubAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<StockList> myorderBeans;
    SharedPreferences preferences;
    int selectedPosition = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView qty;


        public MyViewHolder(View view) {
            super((view));
            product_image = (ImageView) view.findViewById(R.id.product_image);
            qty = (TextView) view.findViewById(R.id.qty);

        }
    }

    public OrderListSubAdapter(Context mainActivityUser, ArrayList<StockList> myorderBeans) {
        this.mainActivityUser = mainActivityUser;
        this.myorderBeans = myorderBeans;
    }

    public void notifyData(ArrayList<StockList> myorderBeans) {
        this.myorderBeans = myorderBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public OrderListSubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorders_list_sub, parent, false);

        return new OrderListSubAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final StockList myorderBean = myorderBeans.get(position);
        holder.qty.setText(myorderBean.getQty() +" "+ myorderBean.getProductName());
//        ArrayList<String> images = new Gson().fromJson(myorderBean.getImage(), (Type) List.class);
     //   holder.qty.setText("orders "+ myorderBean.getModel());

        GlideApp.with(mainActivityUser)
                .load(Appconfig.getResizedImage(myorderBean.getImage(), true))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .placeholder(R.drawable.vivo)
                .into(holder.product_image);

    }

    public int getItemCount() {
        return myorderBeans.size();
    }

}


