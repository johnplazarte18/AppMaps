package com.example.appmaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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
    String[] datos;
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
        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    if(marker.getPosition().latitude==puntos.get(0).latitude && marker.getPosition().longitude==puntos.get(0).longitude){
                        Toast.makeText(MainActivity.this,marker.getTitle(),Toast.LENGTH_SHORT).show();
                        pintarPoligono();
                    }
                }catch (Exception ex){

                }

                return false;

            }
        });
    }

    public void VistaSatelite(View view){
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }
    public void VistaNormal(View view){
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }
    public void VistaHybrida(View view){
        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }
    public void VistaTopográfico(View view){
        mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    public void moverCamaraMapa(View view){
        CameraUpdate camUpd1= CameraUpdateFactory.newLatLngZoom(new LatLng(-1.0429872, -79.8352822), 10);
        mapa.moveCamera(camUpd1);
    }

    public void animarCamaraMapa(View view){
        LatLng madrid = new LatLng(mapa.getCameraPosition().target.latitude, mapa.getCameraPosition().target.longitude);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(madrid)
                .zoom(19)
                .bearing(45)      //noreste arriba
                .tilt(70)         //punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);

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
        puntos.add(new LatLng(point.latitude,point.longitude));
        LatLng punto = new LatLng(point.latitude, point.longitude);
        mapa.addMarker(new MarkerOptions().position(punto).title("Puntos: "+puntos.size()));

    }
}