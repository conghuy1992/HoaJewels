package com.hoa.jewels._class;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hoa.jewels.interfaces.LoginCallback;

import org.json.JSONObject;

/**
 * Created by Huy on 28/5/2017.
 */

public class Const {
    public static final String TAG = "Const";
    public static final String KEY_BARCODE = "KEY_BARCODE";
    public static final String Success = "Success";
    public static final int ON_BACK = 99;


    public static String get_role_userchange(int UID) {
        return "http://hoajewels.monamedia.net/HoaJewelsService.asmx/get_role_userchange?UID=" + UID;
    }

    public static String Login(String username, String password) {
        return "http://hoajewels.monamedia.net/HoaJewelsService.asmx/Login?username=" +
                username +
                "&pass=" +
                password;
    }

    public static String Get_Order_info(String qrcode, int UID) {
        return "http://hoajewels.monamedia.net/HoaJewelsService.asmx/Get_Order_info?qrcode=" +
                qrcode +
                "&UID=" +
                UID;
    }

    public static String update_order(String OrderID, int UID) {
        return "http://hoajewels.monamedia.net/HoaJewelsService.asmx/update_order?OrderID=" +
                OrderID +
                "&UID=" +
                UID;
    }

    public static String update_order_role(String OrderID, int UID, String username_change) {
        return "http://hoajewels.monamedia.net/HoaJewelsService.asmx/update_order_role?OrderID=" +
                OrderID +
                "&UID=" +
                UID +
                "&username_change=" +
                username_change;
    }

    public static void actionGetOrderInfor(Context context, String qrcode, int UID
            , final LoginCallback callback) {
        String url = Get_Order_info(qrcode, UID);
        Log.d(TAG, "actionGetOrderInfor:" + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                url, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
//                        Log.d(TAG, response.toString());
                        String s = response.toString();
                        if (s == null) s = "";
                        callback.onSuccess(s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error.Response");
                        callback.onFail();
                    }
                }
        );
        queue.add(getRequest);
    }

    public static void CallAPI(Context context, String url,
                               final LoginCallback callback) {

        Log.d(TAG, "CallAPI:" + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                url, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
//                        Log.d(TAG, response.toString());
                        String s = response.toString();
                        if (s == null) s = "";
                        callback.onSuccess(s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error.Response");
                        callback.onFail();
                    }
                }
        );
        queue.add(getRequest);
    }

    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
