package com.octacoresoftwares.monitor.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

import com.octacoresoftwares.monitor.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.BATTERY_SERVICE;

public class DeviceUtils{
    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public DeviceUtils(Context context) {
        this.context = context;
        initConnManager();
        initNetworkInfo();
    }

    private void initConnManager() {
        if (connectivityManager == null)
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private void initNetworkInfo() {
        if (networkInfo == null)
            networkInfo = connectivityManager.getActiveNetworkInfo();
    }

    public int getAppVersion() {
        return BuildConfig.VERSION_CODE;
    }

    public int getBatteryPercentage() {
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public Status getConnectionStatus() {
        if (networkInfo != null) {
            if (networkInfo.isAvailable() && networkInfo.isConnected()){
                return Status.CONNECTED;
            }
        }
        return Status.DISCONNECTED;
    }

    public Network getNetworkType() {
        if (networkInfo != null) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return Network.WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return Network.MOBILE;
                case ConnectivityManager.TYPE_BLUETOOTH:
                    return Network.BLUETOOTH;
            }
        }
        return Network.NONE;
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return formatter.format(date);
    }

    public int getNumberOfInstalledApps() {
        int numberOfNonSystemApps = 0;
        List<ApplicationInfo> appList = context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo info : appList) {
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                numberOfNonSystemApps++;
            }
        }
        return numberOfNonSystemApps;
    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return info.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public enum Network {
        WIFI,
        MOBILE,
        BLUETOOTH,
        NONE
    }

    public enum Status {
        CONNECTED,
        DISCONNECTED
    }
}
