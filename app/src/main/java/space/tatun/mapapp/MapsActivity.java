package space.tatun.mapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    private List<LatLng> places;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);     //запускает картографический сервис
        places = new ArrayList<>();


        places.add(new LatLng(54.990176, 73.364898));
        places.add(new LatLng(55.000091, 73.353171));
        places.add(new LatLng(55.006947, 73.344655));
        places.add(new LatLng(55.000048, 73.353128));
        places.add(new LatLng(54.989334, 73.365962));

    }


    //добавленный метод
    public void drawDirect(List<LatLng> points) {
        mMap.clear();

        PolylineOptions line = new PolylineOptions();

        for (int i = 0; i < points.size(); i++) {
            if (i == points.size() - 1) {
                MarkerOptions endMarkerOptions = new MarkerOptions()
                        .position(points.get(i));
                mMap.addMarker(endMarkerOptions);
            }
            line.add(points.get(i));


        }
        line.geodesic(true);
        line.width(8).color(Color.GREEN);
        mMap.addPolyline(line);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // buildGoogleClient();
        
        mMap.getUiSettings().setZoomControlsEnabled(true); //контроль приближение и отдаления
        mMap.setMyLocationEnabled(true);  /// наше местоположение

        //добавили маркер
        MarkerOptions[] markers = new MarkerOptions[places.size()];
        for (int i = 0; i < places.size(); i++) {

            markers[i] = new MarkerOptions()
                    .position(places.get(i));
            googleMap.addMarker(markers[i]);
        }

        drawDirect(places);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       locationRequest = new LocationRequest();
       locationRequest.setInterval(1000);
       locationRequest.setFastestInterval(1000);
       locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));  //приблжение камеры  к точке
    }


    //приближает на наше местоположение если находимся за пределами
    protected synchronized void  buildGoogleClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
               // .addConnectionCallbacks(this)  //постоянно приближает нас к нашему местоположению
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect(); //ЧУСТАНАВЛИВАЕМ МАРКЕР НА НАШЕ МЕСТОПОЛОЖЕНИЕ
    }


}
