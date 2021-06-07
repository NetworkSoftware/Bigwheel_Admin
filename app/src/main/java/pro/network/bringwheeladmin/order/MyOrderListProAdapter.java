package pro.network.bringwheeladmin.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.analytics.ecommerce.Product;

import java.util.ArrayList;

import pro.network.bringwheeladmin.R;
import pro.network.bringwheeladmin.shop.StockList;

import static pro.network.bringwheeladmin.app.Appconfig.decimalFormat;

public class MyOrderListProAdapter extends RecyclerView.Adapter<MyOrderListProAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    int selectedPosition = 0;
    String status = "ordered";
    Ondelete ondelete;
    private ArrayList<StockList> myorderBeans;

    public MyOrderListProAdapter(Context mainActivityUser, ArrayList<StockList> myorderBeans, Ondelete Ondelete) {
        this.mainActivityUser = mainActivityUser;
        this.myorderBeans = myorderBeans;
        this.ondelete = Ondelete;
    }

    public void notifyData(ArrayList<StockList> myorderBeans, String status) {
        this.myorderBeans = myorderBeans;
        this.status = status;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public MyOrderListProAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_payment_item, parent, false);

        return new MyOrderListProAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final StockList myorderBean = myorderBeans.get(position);

        holder.title.setText(myorderBean.getProductName());
        String qty = myorderBean.getQty();
        try {
            if (qty == null || !qty.matches("-?\\d+(\\.\\d+)?")) {
                qty = "1";
            }
        } catch (Exception e) {

        }
        float startValue = Float.parseFloat(myorderBean.getPrice()) * Integer.parseInt(qty);
        holder.subtitle.setText(myorderBean.getQty() + "*" + myorderBean.getPrice()+
                "=" + "₹" + decimalFormat.format(startValue) + ".00");

//        if (status.equalsIgnoreCase("ordered")) {
//            holder.deleteImg.setVisibility(View.GONE);
//        } else {
//            holder.deleteImg.setVisibility(View.GONE);
//        }
//
//        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ondelete.ondeleteItem(position);
//            }
//        });
    }

    public int getItemCount() {
        return myorderBeans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView subtitle;
        ImageView deleteImg;

        public MyViewHolder(View view) {
            super((view));
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
            deleteImg = view.findViewById(R.id.deleteImg);

        }
    }

}


