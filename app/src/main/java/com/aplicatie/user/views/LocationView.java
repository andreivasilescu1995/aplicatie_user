package com.aplicatie.user.views;

import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;

import com.aplicatie.user.R;
import com.aplicatie.user.misc_objects.Routes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class LocationView {
    private MapView mapView;
    private GoogleMap map;
    private Marker phoneMarker;
    private Marker busMarker;
    private MenuItem route_5_40;
    private MenuItem route_test;
    private Polyline polyline;

    public void initViews(View v) {
        mapView = v.findViewById(R.id.mapView);
    }

    public MapView getMapView() {
        return mapView;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public Marker getPhoneMarker() {
        return phoneMarker;
    }

    public void setPhoneMarker(Marker phoneMarker) {
        this.phoneMarker = phoneMarker;
    }

    public Marker getBusMarker() {
        return busMarker;
    }

    public void setBusMarker(Marker busMarker) {
        this.busMarker = busMarker;
    }

    public MenuItem getRoute_5_40() {
        return route_5_40;
    }

    public MenuItem getRoute_test() {
        return route_test;
    }

    public void setRoute_5_40(MenuItem route_5_40) {
        this.route_5_40 = route_5_40;
    }

    public void setRoute_test(MenuItem route_test) {
        this.route_test = route_test;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }
}
