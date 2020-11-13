package com.octacoresoftwares.monitor.callback;

import com.octacoresoftwares.monitor.model.Device;

public interface LocationResult {
    void onLocationReceived(Device.Location location);
}
