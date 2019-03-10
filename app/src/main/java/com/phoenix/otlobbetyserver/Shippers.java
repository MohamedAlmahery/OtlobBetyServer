package com.phoenix.otlobbetyserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenix.otlobbetyserver.Common.Common;
import com.phoenix.otlobbetyserver.Model.Push;
import com.phoenix.otlobbetyserver.Model.Request;
import com.phoenix.otlobbetyserver.ViewHolder.OrderViewHolder;
import com.phoenix.otlobbetyserver.ViewHolder.ShippersViewHolder;

import static com.phoenix.otlobbetyserver.Common.Common.convertCodeToStatus;

public class Shippers extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Push, ShippersViewHolder> adapter;


    FirebaseDatabase db;
    DatabaseReference push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shippers);

        db = FirebaseDatabase.getInstance();
        push = db.getReference("Push");

        recyclerView = (RecyclerView) findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        loadShippersOrders();
    }

    private void loadShippersOrders() {
        adapter = new FirebaseRecyclerAdapter<Push, ShippersViewHolder>(
                Push.class,
                R.layout.shippers_layout,
                ShippersViewHolder.class,
                push
        ) {
            @Override
            protected void populateViewHolder(ShippersViewHolder shippersViewHolder, Push model, int position) {
                shippersViewHolder.txtOrderID.setText(adapter.getRef(position).getKey());
                shippersViewHolder.txtOrderID.setText(model.getIdnum());
                shippersViewHolder.txtShipperId.setText(model.getName());
                shippersViewHolder.txtShipperName.setText(model.getPhone());

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
            }

}
