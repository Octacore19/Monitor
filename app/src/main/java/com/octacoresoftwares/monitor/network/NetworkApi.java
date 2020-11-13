package com.octacoresoftwares.monitor.network;

import com.octacoresoftwares.monitor.model.Device;
import com.octacoresoftwares.monitor.model.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NetworkApi {

    @POST("/tms/devicestatus/v1/logStatus")
    Call<Response> update(@Body Device body);
}
