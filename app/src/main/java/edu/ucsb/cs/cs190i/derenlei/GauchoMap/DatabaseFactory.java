package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Zichen Sun on 2/6/2017.
 */

public class DatabaseFactory {
    static GauchomapDatabaseHelper helper = MapsActivity.helper;
    static SQLiteDatabase DB = MapsActivity.DB;
    //Database helping function
    static public String saveandgetName(String name, String url, String Event, Double Longitude, Double Latitude, String time){
        DB = helper.getWritableDatabase();
        if(is_present(url)){
            DB.execSQL("DELETE FROM Map WHERE Uri = '" + url + "'");
        }
        if(name_is_present(name)){
            DB.execSQL("DELETE FROM Map WHERE Name = '" + name + "'");
        }
        DB.execSQL("INSERT OR IGNORE INTO Map (Name,Uri,Event,Longitude,Latitude,Time) " +
                "VALUES ('"+ name+"','"+url+"','"+Event+"','"+Longitude+"','"+Latitude+"','"+time+"');");
        return getName(url);
    }

    static public String getUri(String name){
        DB = helper.getReadableDatabase();
        String url = "";
        Cursor cursor = DB.rawQuery("SELECT Uri FROM Map WHERE Map.Name = '"+name+"'",null);
        if(!name_is_present(name));
        else{
            cursor.moveToFirst();
            url =cursor.getString(0);
            while (!cursor.isAfterLast()) {
                url=cursor.getString(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return url;
    }

    static public String getName(String url){
        DB = helper.getReadableDatabase();
        String name = "";
        Cursor cursor = DB.rawQuery("SELECT Name FROM Map WHERE Map.Uri = '"+url+"'",null);
        if(!is_present(url));
        else{
            cursor.moveToFirst();
            name =cursor.getString(0);
            while (!cursor.isAfterLast()) {
                name=cursor.getString(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return name;
    }

    static public String getEvent(String url){
        DB = helper.getReadableDatabase();
        String event = "";
        Cursor cursor = DB.rawQuery("SELECT Event FROM Map WHERE Map.Uri = '"+url+"'",null);
        if(!is_present(url));
        else{
            cursor.moveToFirst();
            event =cursor.getString(0);
            while (!cursor.isAfterLast()) {
                event=cursor.getString(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return event;
    }

    static public String getTime(String url){
        DB = helper.getReadableDatabase();
        String event = "";
        Cursor cursor = DB.rawQuery("SELECT Time FROM Map WHERE Map.Uri = '"+url+"'",null);
        if(!is_present(url));
        else{
            cursor.moveToFirst();
            event =cursor.getString(0);
            while (!cursor.isAfterLast()) {
                event=cursor.getString(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return event;
    }

    static public Double getLongitude(String url){
        DB = helper.getReadableDatabase();
        Double Longitude = Double.valueOf(0);
        Cursor cursor = DB.rawQuery("SELECT Longitude FROM Map WHERE Map.Uri = '"+url+"'",null);
        if(!is_present(url));
        else
        {
            cursor.moveToFirst();
            Longitude =cursor.getDouble(0);
            while (!cursor.isAfterLast()) {
                Longitude=cursor.getDouble(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return Longitude;
    }

    static public Double getLatitude(String url){
        DB = helper.getReadableDatabase();
        Double Latitude = Double.valueOf(0);
        Cursor cursor = DB.rawQuery("SELECT Latitude FROM Map WHERE Map.Uri = '"+url+"'",null);
        if(!is_present(url));
        else{
            cursor.moveToFirst();
            Latitude =cursor.getDouble(0);
            while (!cursor.isAfterLast()) {
                Latitude=cursor.getDouble(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return Latitude;
    }
    static public boolean is_present(String url){
        DB = helper.getReadableDatabase();
        int count = 0;
        Cursor cursor = DB.rawQuery("SELECT Name FROM Map WHERE Map.Uri = '"+url+"'",null);if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                count = cursor.getCount();
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return count != 0;
    }

    static public boolean name_is_present(String name){
        DB = helper.getReadableDatabase();
        int count = 0;
        Cursor cursor = DB.rawQuery("SELECT Name FROM Map WHERE Map.Name = '"+name+"'",null);if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                count = cursor.getCount();
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return count != 0;
    }

    static public void cleanDB() {
        DB=helper.getWritableDatabase();
        DB.delete("Map", null, null);
        helper.updateDB();
    }

    static public ArrayList<String> getNamelist(){
        ArrayList<String> array = new ArrayList<>();
        DB = helper.getReadableDatabase();
        Cursor result = DB.rawQuery("SELECT Map.Name FROM Map;",null);
        result.moveToFirst();
        String a = result.getString(0);
        while (!result.isAfterLast()) {
            array.add(result.getString(0));
            result.moveToNext();
        }
        result.close();
        return array;
    }

    static void update(){
        helper.updateDB();
    }


    static void TEST(String name){
        String URI = DatabaseFactory.getUri(name);
        String event = DatabaseFactory.getEvent(URI);
        Log.d("worile_present", String.valueOf(DatabaseFactory.is_present(URI)));
        Log.d("worile_event",event);
        String Name = DatabaseFactory.getName(URI);
        Log.d("worile_name",Name);
        Double lat = DatabaseFactory.getLatitude(URI);
        Log.d("worile_latit", String.valueOf(lat));
        Double lon = DatabaseFactory.getLongitude(URI);
        Log.d("worile_longi", String.valueOf(lon));
        Log.d("worile_time",DatabaseFactory.getTime(URI));
    }
}
