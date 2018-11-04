package com.example.albadr.weatherapp.locationDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.albadr.weatherapp.model.PlaceInfo;

public class DBAdapter {

    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public DBAdapter(Context c) {
        this.c = c;
        helper= DBHelper.getInstance(c);
    }

    //OPEN CON
    public void openDB()
    {
        try
        {
            db=helper.getWritableDatabase();
        }catch (SQLException e)
        {

        }
    }
    //CLOSE DB
    public void closeDB()
    {
        try
        {
            helper.close();
        }catch (SQLException e)
        {

        }
    }

    //SAVE
    public boolean add(PlaceInfo placeInfo)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(Constants.LOCATIONNAME,placeInfo.getName());
            cv.put(Constants.LAT,String.valueOf(placeInfo.getLatlng().latitude));
            cv.put(Constants.LNG,String.valueOf(placeInfo.getLatlng().longitude));

            long result=db.insert(Constants.TB_NAME,null,cv);
            if(result>0)
            {
                return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    //SELECT
    public Cursor retrieve()
    {
        String[] columns={Constants.ROW_ID,Constants.LOCATIONNAME,Constants.LAT,Constants.LNG};

        Cursor c=db.query(Constants.TB_NAME,columns,null,null,null,null,null);
        return c;
    }

    //UPDATE/edit
    public boolean update(String newName,int id)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(Constants.LOCATIONNAME,newName);
            cv.put(Constants.LAT,newName);
            cv.put(Constants.LNG,newName);


            int result=db.update(Constants.TB_NAME,cv, Constants.ROW_ID + " =?", new String[]{String.valueOf(id)});
            if(result>0)
            {
                return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;

    }

    //DELETE/REMOVE
    public boolean delete(int id)
    {
        try
        {
            int result=db.delete(Constants.TB_NAME,Constants.ROW_ID+" =?",new String[]{String.valueOf(id)});
            if(result>0)
            {
                return true;
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


}
