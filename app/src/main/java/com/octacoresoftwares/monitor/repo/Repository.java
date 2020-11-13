package com.octacoresoftwares.monitor.repo;

import androidx.annotation.NonNull;

import com.octacoresoftwares.monitor.callback.DeviceCallback;
import com.octacoresoftwares.monitor.model.Device;
import com.octacoresoftwares.monitor.model.Response;
import com.octacoresoftwares.monitor.network.NetworkApi;

import retrofit2.Call;
import retrofit2.Callback;

public class Repository implements Callback<Response> {

    private NetworkApi service;
    private DeviceCallback callback;

    public Repository(NetworkApi service, DeviceCallback callback) {
        this.service = service;
        this.callback = callback;
    }

    public void updateDeviceInfo(Device device) {
        service.update(device)
                .enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<Response> call,
                           @NonNull retrofit2.Response<Response> response) {
        if (response.isSuccessful()) {
            callback.success(response.body());
        } else {
            callback.error(response.errorBody());
        }
    }

    @Override
    public void onFailure(@NonNull Call<Response> call,
                          @NonNull Throwable t) {
        callback.error(t);
    }
}
