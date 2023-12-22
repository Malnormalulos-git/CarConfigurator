package edu.nuzp.carconfigurator;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DisplayOrderFragment extends Fragment {
    private SQLiteConnector db;
    private int orderId;
    public DisplayOrderFragment(int orderId) {
        this.orderId = orderId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_order, container, false);

        db = new SQLiteConnector(view.getContext());
        Cursor cursor = db.getOrderConnectedData(orderId);

        if (cursor.moveToFirst()) {
            ((TextView)view.findViewById(R.id.text_view_order)) .setText("Order №" + orderId);
            ((TextView)view.findViewById(R.id.text_view_model)) .setText("Model: " + cursor.getString(cursor.getColumnIndexOrThrow("model")));
            ((TextView)view.findViewById(R.id.text_view_engine)).setText("Engine: " + cursor.getString(cursor.getColumnIndexOrThrow("engine")));
            ((TextView)view.findViewById(R.id.text_view_color)) .setText("Color: " + cursor.getString(cursor.getColumnIndexOrThrow("color_name")));

            String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));
            try {
                ((View)view.findViewById(R.id.view_color)).setBackgroundColor(Color.parseColor("#" + color));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            ((TextView)view.findViewById(R.id.text_view_configuration)).setText("Configuration: " + cursor.getString(cursor.getColumnIndexOrThrow("configuration")));
            ((TextView)view.findViewById(R.id.text_view_total_price))  .setText(cursor.getInt(cursor.getColumnIndexOrThrow("total_price")) + "$");
        } else {
            Log.e("DisplayOrderFragment", "Cursor is empty");
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        AppCompatButton btnBack = mainActivity.getBtnBack();
        btnBack.setEnabled(true);
        btnBack.setBackgroundResource(R.color.ua_yellow);
        btnBack.setText("Back to orders");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setFragment(new OrderListFragment());
            }
        });

        AppCompatButton btnNext = mainActivity.getBtnNext();
        btnNext.setEnabled(false);
        btnNext.setBackgroundResource(R.color.gray);

        return view;
    }
}