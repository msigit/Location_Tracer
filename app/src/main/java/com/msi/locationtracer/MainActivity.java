package com.msi.locationtracer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.msi.locationtracer.ApplicationHelper.DirectionsJsonParser;
import com.msi.locationtracer.ApplicationHelper.LocationPublisher;
import com.msi.locationtracer.JsonParser.UserInfoJson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private GoogleMap googleMap;
    private double UserLat;
    private double UserLng;
    private double PlaceLat;
    private double PlaceLng;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String Shared_Name = "user_profile";
    private static final String device_Id = "device_Id";

    UserInfoJson userInfoJson = new UserInfoJson();


    private ArrayList<LatLng> markarpoints;

    boolean tempIndicator = true;

    FloatingActionButton flatProfile,flatLocation,flatConnection ;
    FloatingActionMenu flatMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences =getSharedPreferences(Shared_Name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        LocationPublisher locationPublisher = new LocationPublisher(MainActivity.this);
        UserLat = locationPublisher.LocationProvider().getUserLatitude();
        UserLng =locationPublisher.LocationProvider().getUserLongitude();
        PlaceLat = 23.797741;
        PlaceLng = 90.369156;

        markarpoints = new ArrayList<LatLng>();

        deviceCheck();
        LoadMap();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //TapLocation(latLng);
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    if(tempIndicator) {
//                        final String url = getDirectionsUrl(new LatLng(UserLat, UserLng), new LatLng(PlaceLat += .123, PlaceLng += 0.1234));
//                        final DownloadTask downloadTask = new DownloadTask();
//                        downloadTask.execute(url);
//                    }
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();


        flatProfile = (FloatingActionButton) findViewById(R.id.flatProfile);
        flatLocation = (FloatingActionButton) findViewById(R.id.flatLocation);
        flatConnection = (FloatingActionButton) findViewById(R.id.flatConnection);
        flatMenu = (FloatingActionMenu) findViewById(R.id.menu);

        flatProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
                flatMenu.close(true);
            }
        });

        flatConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ConnectionActivity.class);
                startActivity(intent);
                flatMenu.close(true);
            }
        });

        flatLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LocatorsActivity.class);
                startActivity(intent);
                flatMenu.close(true);
            }
        });

    }


    private void deviceCheck()
    {
        if(sharedPreferences.getString(device_Id,"").toString().isEmpty())
        {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            final String deviceID = telephonyManager.getDeviceId();


                String url = getString(R.string.local_base_url)+"info";

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try
                                {
                                    JSONObject jsonResponse = new JSONObject(response);
                                   String statCode = jsonResponse.getString("code");

                                    if(statCode.equals("200") || statCode.equals("409"))
                                    {
                                        editor.putString(device_Id, deviceID);
                                        editor.commit();
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
                        params.put("Device_ID",deviceID);
                        params.put("Status","active");
                        return params;
                    }
                };

                requestQueue.add(putRequest);
            }
    }

    public void LoadMap() {
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);


            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(UserLat, UserLng))
                    .title("My Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            // fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.terrain)))); //

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(PlaceLat, PlaceLng))
                    .title("Other Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(PlaceLat, PlaceLng)).zoom(16).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void TapLocation(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.addMarker(markerOptions);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tempIndicator = false;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJsonParser parser = new DirectionsJsonParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.BLUE);
            }

            try{
                googleMap.clear();
                googleMap.addPolyline(lineOptions);
            }
            catch (Exception e){}
            // Drawing polyline in the Google Map for the i-th route

            tempIndicator = true;
        }
    }
}
