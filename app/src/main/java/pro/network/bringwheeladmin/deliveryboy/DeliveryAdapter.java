package pro.network.bringwheeladmin.deliveryboy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import pro.network.bringwheeladmin.R;
import pro.network.bringwheeladmin.app.GlideApp;


/**
 * Created by ravi on 16/11/17.
 */

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.MyViewHolder> {
    private final Context context;
    OnDeliveryBoy onDeliveryBoy;
    private List<DeliveryBean> deliveryBeans;

    public DeliveryAdapter(Context context, List<DeliveryBean> deliveryBeans, OnDeliveryBoy onDeliveryBoy) {
        this.context = context;
        this.deliveryBeans = deliveryBeans;
        this.onDeliveryBoy = onDeliveryBoy;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DeliveryBean deliveryBean = deliveryBeans.get(position);
        holder.user_name.setText(deliveryBean.getName());
        holder.phone.setText(deliveryBean.getPhone());
        holder.status.setText(deliveryBean.getStatus());
        GlideApp.with(context)
                .load(deliveryBean.adharcard)
                .into(holder.aadhar);
        GlideApp.with(context)
                .load(deliveryBean.license)
                .into(holder.license);
        GlideApp.with(context)
                .load(deliveryBean.profileImage)
                .into(holder.profile);
        holder.active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeliveryBoy.onStatusClick(position, "active");
            }
        });

        holder.inactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeliveryBoy.onStatusClick(position, "inactive");
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryBeans.size();
    }

    public void notifyData(List<DeliveryBean> deliveryBeans) {
        this.deliveryBeans = deliveryBeans;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, phone, status;
        public ImageView license, aadhar, profile;
        MaterialButton inactive, active;

        public MyViewHolder(View view) {
            super(view);
            user_name = view.findViewById(R.id.user_name);
            phone = view.findViewById(R.id.phone);
            license = view.findViewById(R.id.license);
            aadhar = view.findViewById(R.id.aadhar);
            profile = view.findViewById(R.id.profile);
            active = view.findViewById(R.id.active);
            inactive = view.findViewById(R.id.inactive);
            status = view.findViewById(R.id.status);

        }
    }
}
