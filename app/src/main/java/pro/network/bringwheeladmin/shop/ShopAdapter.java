package pro.network.bringwheeladmin.shop;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pro.network.bringwheeladmin.R;


/**
 * Created by ravi on 16/11/17.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Shop> shopList;
    private List<Shop> shopListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final Button reject, accept, waiting;
        public TextView name, mobile, category, address, status;
        CardView cardItem;
        LinearLayout stock, myorder,wallet;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);

            mobile = view.findViewById(R.id.mobile);
            category = view.findViewById(R.id.category);
            status = view.findViewById(R.id.status);
            address = view.findViewById(R.id.address);
            accept = view.findViewById(R.id.accept);
            reject = view.findViewById(R.id.reject);
            waiting = view.findViewById(R.id.waiting);
            cardItem = view.findViewById(R.id.cardItem);
            stock = view.findViewById(R.id.stock);
            myorder = view.findViewById(R.id.myorder);
            wallet = view.findViewById(R.id.wallet);
        }
    }


    public ShopAdapter(Context context, List<Shop> shopList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.shopList = shopList;
        this.shopListFiltered = shopList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Shop shop = shopListFiltered.get(position);
        holder.name.setText(shop.getShopName());
        holder.mobile.setText(shop.getPhoneone());
        holder.category.setText(shop.getProductName());
        holder.address.setText(shop.getPincode());
        String status = shop.getStatus();
        String output = status.substring(0, 1).toUpperCase() + status.substring(1);
        holder.status.setText(output);
        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onContactSelected(shop);
            }
        });

        if (status.equalsIgnoreCase("accepted")) {
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            holder.waiting.setVisibility(View.GONE);
            holder.stock.setVisibility(View.VISIBLE);
            holder.myorder.setVisibility(View.VISIBLE);
            holder.wallet.setVisibility(View.VISIBLE);
            holder.status.setTextColor(Color.parseColor(status.equalsIgnoreCase("accepted") ? "#43A047" : "#F4511E"));
        } else if (status.equalsIgnoreCase("rejected")) {
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            holder.waiting.setVisibility(View.GONE);
            holder.stock.setVisibility(View.GONE);
            holder.myorder.setVisibility(View.GONE);
            holder.wallet.setVisibility(View.GONE);
            holder.status.setTextColor(Color.parseColor(status.equalsIgnoreCase("accepted") ? "#43A047" : "#F4511E"));
        } else if (status.equalsIgnoreCase("waiting")) {
            holder.accept.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);
            holder.waiting.setVisibility(View.GONE);
            holder.stock.setVisibility(View.GONE);
            holder.myorder.setVisibility(View.GONE);
            holder.wallet.setVisibility(View.GONE);
            holder.status.setTextColor(Color.parseColor("#FB8C00"));
        } else if (status.equalsIgnoreCase("registered")) {
            holder.accept.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);
            holder.waiting.setVisibility(View.VISIBLE);
            holder.stock.setVisibility(View.GONE);
            holder.myorder.setVisibility(View.GONE);
            holder.wallet.setVisibility(View.GONE);
            holder.status.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            holder.waiting.setVisibility(View.GONE);
            holder.stock.setVisibility(View.GONE);
            holder.wallet.setVisibility(View.GONE);
            holder.myorder.setVisibility(View.GONE);
            holder.status.setTextColor(Color.parseColor("#000000"));
        }

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStatusChanged(shop, "accepted");
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStatusChanged(shop, "rejected");
            }
        });
        holder.waiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStatusChanged(shop, "waiting");
            }
        });

        holder.stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStockClick(shop);
            }
        });

        holder.myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMyorderClick(shop);
            }
        });
        holder.wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onWalletClick(shop);
            }
        });


    }

    @Override
    public int getItemCount() {
        return shopListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    shopListFiltered = shopList;
                } else {
                    List<Shop> filteredList = new ArrayList<>();
                    for (Shop row : shopList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String val = row.getShopName();
                        if (val.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else if (row.getPhoneone().contains(charString.toLowerCase())) {
                            filteredList.add(row);

                        }
                    }

                    shopListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = shopListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                shopListFiltered = (ArrayList<Shop>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyData(List<Shop> shopList) {
        this.shopListFiltered = shopList;
        this.shopList = shopList;
        notifyDataSetChanged();
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Shop shop);

        void onStatusChanged(Shop shop, String status);

        void onDeleteClick(Shop shop);
        void onEditClick(Shop shop);
        void onStockClick(Shop shop);
        void onBannerClick(Shop shop);
        void onMyorderClick(Shop shop);
        void onWalletClick(Shop shop);

    }
}
