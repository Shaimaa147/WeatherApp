package com.example.albadr.weatherapp.locationDB;

public class Constants {

    //COLUMNS
    static final String ROW_ID = "id";
    static final String LOCATIONNAME = "name";
    static final String LAT = "lat";
    static final String LNG = "lng";


    //DB PROPERTIES
    static final String DB_NAME = "weather_DB";
    static final String TB_NAME = "bookmarks_TB";
    static final int DB_VERSION = 1;

    //CREATE TB STMT
    static final String CREATE_TB = "CREATE TABLE bookmarks_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL, lat TEXT NOT NULL, lng TEXT NOT NULL);";


    //DROP TB STMT
    static final String DROP_TB = "DROP TABLE IF EXISTS " + TB_NAME;

}
