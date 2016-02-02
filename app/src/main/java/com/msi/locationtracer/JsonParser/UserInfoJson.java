package com.msi.locationtracer.JsonParser;

import android.app.Activity;
import android.app.DownloadManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msi.locationtracer.Data_Model.UserInfo;
import com.msi.locationtracer.MainActivity;
import com.msi.locationtracer.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sahid_000 on 1/26/2016.
 */
public class UserInfoJson {

   static String resCode;

    public void UserInfoGet(Activity activity,String deviceID)
    {
        String url = activity.getString(R.string.local_base_url)+"info/"+deviceID+"";

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        UserInfo  userInfo = new UserInfo();
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            userInfo.setApiStatus(jsonResponse.getString("code"));

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(getRequest);
    }



    public static UserInfo UserInfoPut(Activity activity,final String deviceID, final String userName, final String phoneNumber, final String status)
    {
        String url = activity.getString(R.string.local_base_url)+"info";
        final UserInfo userInfo = new UserInfo();

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.e("Check",response);
                            resCode = jsonResponse.getString("code");
                        }

                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String,String>();
                params.put("Device_ID",deviceID);
                params.put("User_name",userName);
                params.put("Phone_number",phoneNumber);
                params.put("Status",status);
                return params;
            }
        };


        requestQueue.add(putRequest);

        return userInfo;
    }



    public void UserInfoPost(Activity activity,String deviceID,final String phoneNumber, final String userName, final String status)
    {
        String url = activity.getString(R.string.local_base_url)+"info/"+deviceID+"";

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        UserInfo  userInfo = new UserInfo();

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            userInfo.setApiStatus(jsonResponse.getString("code"));

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Error",error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String,String>();
                params.put("User_name",userName);
                params.put("Phone_number",phoneNumber);
                params.put("Status",status);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }


    public void UserInfoDelete(Activity activity,String deviceID)
    {
        String url = activity.getString(R.string.local_base_url)+"info/"+deviceID+"";

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Response",response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Error",error.toString());
                    }
                }
        );
        requestQueue.add(deleteRequest);
    }
}
