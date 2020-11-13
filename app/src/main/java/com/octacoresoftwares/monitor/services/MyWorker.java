package com.octacoresoftwares.monitor.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.octacoresoftwares.monitor.R;
import com.octacoresoftwares.monitor.callback.DeviceCallback;
import com.octacoresoftwares.monitor.model.Device;
import com.octacoresoftwares.monitor.model.Response;
import com.octacoresoftwares.monitor.network.NetworkService;
import com.octacoresoftwares.monitor.repo.Repository;
import com.octacoresoftwares.monitor.utils.DeviceUtils;

import java.util.Locale;

public class MyWorker extends Worker implements DeviceCallback {

    private static final String TAG = MyWorker.class.getSimpleName();
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private Location mLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private Context mContext;
    private LocationCallback mLocationCallback;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Work started");
        getDeviceLocation();
        return Result.success();
    }

    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();
                            Device.Location loc = new Device.Location();
                            loc.setLongitude(String.valueOf(mLocation.getLongitude()));
                            loc.setLatitude(String.valueOf(mLocation.getLatitude()));
                            uploadDeviceInfo(loc);
                        } else {
                            Log.e(TAG, "Failed to get location.");
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void uploadDeviceInfo(Device.Location location) {
        Device device = new Device();
        DeviceUtils deviceUtils = new DeviceUtils(getApplicationContext());
        device.setAppVersion(String.valueOf(deviceUtils.getAppVersion()));
        device.setBatteryLevel(String.valueOf(deviceUtils.getBatteryPercentage()));
        device.setConnectivityStatus(deviceUtils.getConnectionStatus().name().toLowerCase(Locale.getDefault()));
        device.setDataUsageCollectionTime(deviceUtils.getCurrentTime());
        device.setLocation(location);
        device.setNetworkType(deviceUtils.getNetworkType().name().toLowerCase());
        device.setNumberOfApps(deviceUtils.getNumberOfInstalledApps());
        device.setHasApp(deviceUtils.isGoogleMapsInstalled());
        device.setTerminalId("000000");
        device.setSignalStrength(0);

        Repository repo = new Repository(NetworkService.createService(), this);
        repo.updateDeviceInfo(device);
    }

    private void buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            String description = mContext.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.app_name), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void buildSuccessNotification() {
        buildNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.app_name))
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Upload Device Information")
                .setContentText("Device Information Uploaded successfully")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(1001, builder.build());
    }

    private void buildErrorNotification() {
        buildNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.app_name))
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Upload Device Information")
                .setContentText("Failed to upload device information")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(1001, builder.build());
    }

    @Override
    public void success(Response response) {
        Log.i(TAG, "Uploaded successfully");
        buildSuccessNotification();
    }

    @Override
    public void error(Object e) {
        Log.e(TAG, "Failed to upload");
        buildErrorNotification();
    }
}
