package pro.network.bringwheeladmin.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.repsly.library.timelineview.LineType;
import com.repsly.library.timelineview.TimelineView;

import java.util.List;

import pro.network.bringwheeladmin.R;


/**
 * Adapter for RecyclerView with TimelineView
 */

class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private final int orientation;
    private final List<TrackOrder> items;

    TimelineAdapter(int orientation, List<TrackOrder> items) {
        this.orientation = orientation;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.track_order_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getStatus());
        holder.tvAddress.setText(items.get(position).getDescription());
        holder.timelineView.setLineType(getLineType(position));
        holder.timelineView.setNumber(position + 1);
        holder.tv_created.setText(items.get(position).createdon);
        // Make first and last markers stroked, others filled
//        if (position == 0 || position + 1 == getItemCount()) {
//            holder.timelineView.setFillMarker(false);
//        } else {
        holder.timelineView.setFillMarker(true);
//        }
//
//        if (position == 4) {
//            holder.timelineView.setDrawable(AppCompatResources
//                                                    .getDrawable(holder.timelineView.getContext(),
//                                                                 R.drawable.ic_checked));
//        } else {
//            holder.timelineView.setDrawable(null);
//        }
//
//        // Set every third item active
//        holder.timelineView.setActive(position % 3 == 2);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private LineType getLineType(int position) {
        if (getItemCount() == 1) {
            return LineType.ONLYONE;

        } else {
            if (position == 0) {
                return LineType.BEGIN;

            } else if (position == getItemCount() - 1) {
                return LineType.END;

            } else {
                return LineType.NORMAL;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView tvName;
        TextView tvAddress, tv_created;

        ViewHolder(View view) {
            super(view);
            timelineView = (TimelineView) view.findViewById(R.id.timeline);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tv_created = view.findViewById(R.id.tv_created);

        }
    }

}