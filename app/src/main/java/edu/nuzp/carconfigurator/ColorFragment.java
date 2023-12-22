package edu.nuzp.carconfigurator;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ColorFragment extends Fragment {
    private SQLiteConnector db;
    private ListView listView;

    private class ModelCursorAdapter extends CursorAdapter {
        public ModelCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_color, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textViewColor = view.findViewById(R.id.text_view_color);
            View viewColor = view.findViewById(R.id.view_color);
            TextView textViewPrice = view.findViewById(R.id.text_view_price);

            String colorName = cursor.getString(cursor.getColumnIndexOrThrow("color_name"));
            String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));

            textViewColor.setText(colorName);
            try {
                viewColor.setBackgroundColor(Color.parseColor("#" + color));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            textViewPrice.setText(price + " $");

            int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleItemClick(itemId);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        listView = view.findViewById(R.id.list_view);
        ((TextView)view.findViewById(R.id.text_view_list_title)).setText("Colors");

        db = new SQLiteConnector(view.getContext());
        Cursor cursor = db.getAllDataFromTable("colors");

        ModelCursorAdapter  adapter = new ModelCursorAdapter (view.getContext(), cursor);

        listView.setAdapter(adapter);

        return view;
    }

    private void handleItemClick(int itemId) {
        MainActivity mainActivity = (MainActivity) getActivity();

        Order order = mainActivity.getCurrentOrder();
        order.setColor_id(itemId);

        mainActivity.setCurrentOrder(order);
        mainActivity.setFragment(new ConfigurationFragment());

//        Log.d("ColorFragment", String.valueOf(itemId));
    }
}