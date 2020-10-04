package space.tatun.mapapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
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

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoint = new ArrayList<>();
    }

// Эта активити для пробежки, мы фиксируем 2 точки(старт и финиш)
//Узнаем расстояние и бежим
//что бы поставить точку нужно нажать и удерживать

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (listPoint.size() == 2) {
                    listPoint.clear();
                    mMap.clear();
                }
                listPoint.add(latLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                if (listPoint.size() == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    markerOptions.title("Старт");
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    markerOptions.title("Финиш");
                }
                mMap.addMarker(markerOptions);
                if (listPoint.size() == 2) {
                    String url = getRequestUrl(listPoint.get(0), listPoint.get(1));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);
                }
            }
        });
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String param = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line);

            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();


        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }


    //зачем это вообще? но тут лучше не трогать
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }


    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

              TaskParser taskParser = new TaskParser();
              taskParser.execute(s);

        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
//                Toast.makeText(getApplicationContext(), "Parsed Direction is : " +routes, Toast.LENGTH_SHORT).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast.makeText(getApplicationContext(), "Get Direction Clicked !" + routes, Toast.LENGTH_SHORT).show();
            return routes;

        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
          /*  ArrayList points = null;
            PolylineOptions polylineOptions = null;


            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path ) {

                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
                }
            if(polylineOptions != null){
                mMap.addPolyline(polylineOptions);
            }
            else {
                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
            }

           */

            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            if (lists != null){
//
//                Toast.makeText(getApplicationContext(),"List value :" +lists, Toast.LENGTH_SHORT);
                for (List<HashMap<String, String>> path : lists){
                    points = new ArrayList();
                    polylineOptions = new PolylineOptions();

                    if (path !=null){

                        String lat="";
                        String lon="";

                        for (HashMap<String, String> point : path ){

                            lat = point.get("lat");
                            lon = point.get("lng");

                            if(lat != null && lon != null){
                                points.add(new LatLng(Double.parseDouble(lat),Double.parseDouble(lon)));
                            }
                            else{
                                continue;
                            }
//
//                                LatLng ll= new LatLng()
//                            double def=Double.valueOf(lat);
//                            double lon = Double.parseDouble(point.get("lng"));
//                             points.add(new LatLng(Double.parseDouble(point.get("lat")),Double.parseDouble(point.get("lng"))));
//                            Toast.makeText(getApplicationContext(),"path from class" + lat, Toast.LENGTH_SHORT).show();
                        }
//                        double abc=Double.parseDouble(lat);

//                        Toast.makeText(getApplicationContext(),"path from class" + lat + "and lob :" +lon, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"path Empty", Toast.LENGTH_SHORT).show();
                    }

//                      points.add(new LatLng(10.0159,76.3419));
//                        points.add(new LatLng(9.7138,76.6829));
//                    points.add(new LatLng(55.008224,-56.8984527));
//                    points.add(new LatLng(65.008224,-46.8984527));
//                    points.add(new LatLng(75.008224,-36.8984527));
//                    points.add(new LatLng(85.008224,-26.8984527));
//                    points.add(new LatLng(95.008224,-16.8984527));
                    polylineOptions.addAll(points);
                    polylineOptions.width(15);
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.geodesic(true);
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "List Variable is null", Toast.LENGTH_SHORT).show();
            }
            if (polylineOptions != null){
                mMap.addPolyline(polylineOptions);
            }
            else{
                Toast.makeText(getApplicationContext(),"Directions Not Found !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
