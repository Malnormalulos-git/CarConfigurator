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

public class EngineFragment extends Fragment {
    private SQLiteConnector db;
    private ListView listView;

    private class ModelCursorAdapter extends CursorAdapter {
        public ModelCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_engine, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textViewEngine = view.findViewById(R.id.text_view_engine);
            TextView textViewPrice = view.findViewById(R.id.text_view_price);

            String engine = cursor.getString(cursor.getColumnIndexOrThrow("engine"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));

            textViewEngine.setText(engine);
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
        ((TextView)view.findViewById(R.id.text_view_list_title)).setText("Engines");

        db = new SQLiteConnector(view.getContext());
        Cursor cursor = db.getAllDataFromTable("engines");

        ModelCursorAdapter adapter = new ModelCursorAdapter (view.getContext(), cursor);

        listView.setAdapter(adapter);

        return view;
    }

    private void handleItemClick(int itemId) {
        MainActivity mainActivity = (MainActivity) getActivity();

        Order order = mainActivity.getCurrentOrder();
        order.setEngine_id(itemId);

        mainActivity.setCurrentOrder(order);
        mainActivity.setFragment(new ColorFragment());

//        Log.d("EngineFragment", String.valueOf(itemId));
    }
}