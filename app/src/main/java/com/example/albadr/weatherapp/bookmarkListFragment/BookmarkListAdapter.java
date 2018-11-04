package com.example.albadr.weatherapp.bookmarkListFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.albadr.weatherapp.CityPopActivity;
import com.example.albadr.weatherapp.R;
import com.example.albadr.weatherapp.model.PlaceInfo;

import java.util.List;

public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.MyViewHolder> {

    private Context context;
    private List<PlaceInfo> bookmarkList;

    private static final String TAG = "BookmarkListAdapter";
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, lat, lng;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            lat = view.findViewById(R.id.lat);
            lng = view.findViewById(R.id.lng);
//            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

        }

    }


    public BookmarkListAdapter(Context context, List<PlaceInfo> bookmarkList) {
        this.context = context;
        this.bookmarkList = bookmarkList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookmark_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PlaceInfo item = bookmarkList.get(position);
        holder.name.setText(item.getName());
        holder.lat.setText("lat: "+item.getLatlng().latitude);
        holder.lng.setText("lng: "+item.getLatlng().longitude);
        holder.viewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked on: " + bookmarkList.get(position));

                Toast.makeText(context, "clicked position:" + position+" "+ bookmarkList.get(position).getId()
                        , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, CityPopActivity.class);
                intent.putExtra("lat", String.valueOf(bookmarkList.get(position).getLatlng().latitude));
                intent.putExtra("lng", String.valueOf(bookmarkList.get(position).getLatlng().longitude));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public void removeItem(int position) {
        if(BookmarksOperations.delete(bookmarkList.get(position).getId(),context)) {
            bookmarkList.remove(position);

            // notify the item removed by position
            // to perform recycler view delete animations
            // NOTE: don't call notifyDataSetChanged()
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());

        } else
            Toast.makeText(context,"Unable To Delete location",Toast.LENGTH_SHORT).show();

    }

//    public void restoreItem(PlaceInfo item, int position) {
//        if (BookmarksOperations.saveLocation(item, context)) {
//            bookmarkList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        } else
//            Toast.makeText(context,"Unable To restore location",Toast.LENGTH_SHORT).show();
//    }


}
