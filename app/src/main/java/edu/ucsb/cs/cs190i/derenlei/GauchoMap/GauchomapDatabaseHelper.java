package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deren Lei on 6/2/2017.
 */

public class GauchomapDatabaseHelper extends SQLiteOpenHelper {
    private static final String CreateTable = "CREATE TABLE IF NOT EXISTS Map( Name text PRIMARY KEY, " +
            "Uri text NOT NULL UNIQUE, " +
            "Event text NOT NULL, " +
            "Longitude decimal(18,5) NOT NULL, " +
            "Latitude decimal(18,5) NOT NULL, " +
            "Time text NOT NULL);";
    private static final String DatabaseName = "GauchomapDatabase.db";
    private static GauchomapDatabaseHelper Instance;
    private List<OnDatabaseChangeListener> Listeners;

    private GauchomapDatabaseHelper(Context context) {
        super(context, DatabaseName, null, 1);
        Listeners = new ArrayList<>();
    }

    public static void Initialize(Context context) {
        Instance = new GauchomapDatabaseHelper(context);
    }

    public static GauchomapDatabaseHelper GetInstance() {
        return Instance;
    }

    public void Subscribe(OnDatabaseChangeListener listener) {
        Listeners.add(listener);
    }

    private boolean TryUpdate(Cursor cursor) {
        try {
            cursor.moveToFirst();
        } catch (SQLiteConstraintException exception) {
            return false;
        } finally {
            cursor.close();
        }
        NotifyListeners();
        return true;
    }

    public void updateDB(){
        NotifyListeners();
    }

    private void NotifyListeners() {
        for (OnDatabaseChangeListener listener : Listeners) {
            listener.OnDatabaseChange();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public interface OnDatabaseChangeListener {
        void OnDatabaseChange();
    }

}
