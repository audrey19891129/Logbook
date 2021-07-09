package com.packet.ocx_android.models;

public class Subscriptions {
    public int id;
    public String id_plan;
    public String name;
    public String description;
    public double amount;
    public String date;
    public String created_at;
    public String updated_at;

    public Subscriptions(int id, String id_plan, String name, String description, double amount, String date, String created_at, String updated_at) {
        this.id = id;
        this.id_plan = id_plan;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_plan() {
        return id_plan;
    }

    public void setId_plan(String id_plan) {
        this.id_plan = id_plan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Subscriptions{" +
                "id=" + id +
                ", id_plan='" + id_plan + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
