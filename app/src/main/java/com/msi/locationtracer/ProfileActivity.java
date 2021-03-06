package com.msi.locationtracer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
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
import com.msi.locationtracer.JsonParser.UserInfoJson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sahid_000 on 1/27/2016.
 */
public class ProfileActivity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String Shared_Name = "user_profile";
    private static final String device_Id = "device_Id";
    private static final String user_Name = "user_Name";
    private static final String user_Phone = "user_Phone";

    Button saveProfile,editProfile;
    EditText editName,editPhone;
    TextView textName,textPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        setTitle("Profile");

        sharedPreferences =getSharedPreferences(Shared_Name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        saveProfile = (Button) findViewById(R.id.saveProfile);
        editProfile = (Button) findViewById(R.id.editProfile);
        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        textName = (TextView) findViewById(R.id.textName);
        textPhone = (TextView) findViewById(R.id.textPhone);

        profileSet();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             editName.setText(sharedPreferences.getString(user_Name, "").toString());
             editPhone.setText(sharedPreferences.getString(user_Phone, "").toString());
             editAction();
            }
        });


        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileSave();
            }
        });
    }

    private void profileSave() {

        final String newName = editName.getText().toString().trim();
        final String newPhone = editPhone.getText().toString().trim();

        if(newName.isEmpty())
        {
            editAction();
            Toast nametoast = Toast.makeText(this, "Write a name", Toast.LENGTH_LONG);
            TextView nv = (TextView) nametoast.getView().findViewById(android.R.id.message);
            nv.setTextColor(Color.WHITE);nametoast.show();
        }

       else if( newPhone.length() < 11)
        {
            editAction();
            Toast phonetoast = Toast.makeText(this, "Phone number must be 11 digit", Toast.LENGTH_LONG);
            TextView pv = (TextView) phonetoast.getView().findViewById(android.R.id.message);
            pv.setTextColor(Color.WHITE);phonetoast.show();
        }

        if(!newName.isEmpty() && !newPhone.isEmpty() && newPhone.length() == 11)
        {
            String url = getString(R.string.local_base_url)+"info/"+sharedPreferences.getString(device_Id,"")+"";

            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try
                            {
                                JSONObject jsonResponse = new JSONObject(response);
                                String statCode = jsonResponse.getString("code");

                                if(statCode.equals("200"))
                                {
                                    editor.putString(user_Name, newName);
                                    editor.putString(user_Phone, newPhone);
                                    editor.commit();

                                    saveAction();
                                }
                                else if(statCode.equals("404"))
                                {
                                    Toast nametoast = Toast.makeText(ProfileActivity.this, "Something wrong occured", Toast.LENGTH_LONG);
                                    TextView nv = (TextView) nametoast.getView().findViewById(android.R.id.message);
                                    nv.setTextColor(Color.WHITE);nametoast.show();
                                }
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
                    params.put("User_name",newName);
                    params.put("Phone_number",newPhone);
                    params.put("Status","active");
                    return params;
                }
            };
            requestQueue.add(postRequest);
        }
    }

    private void profileSet() {
        String url = getString(R.string.local_base_url)+"info/"+sharedPreferences.getString(device_Id,"")+"";

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String statCode = jsonResponse.getString("code");

                            if(statCode.equals("200")) {
                                JSONObject Data = jsonResponse.getJSONObject("data");
                                String userName = Data.getString("User_name");
                                String userPhone = Data.getString("Phone_number");

                                if(!userPhone.isEmpty() && !userPhone.equals(null)) {
                                    editor.putString(user_Name, userName);
                                    editor.putString(user_Phone, userPhone);
                                    editor.commit();
                                    saveAction();
                                }
                               else {
                                    editor.putString(user_Name, "");
                                    editor.putString(user_Phone, "");
                                    editor.commit();
                                    editAction();
                                }
                            }

                           else if(statCode.equals("404")) {
                            }
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

    private void saveAction() {
        editName.setVisibility(View.GONE);
        editPhone.setVisibility(View.GONE);
        saveProfile.setVisibility(View.GONE);
        textName.setVisibility(View.VISIBLE);
        textPhone.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.VISIBLE);

        textName.setText(sharedPreferences.getString(user_Name, "").toString());
        textPhone.setText(sharedPreferences.getString(user_Phone, "").toString());
    }

    private void editAction() {
        textName.setVisibility(View.GONE);
        textPhone.setVisibility(View.GONE);
        editProfile.setVisibility(View.GONE);
        editName.setVisibility(View.VISIBLE);
        editPhone.setVisibility(View.VISIBLE);
        saveProfile.setVisibility(View.VISIBLE);
    }
}
