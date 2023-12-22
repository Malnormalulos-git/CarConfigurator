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

public class OrderListFragment extends Fragment {
    private SQLiteConnector db;
    private ListView listView;
    MainActivity mainActivity;

    private class OrdersCursorAdapter extends CursorAdapter {
        public OrdersCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_order, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textViewOrder = view.findViewById(R.id.text_view_order);
            TextView textViewPrice = view.findViewById(R.id.text_view_total_price);

            int order = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("total_price"));

            textViewOrder.setText("Order№" + order);
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
        ((TextView)view.findViewById(R.id.text_view_list_title)).setText("Orders");

        db = new SQLiteConnector(view.getContext());
        Cursor cursor = db.getOrdersIdAndTotalPrice();

        OrdersCursorAdapter adapter = new OrdersCursorAdapter(view.getContext(), cursor);

        listView.setAdapter(adapter);

        mainActivity = (MainActivity) getActivity();

        AppCompatButton btnBack = mainActivity.getBtnBack();
        btnBack.setEnabled(false);
        btnBack.setBackgroundResource(R.color.gray);
        btnBack.setText("Back to orders");

        AppCompatButton btnNext = mainActivity.getBtnNext();
        btnNext.setEnabled(true);
        btnNext.setBackgroundResource(R.color.ua_yellow);
        btnNext.setText("New order");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setFragment(new ModelFragment());
            }
        });

        return view;
    }

    private void handleItemClick(int itemId) {
        mainActivity.setFragment(new DisplayOrderFragment(itemId));
//        Log.d("OrdersListFragment", String.valueOf(itemId));
    }
}