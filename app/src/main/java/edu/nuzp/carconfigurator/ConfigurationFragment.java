package edu.nuzp.carconfigurator;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ConfigurationFragment extends Fragment {
    private SQLiteConnector db;
    private ListView listView;

    private class ModelCursorAdapter extends CursorAdapter {
        public ModelCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_configuration, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textViewConfiguration = view.findViewById(R.id.text_view_configuration);
            TextView textViewPrice = view.findViewById(R.id.text_view_price);

            String configuration = cursor.getString(cursor.getColumnIndexOrThrow("configuration"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));

            textViewConfiguration.setText(configuration);
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
        ((TextView)view.findViewById(R.id.text_view_list_title)).setText("Configurations");

        db = new SQLiteConnector(view.getContext());
        Cursor cursor = db.getAllDataFromTable("configurations");

        ModelCursorAdapter  adapter = new ModelCursorAdapter (view.getContext(), cursor);

        listView.setAdapter(adapter);

        return view;
    }

    private void handleItemClick(int itemId) {
        MainActivity mainActivity = (MainActivity) getActivity();

        Order order = mainActivity.getCurrentOrder();
        order.setConfiguration_id(itemId);

        order = mainActivity.setCurrentOrder(order);
        Log.d("ConfigurationFragment", String.valueOf(order.get_id()));
        mainActivity.setFragment(new DisplayOrderFragment(order.get_id()));

//        Log.d("ConfigurationFragment", String.valueOf(itemId));
    }
}