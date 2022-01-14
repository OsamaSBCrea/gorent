package com.example.gorent.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoRentSQLiteOpenHelper extends SQLiteOpenHelper {

    private final SQLiteDatabaseManager SQLiteDatabaseManager;

    public GoRentSQLiteOpenHelper(Context context, String name, int version, SQLiteDatabaseManager SQLiteDatabaseManager) {
        super(context, name, null, version);
        this.SQLiteDatabaseManager = SQLiteDatabaseManager;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.SQLiteDatabaseManager.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
