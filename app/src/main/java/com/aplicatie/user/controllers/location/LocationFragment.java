package com.aplicatie.user.controllers.location;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aplicatie.user.R;
import com.aplicatie.user.misc_objects.Constants;
import com.aplicatie.user.misc_objects.RequestQueueSingleton;
import com.aplicatie.user.misc_objects.Routes;
import com.aplicatie.user.misc_objects.StaticMethods;
import com.aplicatie.user.models.LocationModel;
import com.aplicatie.user.controllers.SettingsFragment;
import com.aplicatie.user.views.LocationView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private final String TAG = LocationFragment.class.getName();
    private LocationModel model;
    private LocationView view;
    public static Context context;
    private ActionBar toolbar;
    private Timer bus_location_timer;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_routes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        view.setRoute_5_40(menu.findItem(R.id.route_5_40));
        view.setRoute_test(menu.findItem(R.id.route_test));

        model.setSelectedRoute("5-40");

        view.getRoute_5_40().setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!view.getRoute_5_40().isChecked()) view.getRoute_5_40().setChecked(true);
                if (view.getRoute_test().isChecked()) view.getRoute_test().setChecked(false);
                model.setSelectedRoute("5-40");
                view.getPolyline().remove();
                view.setPolyline(view.getMap().addPolyline(Routes.route_5_40_pline));

                if (view.getBusMarker() != null) {
                    view.getBusMarker().remove();
                    view.setBusMarker(null);
                    RequestQueueSingleton.getInstance(LocationFragment.context).getRequestQueue().cancelAll("LocationFragment");
                    updateBusLocation();
                }

                return false;
            }
        });

        view.getRoute_test().setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!view.getRoute_test().isChecked()) view.getRoute_test().setChecked(true);
                if (view.getRoute_5_40().isChecked()) view.getRoute_5_40().setChecked(false);
                model.setSelectedRoute("route_test");
                view.getPolyline().remove();
                view.setPolyline(view.getMap().addPolyline(Routes.route_test_pline));

                if (view.getBusMarker() != null) {
                    view.getBusMarker().remove();
                    view.setBusMarker(null);
                    RequestQueueSingleton.getInstance(LocationFragment.context).getRequestQueue().cancelAll("LocationFragment");
                    updateBusLocation();
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (context == null) context = requireActivity().getApplicationContext();
        if (model == null) model = new LocationModel();
        if (view == null) view = new LocationView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        toolbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        view.initViews(v);
        view.getMapView().onCreate(savedInstanceState);
        view.getMapView().getMapAsync(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(getResources().getString(R.string.menu_maps));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        view.setMap(googleMap);
        updatePhoneLocation();
        updateBusLocation();
        view.setPolyline(view.getMap().addPolyline(Routes.route_5_40_pline));
    }

    private void updatePhoneLocation() {
        LocationModel.LocationUpdate phoneLocationUpdate = new LocationModel.LocationUpdate() {
            @Override
            public void onLocationChanged(LatLng location) {
                Log.e(TAG, " PHONE LOCATION UPDATE!" + " " + location.latitude + " " + location.longitude);
                if (view.getPhoneMarker() == null)
                    view.setPhoneMarker(view.getMap().addMarker(new MarkerOptions().title("Locatia telefonului").position(location).icon(StaticMethods.getBitmapFromVector(requireContext(), R.drawable.ic_location_black_24dp, Color.RED))));
                else
                    view.getPhoneMarker().setPosition(location);
                updateMapCamera();
            }
        };
        model.getPhoneLocation(phoneLocationUpdate);
    }

    private void updateBusLocation() {
        final LocationModel.LocationUpdate busLocationUpdate = new LocationModel.LocationUpdate() {
            @Override
            public void onLocationChanged(LatLng location) {
                if (location != null) {
                    if (view.getBusMarker() == null)
                        view.setBusMarker(view.getMap().addMarker(new MarkerOptions().title("Locatia autobuzului").position(location).icon(StaticMethods.getBitmapFromVector(requireContext(), R.drawable.ic_directions_bus_black_24dp, Color.BLUE))));
                    else
                        view.getBusMarker().setPosition(location);
                    StaticMethods.dismissErrorDialog((AppCompatActivity) requireActivity());
                    Log.e(TAG,"BUS LOCATION UPDATE: " + location.latitude + ", " + location.longitude);
                }
                else {
                    if (view.getBusMarker() == null) {
                        Log.e(TAG, "Nici un autobuz online pe ruta " + model.getSelectedRoute() + "!");
//                        bus_location_timer.cancel();
                        if (LocationFragment.this.isAdded())
                            StaticMethods.showErrorDialog((AppCompatActivity) requireActivity(), "Eroare update locatie autobuz", "Nici un autobuz online pe ruta " + model.getSelectedRoute() + "!");
                    }
                }
                updateMapCamera();
            }
        };
        if (bus_location_timer != null) {
            bus_location_timer.cancel();
            bus_location_timer.purge();
        }
        bus_location_timer = new Timer();
        bus_location_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.getBusLocation(busLocationUpdate);
            }
        }, 100, SettingsFragment.getUpdateInterval() * 1000);
    }

    private void updateMapCamera() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (view.getBusMarker() != null && view.getPhoneMarker() != null) {
            builder.include(view.getPhoneMarker().getPosition());
            builder.include(view.getBusMarker().getPosition());
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, Constants.camera_padding);
            view.getMap().animateCamera(cu);
        }
        else {
            if (view.getBusMarker() != null && view.getPhoneMarker() == null) {
                builder.include(view.getBusMarker().getPosition());
                Constants.camera_padding = 150;
            }
            else if (view.getPhoneMarker() != null) {
                view.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(view.getPhoneMarker().getPosition(),17));
            }
        }
//        builder.include(view.getPolyline().getPoints().get(0));
//        builder.include(view.getPolyline().getPoints().get(view.getPolyline().getPoints().size() / 2));
    }

    @Override
    public void onPause() {
        super.onPause();
        view.getMapView().onPause();
        view.setBusMarker(null);
        if (model.getFusedLocationClient() != null)
            model.getFusedLocationClient().removeLocationUpdates(model.getLocationCallback());
        if (bus_location_timer != null)
            bus_location_timer.cancel();
//        context = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view.getMapView().onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus_location_timer.cancel();
        bus_location_timer.purge();
    }

    @Override
    public void onResume() {
        super.onResume();
        view.getMapView().onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        view.getMapView().onLowMemory();
    }
}
