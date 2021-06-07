package pro.network.bringwheeladmin.shop;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pro.network.bringwheeladmin.R;


public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {

    ImageClick imageClick;
    private final Context mainActivityUser;
    private ArrayList<String> samplesbean;
    boolean isEdit;
    int selectedPosition = 0;

    public SizeAdapter(Context mainActivityUser, ArrayList<String> samplesbean, ImageClick imageClick, boolean isEdit) {
        this.mainActivityUser = mainActivityUser;
        this.samplesbean = samplesbean;
        this.imageClick = imageClick;
        this.isEdit = isEdit;
    }

    public void notifyData(ArrayList<String> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.samplesbean = myList;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.size_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String samples = samplesbean.get(position);
        holder.name.setText(samples);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onDeleteClick(position);
            }
        });
        if (isEdit) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }

        if (!isEdit && selectedPosition == position) {
            holder.sizeLinear.setBackgroundResource(R.drawable.rectangle_box_select);
            holder.name.setTextColor(Color.WHITE);
        } else {
            holder.sizeLinear.setBackgroundResource(R.drawable.rectangle_box);
            holder.name.setTextColor(mainActivityUser.getResources().getColor(R.color.colorPrimary));
        }

        holder.sizeLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(position);
            }
        });

    }

    public int getItemCount() {
        return samplesbean.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        LinearLayout delete, sizeLinear;

        public MyViewHolder(View view) {
            super((view));
            name = view.findViewById(R.id.name);
            delete = view.findViewById(R.id.delete);
            sizeLinear = view.findViewById(R.id.sizeLinear);

        }
    }

}


