package edu.nuzp.carconfigurator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//    private SQLiteConnector connector;
//    private SQLiteConnector db;
    private Cursor cursor;

    public AppCompatButton getBtnBack() {
        return btnBack;
    }

    public AppCompatButton getBtnNext() {
        return btnNext;
    }

    private AppCompatButton btnBack, btnNext;

    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBack = findViewById(R.id.button_back);
        btnNext = findViewById(R.id.button_next);

        currentOrder = new Order();

        setFragment(new OrderListFragment());


    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public Order setCurrentOrder(Order order) {
        currentOrder = order;
        if (currentOrder.isCompleted()){
            SQLiteConnector db = new SQLiteConnector(this);
            currentOrder = db.addOrder(currentOrder);
            Log.d("MainActivity", String.valueOf(order.get_id()));
        }
        return currentOrder;
    }
}