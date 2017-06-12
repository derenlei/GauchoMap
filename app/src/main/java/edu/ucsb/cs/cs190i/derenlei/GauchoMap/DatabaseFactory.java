package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by Zichen Sun on 2/6/2017.
 */

public class DatabaseFactory {
    static GauchomapDatabaseHelper helper = MapsActivity.helper;
    static SQLiteDatabase DB = MapsActivity.DB;
    //Database helping function
    //NEED
    static public String saveandgetName(int user, String name, String url, String Event, Double Longitude, Double Latitude, String time){
        DB = helper.getWritableDatabase();
        int Interest = 0;
        if(is_present(url)){
            Interest = getInterest(url);
            DB.execSQL("DELETE FROM Map WHERE Uri = '" + url + "'");
        }
        if(name_is_present(name)){
            Interest = getInterest(url);
            DB.execSQL("DELETE FROM Map WHERE Name = '" + name + "'");
        }
        DB.execSQL("INSERT OR IGNORE INTO Map (User,Name,Uri,Event,Longitude,Latitude,Time,Interest) " +
                "VALUES ('" + user +"','"+ name+ "','"+url+"','"+Event+"','"+Longitude+"','"+Latitude+"','"+time+"','"+Interest+"');");
        return getName(url);
    }
    //need
    static public void addInterest(String name,int number){
        String url = getUri(name);
        DB = helper.getWritableDatabase();
        int Interest = getInterest(url);
        Interest = Interest+number;
        DB.execSQL("UPDATE Map SET Interest='"+Interest+"'WHERE Uri = '" + url + "'");
    }

    //NEED
    static public int getUser(String name){
        String url = getUri(name);
        DB = helper.getReadableDatabase();
        int User = 0;
        Cursor cursor = DB.rawQuery("SELECT User FROM Map WHERE Map.Uri = '"+url+"'",null);
        if(!is_present(url));
        else{
            cursor.moveToFirst();
            User = cursor.getInt(0);
            while (!cursor.isAfterLast()) {
                User=cursor.getInt(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return User;
    }

    static public int getUserByUri(String url){
        DB = helper.getReadableDatabase();
        int User = 0;
        Cursor cursor = DB.rawQuery("SELECT User FROM Map WHERE Map.Uri = '"+url+"'",null);
        if(!is_present(url));
        else{
            cursor.moveToFirst();
            User = cursor.getInt(0);
            while (!cursor.isAfterLast()) {
                User=cursor.getInt(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return User;
    }
    //NEED
    static public int getInterest(String url){
        DB = helper.getReadableDatabase();
        int Interest = 0;
        Cursor cursor = DB.rawQuery("SELECT Interest FROM Map WHERE Map.Uri = '"+url+"'",null);
        if(!is_present(url));
        else{
            cursor.moveToFirst();
            Interest = cursor.getInt(0);
            while (!cursor.isAfterLast()) {
                Interest=cursor.getInt(0);
                // do something useful with these
                cursor.moveToNext();
            }
            //int result = cursor.getInt(0);
            cursor.close();
        }
        return Interest;
    }

    static public ArrayList<String> getNamelistOfUser(int User){
        ArrayList<String> array = new ArrayList<>();
        DB = helper.getReadableDatabase();
        Cursor result = DB.rawQuery("SELECT Map.Name FROM Map WHERE Map.User = '"+User+"'",null);
        result.moveToFirst();
        String a = result.getString(0);
        while (!result.isAfterLast()) {
            array.add(result.getString(0));
            result.moveToNext();
        }
        result.close();
        return array;
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

    static public void deletebyname(String name){
        DB=helper.getWritableDatabase();
        DB.execSQL("DELETE FROM Map WHERE Name = '" + name + "'");
    }


    static public String getNamebyloc(Double log, Double alt){
        DB = helper.getReadableDatabase();
        String name = "";
        Cursor cursor = DB.rawQuery("SELECT Name FROM Map WHERE Map.Longitude = '"+log+"' AND Map.Latitude = '"+alt+"'",null);
        if(!loc_is_present(log,alt));
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

    static public boolean is_empty(){
        DB = helper.getReadableDatabase();
        int count = 0;
        Cursor cursor = DB.rawQuery("SELECT NAME FROM Map",null);
        cursor.moveToFirst();
        do{
            count = cursor.getCount();
            // do something useful with these
            cursor.moveToNext();
        }while(cursor.moveToNext());
        //int result = cursor.getInt(0);
        cursor.close();
        return count == 0;
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

    static public boolean loc_is_present(Double log, Double alt){
        DB = helper.getReadableDatabase();
        int count = 0;
        Cursor cursor = DB.rawQuery("SELECT Name FROM Map WHERE Map.Longitude = '"+log+"' AND Map.Latitude = '"+alt+"'",null);if(cursor.moveToFirst()) {
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
