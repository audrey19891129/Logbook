package com.packet.ocx_android.controllers.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.compose.ui.res.Resource;

import com.google.android.gms.maps.model.LatLng;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.connection.DirectionsController;

import java.io.IOException;
import java.util.List;

public class Maps {

    public static LatLng getLocationFromAddress(Context context, String address)throws IOException {

        address = DirectionsController.addressFormatter(address);

        Log.e("MAPS", address);

        Geocoder coder = new Geocoder(context);
        List<Address> addresses;
        LatLng latlong = null;

        addresses = coder.getFromLocationName(address, 5);
        if (addresses.size() == 0 || addresses == null) {

            Log.e("MAPS", "size = 0" );

            return null;
        }
        android.location.Address location = addresses.get(0);
        location.getLatitude();
        location.getLongitude();

        latlong = new LatLng(location.getLatitude(), location.getLongitude());
        return latlong;

    }

    public static String getAddressFromLocation(Context context, double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context);
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        return addresses.get(0).getAddressLine(0);
    }





}
