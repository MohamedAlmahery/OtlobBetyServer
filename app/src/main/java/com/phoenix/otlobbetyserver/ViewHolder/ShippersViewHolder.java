package com.phoenix.otlobbetyserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.phoenix.otlobbetyserver.R;

import androidx.annotation.NonNull;

public class ShippersViewHolder extends RecyclerView.ViewHolder  {

    public TextView txtShipperId,txtShipperName,txtOrderID;
    public Button btnRemove,btnDetail;


    public ShippersViewHolder(@NonNull View itemView) {
        super(itemView);

        txtShipperId = (TextView) itemView.findViewById(R.id.order_Shipper_ID);
        txtShipperName = (TextView) itemView.findViewById(R.id.order_Shipper_Name);
        txtOrderID = (TextView) itemView.findViewById(R.id.order_ID);

        btnRemove = (Button)itemView.findViewById(R.id.btnRemoveOrder);

    }
}
