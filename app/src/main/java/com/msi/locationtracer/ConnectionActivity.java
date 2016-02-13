package com.msi.locationtracer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msi.locationtracer.Data_Model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sahid_000 on 2/1/2016.
 */
public class ConnectionActivity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String Shared_Name = "user_profile";
    private static final String device_Id = "device_Id";
    private static final String user_Name = "user_Name";
    private static final String user_Phone = "user_Phone";

    Button connectProfile;
    EditText connectPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_connect);
        setTitle("Connect People");

        sharedPreferences =getSharedPreferences(Shared_Name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        connectPhone = (EditText) findViewById(R.id.connectPhone);
        connectProfile = (Button) findViewById(R.id.connectProfile);

        connectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectPeople();
            }
        });
    }

    private void ConnectPeople() {
        if (sharedPreferences.getString(user_Phone, "").isEmpty() || sharedPreferences.getString(user_Phone, "").equals(null)) {
            Toast phonetoast = Toast.makeText(ConnectionActivity.this, "Please set your profile to connect others", Toast.LENGTH_LONG);
            TextView pv = (TextView) phonetoast.getView().findViewById(android.R.id.message);
            pv.setTextColor(Color.WHITE);
            phonetoast.show();
        }
        else if (!sharedPreferences.getString(user_Phone, "").isEmpty() || !sharedPreferences.getString(user_Phone, "").equals(null)) {
            String connectionPhone = connectPhone.getText().toString().trim();

//        if( connectionPhone.length() < 11)
//        {
//            Toast phonetoast = Toast.makeText(ConnectionActivity.this, "Phone number must be 11 digit", Toast.LENGTH_LONG);
//            TextView pv = (TextView) phonetoast.getView().findViewById(android.R.id.message);
//            pv.setTextColor(Color.WHITE);phonetoast.show();
//        }
            if(connectionPhone.equals(sharedPreferences.getString(user_Phone,"")))
            {
                Toast phonetoast = Toast.makeText(ConnectionActivity.this, "Please avoid intentional attempt", Toast.LENGTH_LONG);
                TextView pv = (TextView) phonetoast.getView().findViewById(android.R.id.message);
                pv.setTextColor(Color.WHITE);phonetoast.show();
            }
            else if (!connectionPhone.isEmpty())// && connectionPhone.length() == 11)
            {
                String url = getString(R.string.local_base_url) + "info_by_phone/" + connectionPhone + "";

                RequestQueue requestQueue = Volley.newRequestQueue(ConnectionActivity.this);
                StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String statCode = jsonResponse.getString("code");

                                    if (statCode.equals("404")) {
                                        Toast phonetoast = Toast.makeText(ConnectionActivity.this, "This phone number didn't used yet", Toast.LENGTH_LONG);
                                        TextView pv = (TextView) phonetoast.getView().findViewById(android.R.id.message);
                                        pv.setTextColor(Color.WHITE);
                                        phonetoast.show();
                                    } else if (statCode.equals("200")) {
                                        JSONObject jsonData = jsonResponse.getJSONObject("data");
                                        final String connectDevice = jsonData.getString("Device_ID");
                                        final String connectName = jsonData.getString("User_name");

                                        String url = getString(R.string.local_base_url) + "connection/" + sharedPreferences.getString(device_Id, "") + "/" + connectDevice + "";

                                        RequestQueue requestQueue = Volley.newRequestQueue(ConnectionActivity.this);
                                        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonResponse = new JSONObject(response);
                                                            String statCode = jsonResponse.getString("code");

                                                            if (statCode.equals("200")) {
                                                                Toast phonetoast = Toast.makeText(ConnectionActivity.this, "You already connected with " + connectName, Toast.LENGTH_LONG);
                                                                TextView pv = (TextView) phonetoast.getView().findViewById(android.R.id.message);
                                                                pv.setTextColor(Color.GREEN);
                                                                phonetoast.show();
                                                            } else if (statCode.equals("404")) {
                                                                String url = getString(R.string.local_base_url) + "connection";

                                                                RequestQueue requestQueue = Volley.newRequestQueue(ConnectionActivity.this);
                                                                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {

                                                                                try {
                                                                                    JSONObject jsonResponse = new JSONObject(response);
                                                                                    String statCode = jsonResponse.getString("code");

                                                                                    if (statCode.equals("200")) {
                                                                                        Toast phonetoast = Toast.makeText(ConnectionActivity.this, "You connected with " + connectName, Toast.LENGTH_LONG);
                                                                                        TextView pv = (TextView) phonetoast.getView().findViewById(android.R.id.message);
                                                                                        pv.setTextColor(Color.GREEN);
                                                                                        phonetoast.show();
                                                                                    }
                                                                                } catch (Exception e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        },
                                                                        new Response.ErrorListener() {
                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {

                                                                                Log.e("connection Put", error.toString());

                                                                            }
                                                                        }
                                                                ) {
                                                                    @Override
                                                                    protected Map<String, String> getParams() {
                                                                        Map<String, String> params = new HashMap<String, String>();
                                                                        params.put("Host_Device_ID", sharedPreferences.getString(device_Id, ""));
                                                                        params.put("Guest_Device_ID", connectDevice);
                                                                        params.put("Status", "connected");
                                                                        return params;
                                                                    }
                                                                };

                                                                requestQueue.add(putRequest);

                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("connection Get", error.toString());
                                                    }
                                                });
                                        requestQueue.add(getRequest);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    String errorResponse = new String(error.networkResponse.data);
                                    JSONObject jsonResponse = new JSONObject(errorResponse);

                                    Log.e("response", jsonResponse.toString());
                                } catch (Exception e) {

                                }
                            }
                        });
                requestQueue.add(getRequest);
            }
        }
    }
}
