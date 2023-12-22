package edu.nuzp.carconfigurator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SQLiteConnector extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "CarConfigurator.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    public SQLiteConnector(@Nullable Context context, @Nullable String name, int version)
    {
        super(context, name, null, version);
        this.context = context;
    }

    public SQLiteConnector(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE models (_id INTEGER PRIMARY KEY AUTOINCREMENT, model VARCHAR(30), price INTEGER);");
        sqLiteDatabase.execSQL("CREATE TABLE engines (_id INTEGER PRIMARY KEY AUTOINCREMENT, engine VARCHAR(30), price INTEGER);");
        sqLiteDatabase.execSQL("CREATE TABLE colors (_id INTEGER PRIMARY KEY AUTOINCREMENT, color_name VARCHAR(30), color VARCHAR(6), price INTEGER);");
        sqLiteDatabase.execSQL("CREATE TABLE configurations (_id INTEGER PRIMARY KEY AUTOINCREMENT, configuration VARCHAR(60), price INTEGER);");
        sqLiteDatabase.execSQL("CREATE TABLE orders (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "model_id INTEGER, " +
                "engine_id INTEGER, " +
                "color_id INTEGER, " +
                "configuration_id INTEGER);");

        insertInitialData(sqLiteDatabase);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Models
        insertModelData(db, "Highlander"        , 39120);
        insertModelData(db, "Grand Highlander"  , 43070);
        insertModelData(db, "bZ4X"              , 42000);
        insertModelData(db, "RAV4"              , 28475);
        insertModelData(db, "RAV4 Prime"        , 43440);
        insertModelData(db, "Corolla Cross"     , 23610);
        insertModelData(db, "4Runner"           , 40455);
        insertModelData(db, "Venza"             , 34920);
        insertModelData(db, "Sequoia"           , 60875);

        // Engines
        insertEngineData(db, "petrol"   , 0);
        insertEngineData(db, "disel"    , 2000);
        insertEngineData(db, "hybrid"   , 3000);
        insertEngineData(db, "electro"  , 3500);

        // Colors
        insertColorData(db, "COASTAL GRAY METALLIC ", "6D7A82", 0);
        insertColorData(db, "BLACK"                 , "000000", 0);
        insertColorData(db, "RUBY FLARE PEARL"      , "740000", 425);
        insertColorData(db, "TITANIUM GLOW"         , "BEB8B8", 0);
        insertColorData(db, "BLUEPRINT "            , "1E2548", 0);
        insertColorData(db, "WIND CHILL PEARL"      , "E3E9E9", 425);

        // Configurations
        insertConfigurationData(db, "All-Weather Floor Liner Package"                           , 309);
        insertConfigurationData(db, "Carpet Mat Package"                                        , 309);
        insertConfigurationData(db, "Preferred Accessory Package w/ All-Weather Floor Liners"   , 334);
        insertConfigurationData(db, "Preferred Accessory Package w/ Carpet Mats"                , 334);
    }

    private void insertModelData(SQLiteDatabase db, String model, int price) {
        ContentValues values = new ContentValues();
        values.put("model", model);
        values.put("price", price);
        db.insert("models", null, values);
    }
    private void insertEngineData(SQLiteDatabase db, String engine, int price) {
        ContentValues values = new ContentValues();
        values.put("engine", engine);
        values.put("price", price);
        db.insert("engines", null, values);
    }
    private void insertColorData(SQLiteDatabase db, String colorName, String color, int price) {
        ContentValues values = new ContentValues();
        values.put("color_name", colorName);
        values.put("color", color);
        values.put("price", price);
        db.insert("colors", null, values);
    }
    private void insertConfigurationData(SQLiteDatabase db, String configuration, int price) {
        ContentValues values = new ContentValues();
        values.put("configuration", configuration);
        values.put("price", price);
        db.insert("configurations", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS models;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS engines;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS colors;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS configurations;");
        onCreate(sqLiteDatabase);
//            if(oldV==1 && newV==2)
//            {
//                //1
//            }
//            else if(oldV==1 && newV==3)
//            {
//                //2
//            }
//            else if(oldV==2 && newV==3)
//            {
//                //3
//            }
    }
    public Order addOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert("orders",null, order.getAsContentValues());
        if(result == -1){
            Toast.makeText(context, "Insert failed", Toast.LENGTH_SHORT).show();
        }else {
            Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE _id = last_insert_rowid();", null);

            if (cursor.moveToFirst()) {
                int lastInsertedId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                order.set_id(lastInsertedId);

                Toast.makeText(context, "Inserted successfully", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        }
        return order;
    }

    public Cursor getAllDataFromTable(String tableTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableTitle, null);
        return cursor;
    }

    public Cursor getOrderConnectedData(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT orders._id as id, " +
                                        "       models.model as model, " +
                                        "       engines.engine as engine, " +
                                        "       colors.color_name as color_name, " +
                                        "       colors.color as color, " +
                                        "       configurations.configuration as configuration, " +
                                        "       SUM(models.price + engines.price + colors.price + configurations.price) AS total_price " +
                                        "FROM orders " +
                                        "INNER JOIN models ON orders.model_id = models._id " +
                                        "INNER JOIN engines ON orders.engine_id = engines._id " +
                                        "INNER JOIN colors ON orders.color_id = colors._id " +
                                        "INNER JOIN configurations ON orders.configuration_id = configurations._id " +
                                        "WHERE orders._id = " + orderId + ";"
                , null);
        return cursor;
    }
    public Cursor getOrdersIdAndTotalPrice(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT orders._id as _id, " +
                        "       SUM(models.price + engines.price + colors.price + configurations.price) AS total_price " +
                        "FROM orders " +
                        "INNER JOIN models ON orders.model_id = models._id " +
                        "INNER JOIN engines ON orders.engine_id = engines._id " +
                        "INNER JOIN colors ON orders.color_id = colors._id " +
                        "INNER JOIN configurations ON orders.configuration_id = configurations._id " +
                        "GROUP BY orders._id, models.model, engines.engine, colors.color_name, configurations.configuration;"
                , null);
        return cursor;
    }
}