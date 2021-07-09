package com.packet.ocx_android.models;

public class Deplacements {
    public int id;
    public String dateDeplacement;
    public String addressDepart;
    public String addressArrive;
    public double kilomitrage;
    public int vehicle_id;
    public int user_id;
    public String created_at;
    public String comment;
    public String updated_at;
    public String time_start;
    public String time_end;
    public int deplacement_type_id;
    public String temps;
    public boolean allez;
    public Vehicle vehicule;
    public Deplacement_Type typeDeplacement;



    public Deplacements(){}

    public Deplacements(int id, String dateDeplacement, String addressDepart, String addressArrive,
                        double kilomitrage, int vehicle_id, int user_id, String created_at,
                        String comment, String updated_at, String timeStart, String timeStop,
                        int deplacement_type_id, String temps, boolean allez) {
        this.id = id;
        this.dateDeplacement = dateDeplacement;
        this.addressDepart = addressDepart;
        this.addressArrive = addressArrive;
        this.kilomitrage = kilomitrage;
        this.vehicle_id = vehicle_id;
        this.user_id = user_id;
        this.created_at = created_at;
        this.comment = comment;
        this.updated_at = updated_at;
        this.time_start = timeStart;
        this.time_end = timeStop;
        this.deplacement_type_id = deplacement_type_id;
        this.temps = temps;
        this.allez = allez;
    }

    public Deplacements(int id, String dateDeplacement, String addressDepart, String addressArrive,
                        double kilomitrage, Vehicle vehicule, int user_id, String created_at,
                        String comment, String updated_at, String timeStart, String timeStop,
                        int deplacement_type_id, String temps, boolean allez, Deplacement_Type typeDeplacement) {
        this.id = id;
        this.dateDeplacement = dateDeplacement;
        this.addressDepart = addressDepart;
        this.addressArrive = addressArrive;
        this.kilomitrage = kilomitrage;
        this.vehicule = vehicule;
        this.user_id = user_id;
        this.created_at = created_at;
        this.comment = comment;
        this.updated_at = updated_at;
        this.time_start = timeStart;
        this.time_end = timeStop;
        this.deplacement_type_id = deplacement_type_id;
        this.temps = temps;
        this.allez = allez;
        this.typeDeplacement = typeDeplacement;
    }

    public Deplacements(int id, String dateDeplacement, String addressDepart, String addressArrive,
                        double kilomitrage, int vehicle_id, int user_id, String comment,
                        String timeStart, String timeStop, int deplacement_type_id, String temps, boolean allez) {
        this.id = id;
        this.dateDeplacement = dateDeplacement;
        this.addressDepart = addressDepart;
        this.addressArrive = addressArrive;
        this.kilomitrage = kilomitrage;
        this.vehicle_id = vehicle_id;
        this.user_id = user_id;
        this.comment = comment;
        this.time_start = timeStart;
        this.time_end = timeStop;
        this.deplacement_type_id = deplacement_type_id;
        this.temps = temps;
        this.allez = allez;
    }

    public Deplacements(int id, int user_id, int vehicle_id, int deplacement_type_id,
                        String dateDeplacement, String addressDepart, String addressArrive,
                        String timeStart, String timeStop, double kilomitrage, String temps,
                        String comment) {
        this.id = id;
        this.dateDeplacement = dateDeplacement;
        this.addressDepart = addressDepart;
        this.addressArrive = addressArrive;
        this.kilomitrage = kilomitrage;
        this.vehicle_id = vehicle_id;
        this.user_id = user_id;
        this.comment = comment;
        this.time_start = timeStart;
        this.time_end = timeStop;
        this.deplacement_type_id = deplacement_type_id;
        this.temps = temps;
    }



    public Deplacements(int id, String addressDepart, String timeStart) {
        this.id = id;
        this.addressDepart = addressDepart;
        this.time_start = timeStart;
    }

    public Vehicle getVehicle() {
        return vehicule;
    }

    public Vehicle getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicle vehicule) {
        this.vehicule = vehicule;
    }

    public Deplacement_Type getTypeDeplacement() {
        return typeDeplacement;
    }

    public void setTypeDeplacement(Deplacement_Type typeDeplacement) {
        this.typeDeplacement = typeDeplacement;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicule = vehicle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateDeplacement() {
        return dateDeplacement;
    }

    public void setDateDeplacement(String dateDeplacement) {
        this.dateDeplacement = dateDeplacement;
    }

    public boolean isAllez() { return allez; }

    public void setAllez(boolean allez) { this.allez = allez; }

    public String getAddressDepart() {
        return addressDepart;
    }

    public void setAddressDepart(String addressDepart) {
        this.addressDepart = addressDepart;
    }

    public String getAddressArrive() {
        return addressArrive;
    }

    public void setAddressArrive(String addressArrive) {
        this.addressArrive = addressArrive;
    }

    public double getKilomitrage() {
        return kilomitrage;
    }

    public void setKilomitrage(double kilomitrage) {
        this.kilomitrage = kilomitrage;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public int getDeplacement_type_id() {
        return deplacement_type_id;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public void setDeplacement_type_id(int deplacement_type_id) {
        this.deplacement_type_id = deplacement_type_id;
    }

    @Override
    public String toString() {
        return "Deplacements{" +
                "id=" + id +
                ", dateDeplacement='" + dateDeplacement + '\'' +
                ", addressDepart='" + addressDepart + '\'' +
                ", addressArrive='" + addressArrive + '\'' +
                ", kilomitrage=" + kilomitrage +
                ", vehicle_id=" + vehicle_id +
                ", user_id=" + user_id +
                ", created_at='" + created_at + '\'' +
                ", comment='" + comment + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", time_start='" + time_start + '\'' +
                ", time_end='" + time_end + '\'' +
                ", deplacement_type_id=" + deplacement_type_id +
                ", temps='" + temps + '\'' +
                '}';
    }

    public String toJSON() {
        return "{" +
                "\"dateDeplacement\":\"" + dateDeplacement + '\"' +
                ", \"addressDepart\":\"" + addressDepart + '\"' +
                ", \"addressArrive\":\"" + addressArrive + '\"' +
                ", \"kilomitrage\":\"" + kilomitrage + '\"' +
                ", \"vehicle_id\":\"" + vehicle_id + '\"' +
                ", \"user_id\":\"" + user_id + '\"' +
                ", \"comment\":\"" + comment + '\"' +
                ", \"time_start\":\"" + time_start + '\"' +
                ", \"time_end\":\"" + time_end + '\"' +
                ", \"deplacement_type_id\":\"" + deplacement_type_id + '\"' +
                ", \"temps\":\"" + temps + '\"' +
                ", \"allez\":" + allez  +
                '}';
    }

    public String toJSON_update() {
        return "{" +
                ", \"dateDeplacement\":\"" + dateDeplacement + '\"' +
                ", \"addressDepart\":\"" + addressDepart + '\"' +
                ", \"addressArrive\":\"" + addressArrive + '\"' +
                ", \"kilomitrage\":\"" + kilomitrage + '\"' +
                ", \"vehicle_id\":\"" + vehicle_id + '\"' +
                ", \"user_id\":\"" + user_id + '\"' +
                ", \"comment\":\"" + comment + '\"' +
                ", \"time_start\":\"" + time_start + '\"' +
                ", \"time_end\":\"" + time_end + '\"' +
                ", \"deplacement_type_id\":\"" + deplacement_type_id + '\"' +
                ", \"temps\":\"" + temps + '\"' +
                '}';
    }
}
