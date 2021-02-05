package com.example.appmaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap mapa;
    ArrayList<LatLng> puntos;
    Boolean marcar=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        puntos=new ArrayList<LatLng>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        mapa=googleMap;
        mapa.setOnMapClickListener(this);
    }

    public void cambiarVistaMapa(View view){
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }
    public void moverCamaraMapa(View view){
        CameraUpdate camUpd1= CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5);
        mapa.moveCamera(camUpd1);
    }
    public void pintarPoligono(){
        PolylineOptions lineas=new PolylineOptions();
        for (int i=0;i<puntos.size();i++) {
            lineas.add(puntos.get(i));
        }
        lineas.add(puntos.get(0));
        lineas.width(8);
        lineas.color(Color.RED);
        mapa.addPolyline(lineas);
        puntos.clear();

    }
    @Override
    public void onMapClick(LatLng point) {
        Projection proj = mapa.getProjection();
        Point coord = proj.toScreenLocation(point);

        /*Toast.makeText(
                MainActivity.this,
                "Click\n" +
                        "Lat: " + point.latitude + "\n" +
                        "Lng: " + point.longitude + "\n" +
                        "X: " + coord.x + " - Y: " + coord.y,
                Toast.LENGTH_SHORT).show();*/
        if(marcar){
                puntos.add(new LatLng(point.latitude,point.longitude));
                LatLng punto = new LatLng(point.latitude, point.longitude);
                mapa.addMarker(new MarkerOptions().position(punto).title("Puntos: "+puntos.size()));
            }
        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getPosition().latitude==puntos.get(0).latitude && marker.getPosition().longitude==puntos.get(0).longitude){
                    Toast.makeText(MainActivity.this,marker.getTitle(),Toast.LENGTH_SHORT).show();
                    pintarPoligono();
                }
                return false;
            }
        });
    }
}