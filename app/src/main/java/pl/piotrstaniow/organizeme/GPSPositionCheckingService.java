package pl.piotrstaniow.organizeme;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import pl.piotrstaniow.organizeme.DatabaseUtils.LocalDbHelper;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;
import pl.piotrstaniow.organizeme.Models.Task;

public class GPSPositionCheckingService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleApiClient mGoogleApiClient;

    public GPSPositionCheckingService() {
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onCreate() {
        buildGoogleApiClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private void checkTasks(Location mLastLocation) {
        LocalDbHelper.createInstance(this);
        LocalQueryManager.getInstance().openWritable();
        List<Task> taskList = LocalQueryManager.getInstance().getAllTasks();
        LocalQueryManager.getInstance().close();

        Location taskLocation = new Location(LocationManager.GPS_PROVIDER);
        for (Task t : taskList) {
            if (!t.isLocationNotify() || t.getLocation() == null) {
                continue;
            }
            taskLocation.setLatitude(t.getLocation().latitude);
            taskLocation.setLongitude(t.getLocation().longitude);
            Log.d("ORGANIZME", "" + mLastLocation.distanceTo(taskLocation));
            if (mLastLocation.distanceTo(taskLocation) < t.getLocationPrecision()) {
                Intent i = new Intent();
                i.setClass(this, TasksActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
        this.stopSelf();
    }

    @Override
    public void onLocationChanged(Location location) {
        checkTasks(location);
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);
        return mLocationRequest;
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, createLocationRequest(), this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
}
