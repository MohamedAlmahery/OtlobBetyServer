package com.phoenix.otlobbetyserver.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.phoenix.otlobbetyserver.Common.Common;
import com.phoenix.otlobbetyserver.Model.Token;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String toTokenRefresh = FirebaseInstanceId.getInstance().getToken();
        if(Common.currentUser != null) // لو حصل حاجة أحذف ده
            updateTokenToFirebase(toTokenRefresh);
    }

    private void updateTokenToFirebase(String toTokenRefresh) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Token");
        Token token = new Token(toTokenRefresh,true); //false because this token from Client App
        tokens.child(Common.currentUser.getPhone()).setValue(token);
    }
}
