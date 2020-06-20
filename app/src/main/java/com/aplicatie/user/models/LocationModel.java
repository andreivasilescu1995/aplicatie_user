package com.aplicatie.user.models;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aplicatie.user.MainActivity;
import com.aplicatie.user.misc_objects.Constants;
import com.aplicatie.user.misc_objects.RequestQueueSingleton;
import com.aplicatie.user.controllers.SettingsFragment;
import com.aplicatie.user.controllers.location.LocationFragment;
import com.aplicatie.user.misc_objects.StaticMethods;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LocationModel {
    private final String TAG = LocationModel.class.getName();
    private LatLng phoneLocation, busLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private String selectedRoute = "5-40";

    public FusedLocationProviderClient getFusedLocationClient() {
        return fusedLocationClient;
    }

    public LocationCallback getLocationCallback() {
        return locationCallback;
    }

    public void getPhoneLocation(final LocationUpdate phoneLocationUpdate) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocationFragment.context);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null)
                    phoneLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                phoneLocationUpdate.onLocationChanged(phoneLocation);
            }
        };
        LocationRequest locationRequest = new LocationRequest().create()
                .setInterval(SettingsFragment.getUpdateInterval() * 1000)
                .setFastestInterval(SettingsFragment.getUpdateInterval() * 1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(LocationFragment.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationFragment.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(LocationFragment.context, "Oferiti permisiunile de locatie!", Toast.LENGTH_LONG).show();
        }
        else
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void getBusLocation(final LocationUpdate busLocationUpdate) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.server_ip + "/backend/getBusFromRoute", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.optJSONObject("location") == null) {
                    busLocation = null;
                }
                else {
                    busLocation = new LatLng(
                            Double.parseDouble(response.optJSONObject("location").optString("latitude")),
                            Double.parseDouble(response.optJSONObject("location").optString("longitude"))
                    );
                }
                busLocationUpdate.onLocationChanged(busLocation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                busLocationUpdate.onLocationChanged(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("route", selectedRoute);
                if (phoneLocation != null) {
                    params.put("latitude", String.valueOf(phoneLocation.latitude));
                    params.put("longitude", String.valueOf(phoneLocation.longitude));
                }
                return params;
            }
        };
        request.setTag("LocationFragment");
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(LocationFragment.context).addToRequestQueue(request);
    }

    public String getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(String selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public interface LocationUpdate {
        void onLocationChanged(LatLng location);
    }
}