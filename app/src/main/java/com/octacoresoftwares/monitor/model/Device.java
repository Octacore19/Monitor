package com.octacoresoftwares.monitor.model;

import java.io.Serializable;

public class Device implements Serializable {

    private String appVersion;
    private String batteryLevel;
    private String dataUsageCollectionTime;
    private Location location;
    private int signalStrength;
    private String connectivityStatus;
    private String networkType;
    private String terminalId;
    private int numberOfApps;
    private boolean hasApp;

    public Device() {}

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void setDataUsageCollectionTime(String dataUsageCollectionTime) {
        this.dataUsageCollectionTime = dataUsageCollectionTime;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    public void setConnectivityStatus(String connectivityStatus) {
        this.connectivityStatus = connectivityStatus;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public void setNumberOfApps(int numberOfApps) {
        this.numberOfApps = numberOfApps;
    }

    public void setHasApp(boolean hasApp) {
        this.hasApp = hasApp;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public String getDataUsageCollectionTime() {
        return dataUsageCollectionTime;
    }

    public Location getLocation() {
        return location;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public String getConnectivityStatus() {
        return connectivityStatus;
    }

    public String getNetworkType() {
        return networkType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public int getNumberOfApps() {
        return numberOfApps;
    }

    public boolean isHasApp() {
        return hasApp;
    }

    @Override
    public String toString() {
        return "Device{" +
                "appVersion='" + appVersion + '\'' +
                ", batteryLevel='" + batteryLevel + '\'' +
                ", dataUsageCollectionTime='" + dataUsageCollectionTime + '\'' +
                ", location=" + location +
                ", signalStrength=" + signalStrength +
                ", connectivityStatus='" + connectivityStatus + '\'' +
                ", networkType='" + networkType + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", numberOfApps=" + numberOfApps +
                ", hasApp=" + hasApp +
                '}';
    }

    public static class Location implements Serializable {
        private String longitude;
        private String latitude;

        public Location () {}

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "longitude='" + longitude + '\'' +
                    ", latitude='" + latitude + '\'' +
                    '}';
        }
    }
}
