package com.octacoresoftwares.monitor.callback;

import com.octacoresoftwares.monitor.model.Response;

public interface DeviceCallback {
    void success(Response response);
    void error(Object e);
}
