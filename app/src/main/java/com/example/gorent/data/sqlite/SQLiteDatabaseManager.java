package com.example.gorent.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.gorent.data.sqlite.entities.SQLiteTableName;
import com.example.gorent.data.sqlite.entities.SQLiteCountryField;

public class SQLiteDatabaseManager {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "GoRentDB";

    private static final String CREATE_TABLE_COUNTRY = "CREATE TABLE IF NOT EXISTS "
            + SQLiteTableName.COUNTRY + "(" + SQLiteCountryField.ID + " INTEGER PRIMARY KEY, "
            + SQLiteCountryField.NAME + " VARCHAR(255), " + SQLiteCountryField.COUNTRY_CODE + " VARCHAR(255));";
    private static SQLiteDatabaseManager SQLiteDatabaseManager;

    private final GoRentSQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static SQLiteDatabaseManager getInstance(Context context) {
        if (SQLiteDatabaseManager == null) {
            SQLiteDatabaseManager = new SQLiteDatabaseManager(context, DB_NAME, DB_VERSION);
        }
        return SQLiteDatabaseManager;
    }

    public SQLiteDatabaseManager(Context context, String name, int version) {
        sqLiteOpenHelper = new GoRentSQLiteOpenHelper(context.getApplicationContext(), name, version, this);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(CREATE_TABLE_COUNTRY);
    }

    public SQLiteDatabase getDatabase() {
        return this.sqLiteDatabase;
    }

    public void open() {
        if (this.sqLiteDatabase == null) {
            this.sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        }
    }

    public boolean close() {
        if (this.sqLiteDatabase != null) {
            if (this.sqLiteDatabase.inTransaction()) {
                this.sqLiteDatabase.endTransaction();
            }

            if (this.sqLiteDatabase.isOpen()) {
                this.sqLiteDatabase.close();
            }
            this.sqLiteDatabase = null;
            return true;
        }

        return false;
    }

    public void onCreate(SQLiteDatabase database) {

    }

}
