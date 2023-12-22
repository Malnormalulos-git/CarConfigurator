package edu.nuzp.carconfigurator;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ModelFragment extends Fragment {
    private SQLiteConnector db;
    private ListView listView;
    private MainActivity mainActivity;
//    private int choosenItemId = 0;

    private class ModelCursorAdapter extends CursorAdapter {

        public ModelCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_model, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textViewModel = view.findViewById(R.id.text_view_model);
            TextView textViewPrice = view.findViewById(R.id.text_view_price);

            String model = cursor.getString(cursor.getColumnIndexOrThrow("model"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));

            textViewModel.setText(model);
            textViewPrice.setText(price + " $");

            int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { handleItemClick(itemId); }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        listView = view.findViewById(R.id.list_view);
        ((TextView)view.findViewById(R.id.text_view_list_title)).setText("Models");

        db = new SQLiteConnector(view.getContext());
        Cursor cursor = db.getAllDataFromTable("models");

        ModelCursorAdapter  adapter = new ModelCursorAdapter (view.getContext(), cursor);

        listView.setAdapter(adapter);


        mainActivity = (MainActivity) getActivity();

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

    private void handleItemClick(int itemId) {
        Order order = mainActivity.getCurrentOrder();
        order.setModel_id(itemId);
        mainActivity.setCurrentOrder(order);

        mainActivity.setFragment(new EngineFragment());

//        choosenItemId = itemId;
//        Log.d("ModelFragment", String.valueOf(itemId));
    }
}