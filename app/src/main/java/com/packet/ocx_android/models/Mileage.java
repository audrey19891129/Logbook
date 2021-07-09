package com.packet.ocx_android.models;

public class Mileage {
    public int it;
    public int vehicle_id;
    public String datetime;
    public int odometer;

    public Mileage(){};

    public Mileage(int it, int vehicle_id, String datetime, int odometer) {
        this.it = it;
        this.vehicle_id = vehicle_id;
        this.datetime = datetime;
        this.odometer = odometer;
    }

    public int getIt() {
        return it;
    }

    public void setIt(int it) {
        this.it = it;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }
}
