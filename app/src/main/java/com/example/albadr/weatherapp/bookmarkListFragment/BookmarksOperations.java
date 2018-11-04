package com.example.albadr.weatherapp.bookmarkListFragment;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.albadr.weatherapp.locationDB.DBAdapter;
import com.example.albadr.weatherapp.model.PlaceInfo;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class BookmarksOperations {

    private static final String TAG = "BookmarksOperations";
    private static BookmarksFragment bookmarksFragment;

    public static boolean saveLocation(PlaceInfo placeInfo,Context context) {
        DBAdapter db = new DBAdapter(context);
        db.openDB();
        boolean saved = db.add(placeInfo);

        if (saved) {
            Log.d(TAG, "SaveLOcation: Loaction saved successfully");
            bookmarksFragment.updateBookmarkList();
            return true;
        } else {
            Toast.makeText(context, "Unable To Save", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static  ArrayList<PlaceInfo> getBookmarks(Context context) {

        ArrayList<PlaceInfo> bookmarks = new ArrayList<>();
        DBAdapter db=new DBAdapter(context);
        db.openDB();
        Cursor c=db.retrieve();
        PlaceInfo place=null;

        while (c.moveToNext())
        {
            int id=c.getInt(0);
            String name=c.getString(1);
            String lat=c.getString(2);
            String lng=c.getString(3);

            place=new PlaceInfo();
            place.setId(id);
            place.setName(name);
            place.setLatlng(new LatLng(Double.parseDouble(lat),Double.parseDouble(lng)));
            Log.d(TAG, "getBookmarks: "+ place.getId() +" "+place.getName()+" " + place.getLatlng().latitude
                    + " " + place.getLatlng().longitude);

            bookmarks.add(place);
        }

        db.closeDB();
        return bookmarks;

    }

    public static void setBookmarksFragment(BookmarksFragment bookmarksFragment) {
        BookmarksOperations.bookmarksFragment = bookmarksFragment;
    }

    public static boolean delete(int id,Context context)
    {
        //DELETE FROM DB
        DBAdapter db=new DBAdapter(context);
        db.openDB();
        boolean deleted=db.delete(id);
        db.closeDB();

        if(deleted)
        {
            return  true;
        }else {
            return false;
        }
    }
}
