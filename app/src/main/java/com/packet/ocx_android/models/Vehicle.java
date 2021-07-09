package com.packet.ocx_android.models;

public class Vehicle {

    public int id;
    public int user_id;
    public int vehicules_type_id;
    public int fuel_types_id;
    public String brand;
    public String model;
    public String description;
    public String nickname;
    public String created_at;
    public String updated_at;

    public Vehicle(){}

    public Vehicle(int id, int user_id, int vehicules_type_id, int fuel_types_id, String brand, String model, String description, String nickname) {
        this.id = id;
        this.user_id = user_id;
        this.vehicules_type_id = vehicules_type_id;
        this.fuel_types_id = fuel_types_id;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.nickname = nickname;
    }

    public int getVehicules_type_id() {
        return vehicules_type_id;
    }

    public void setVehicules_type_id(int vehicules_type_id) {
        this.vehicules_type_id = vehicules_type_id;
    }
    public int getFuel_types_id() {
        return fuel_types_id;
    }

    public void setFuel_types_id(int fuel_types_id) {
        this.fuel_types_id = fuel_types_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + "\"" + id + "\"" +
                ", \"user_id\":" + "\"" + user_id + "\"" +
                ", \"vehicules_type_id\":" + "\"" + vehicules_type_id + "\"" +
                ", \"fuel_types_id\":" + "\"" + fuel_types_id + "\"" +
                ", \"brand\":" + "\"" + brand + "\"" +
                ", \"model\":" + "\"" + model + "\"" +
                ", \"description\":" + "\"" + description + "\"" +
                ", \"nickname\":" + "\"" + nickname + "\"" +
                '}';
    }

    public String toJSON() {
        return "{" +
                "\"vehicules_type_id\":" + "\"" + vehicules_type_id + "\"" +
                ", \"fuel_types_id\":" + "\"" + fuel_types_id + "\"" +
                ", \"brand\":" + "\"" + brand + "\"" +
                ", \"model\":" + "\"" + model + "\"" +
                ", \"description\":" + "\"" + description + "\"" +
                ", \"nickname\":" + "\"" + nickname + "\"" +
                '}';
    }
}
