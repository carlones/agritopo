package br.com.neogis.agritopo.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;

import java.util.List;

import br.com.neogis.agritopo.utils.IGPSListener;

import static android.content.Context.LOCATION_SERVICE;
import static br.com.neogis.agritopo.utils.Constantes.MY_PERMISSIONS_ACCESS_COARSE_LOCATION;
import static br.com.neogis.agritopo.utils.Constantes.MY_PERMISSIONS_ACCESS_FINE_LOCATION;

/**
 * Created by carlo on 03/01/2018.
 */

public class MyGpsMyLocationProvider extends GpsMyLocationProvider {
    private Context context;
    private Activity activity;
    private IGPSListener listener;

    public MyGpsMyLocationProvider(Context context, Activity activity, IGPSListener listener) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public Location getLastKnownLocation() {
        Location location = super.getLastKnownLocation();
        if (location == null) {
            LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
                    }
                }
                location = mLocationManager.getLastKnownLocation(provider);
                if (location == null) {
                    continue;
                }
                if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = location;
                }
            }
            location = bestLocation;
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        if( listener != null ) {
            listener.onLocationChanged(location);
        }
    }

    @Override
    public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {
        boolean didIt = super.startLocationProvider(myLocationConsumer);
        if( didIt && listener != null ) {
            listener.startLocationProvider();
        }
        return didIt;
    }

    @Override
    public void stopLocationProvider() {
        super.stopLocationProvider();
        if( listener != null ) {
            listener.stopLocationProvider();
        }
    }
}
