package pro.network.bringwheeladmin.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pro.network.bringwheeladmin.R;
import pro.network.bringwheeladmin.app.Appconfig;


/**
 * Created by ravi on 16/11/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<StockList> productList;
    private List<StockList> productListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,price,stock_update,description,status;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);

            price = view.findViewById(R.id.price);
            stock_update = view.findViewById(R.id.stock_update_row);
            thumbnail = view.findViewById(R.id.thumbnail);
            description=view.findViewById(R.id.description);

            status=view.findViewById(R.id.status);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(productListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ProductAdapter(Context context, List<StockList> productList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.productList = productList;
        this.productListFiltered = productList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final StockList product = productListFiltered.get(position);
        holder.name.setText(product.getSubcategory()+" "+product.getProductName());
        holder.price.setText("₹ "+product.getPrice());
        holder.stock_update.setText(product.getTotalstock());


        ArrayList<String> urls = new Gson().fromJson(product.getImage(), (Type) List.class);
        Glide.with(context)
                .load(Appconfig.getResizedImage(urls.get(0), true))
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<StockList> filteredList = new ArrayList<>();
                    for (StockList row : productList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String val=row.getSubcategory();
                        if (val.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }else if (row.getProductName().contains(charString.toLowerCase())) {
                            filteredList.add(row);

                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<StockList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public void notifyData(List<StockList> productList) {
        this.productListFiltered=productList;
        this.productList=productList;
        notifyDataSetChanged();
    }
    public interface ContactsAdapterListener {
        void onContactSelected(StockList product);
        void onStatusChanged(StockList product, String status);

    }

}
