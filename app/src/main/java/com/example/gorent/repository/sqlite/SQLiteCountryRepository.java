package com.example.gorent.repository.sqlite;

import android.database.Cursor;

import com.example.gorent.data.enums.Currency;
import com.example.gorent.data.models.locale.Country;
import com.example.gorent.data.sqlite.SQLiteDatabaseManager;
import com.example.gorent.data.sqlite.entities.SQLiteCountryField;
import com.example.gorent.data.sqlite.entities.SQLiteTableName;

import java.util.ArrayList;
import java.util.List;

public class SQLiteCountryRepository {
    
    private static SQLiteCountryRepository instance;

    private final SQLiteDatabaseManager sqLiteDatabaseManager;

    public static SQLiteCountryRepository getInstance(SQLiteDatabaseManager sqLiteDatabaseManager) {
        if (instance == null) {
            instance = new SQLiteCountryRepository(sqLiteDatabaseManager);
        }
        return instance;
    }
    
    private SQLiteCountryRepository(SQLiteDatabaseManager sqLiteDatabaseManager) {
        this.sqLiteDatabaseManager = sqLiteDatabaseManager;
    }

    public void addCountry(Country country) {
        this.sqLiteDatabaseManager.open();
        String addCountrySql = "INSERT INTO " + SQLiteTableName.COUNTRY.getLabel() + " (" + SQLiteCountryField.ID.getLabel()
                + ", " + SQLiteCountryField.NAME.getLabel() + ", " + SQLiteCountryField.COUNTRY_CODE.getLabel() + ") VALUES("
                + country.getId().toString() + ", \"" + country.getName() + "\", \"" + country.getCountryCode() + "\");";
        this.sqLiteDatabaseManager.getDatabase().execSQL(addCountrySql);
        this.sqLiteDatabaseManager.close();
    }

    public List<Country> getCountries() {
        List<Country> result = new ArrayList<>();
        this.sqLiteDatabaseManager.open();
        String getCountriesSql = "SELECT * FROM " + SQLiteTableName.COUNTRY.getLabel();
        Cursor cursor = this.sqLiteDatabaseManager.getDatabase().rawQuery(getCountriesSql, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new Country(
                        (long) cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Currency.NIS
                ));
            } while(cursor.moveToNext());
        }
        cursor.close();
        this.sqLiteDatabaseManager.close();
        return result;
    }
}
