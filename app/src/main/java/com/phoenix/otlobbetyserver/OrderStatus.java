package com.phoenix.otlobbetyserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phoenix.otlobbetyserver.Common.Common;
import com.phoenix.otlobbetyserver.Interface.ItemClickListener;
import com.phoenix.otlobbetyserver.Model.MyResponse;
import com.phoenix.otlobbetyserver.Model.Notification;
import com.phoenix.otlobbetyserver.Model.Request;
import com.phoenix.otlobbetyserver.Model.Sender;
import com.phoenix.otlobbetyserver.Model.Token;
import com.phoenix.otlobbetyserver.Remote.APIService;
import com.phoenix.otlobbetyserver.ViewHolder.OrderViewHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phoenix.otlobbetyserver.Common.Common.convertCodeToStatus;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;


    FirebaseDatabase db;
    DatabaseReference requests;

    MaterialSpinner spinner;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        //Init Service
        mService = Common.getFCMService();

        recyclerView = (RecyclerView) findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        loadOrders();
    }


    private void loadOrders() {
            adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                    Request.class,
                    R.layout.layout_order,
                    OrderViewHolder.class,
                    requests
            ) {
                @Override
                protected void populateViewHolder(OrderViewHolder orderViewHolder, final Request request, final int position) {
                    orderViewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                    orderViewHolder.txtOrderStatus.setText(convertCodeToStatus(request.getStatus()));
                    orderViewHolder.txtOrderAddress.setText(request.getAddress());
                    orderViewHolder.txtOrderPhone.setText(request.getPhone());

                    //New event Button
                    orderViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showUpdateDialog(adapter.getRef(position).getKey(),
                                    adapter.getItem(position));
                        }
                    });
                    orderViewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOrder(adapter.getRef(position).getKey());
                        }
                    });
                    orderViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent orderDetail = new Intent (OrderStatus.this,OrderDetail.class);
                            Common.currentRequest = request;
                            orderDetail.putExtra("OrderId",adapter.getRef(position).getKey());
                            startActivity(orderDetail);
                        }
                    });

                }
            };
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }


    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_show,null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","Accepted");

        alertDialog.setView(view);

        final String localKey = key;

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                requests.child(localKey).setValue(item);
                adapter.notifyDataSetChanged(); // Add to Alert

                sendOrderStatusToUser(localKey, item);



            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }

    private void sendOrderStatusToUser(final String key, Request item) {
        DatabaseReference tokens = db.getReference("Token");
        tokens.orderByKey().equalTo(item.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                        {
                            Token token = postSnapShot.getValue(Token.class);

                            //Make raw payload
                            Notification notification = new Notification("OtlobBetyServer","Your Order "+key+" was updated");
                            Sender content = new Sender(token.getToken(),notification);

                            mService.sendNotification(content)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                            if (response.body().success ==1) //  هنا في مشكه مش عارفها
                                            {
                                                Toast.makeText(OrderStatus.this, "Order was Updated !", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Toast.makeText(OrderStatus.this, "Order was Updated but failed to send notification !", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR",t.getMessage());
                                        }
                                    });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

