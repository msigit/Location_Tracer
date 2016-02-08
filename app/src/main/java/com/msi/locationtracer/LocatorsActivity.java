package com.msi.locationtracer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msi.locationtracer.Adapters.LocatorListAdapter;
import com.msi.locationtracer.Data_Model.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sahid_000 on 2/2/2016.
 */
public class LocatorsActivity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String Shared_Name = "user_profile";
    private static final String device_Id = "device_Id";
    private static final String user_Name = "user_Name";


    private ListView listView;
    public ArrayList<UserInfo> jsonUserInfoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locators_list);
        setTitle("Peoples");

        sharedPreferences =getSharedPreferences(Shared_Name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listView = (ListView) findViewById(R.id.placelist);
        LoadLocatorList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(LocatorsActivity.this, MainActivity.class);
                intent.putExtra("guest_device_Id", jsonUserInfoArrayList.get(position).getDeviceID());
                intent.putExtra("guest_user_Name", jsonUserInfoArrayList.get(position).getUserName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }

    private void LoadLocatorList()
    {
        String url = getString(R.string.local_base_url)+"guest_list/"+sharedPreferences.getString(device_Id,"")+"";

        RequestQueue requestQueue = Volley.newRequestQueue(LocatorsActivity.this);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            String statCode = jsonResponse.getString("code");

                            if(statCode.equals("200"))
                            {
                                JSONArray data = jsonResponse.getJSONArray("data");

                                for(int i = 0; i < data.length();i++ )
                                {
                                    JSONObject Data = data.getJSONObject(i);
                                    UserInfo  userInfo = new UserInfo();

                                    userInfo.setUserName(Data.getString("User_name"));
                                    userInfo.setDeviceID(Data.getString("Device_ID"));
                                    userInfo.setPhoneNumber(Data.getString("Phone_number"));
                                    userInfo.setApiStatus(Data.getString("Status"));

                                    jsonUserInfoArrayList.add(userInfo);
                                }

                                LocatorListAdapter locatorListAdapter = new LocatorListAdapter(LocatorsActivity.this, jsonUserInfoArrayList);
                                listView.setAdapter(locatorListAdapter);
                            }
                            else if(statCode.equals("404"))
                            {

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
}
