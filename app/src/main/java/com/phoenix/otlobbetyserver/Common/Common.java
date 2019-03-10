package com.phoenix.otlobbetyserver.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.phoenix.otlobbetyserver.Model.Request;
import com.phoenix.otlobbetyserver.Model.User;
import com.phoenix.otlobbetyserver.Remote.APIService;
import com.phoenix.otlobbetyserver.Remote.FCMRetrofitClient;
import com.phoenix.otlobbetyserver.Remote.RetrofitClient;

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    public static String UPDATE = "Update";
    public static String DELETE = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService()
    {
        return FCMRetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
    public static String convertCodeToStatus (String code){
        if (code.equals("0"))
            return "Placed";
        else
            return "Accepted";
    }
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }


}
