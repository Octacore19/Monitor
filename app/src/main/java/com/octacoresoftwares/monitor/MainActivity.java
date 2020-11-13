package com.octacoresoftwares.monitor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.octacoresoftwares.monitor.callback.DeviceCallback;
import com.octacoresoftwares.monitor.model.Device;
import com.octacoresoftwares.monitor.model.Response;
import com.octacoresoftwares.monitor.network.NetworkService;
import com.octacoresoftwares.monitor.repo.Repository;
import com.octacoresoftwares.monitor.services.MyWorker;
import com.octacoresoftwares.monitor.utils.DeviceUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int MULTIPLE_PERMISSIONS_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (checkPermission()) {
            case GRANTED:
                start();
                break;
            case RETRY:
                break;
            case REJECTED: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            MULTIPLE_PERMISSIONS_CODE);
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MULTIPLE_PERMISSIONS_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            } else {
                Toast.makeText(this, "You need to grant permissions to make this app work better",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Status checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return Status.GRANTED;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)
                || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))) {
            return Status.RETRY;
        } else {
            return Status.REJECTED;
        }
    }

    private void start() {
        final PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Monitor", ExistingPeriodicWorkPolicy.REPLACE, request);
    }

    enum Status {
        GRANTED,
        RETRY,
        REJECTED
    }
}