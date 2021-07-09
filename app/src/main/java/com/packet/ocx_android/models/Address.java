package com.packet.ocx_android.models;

public class Address {
    public int id;
    public int user_id;
    public String description;
    public String nom;
    public String addresse;
    public String created_at;
    public String updated_at;

    public Address() {};

    public Address(int id, int user_id, String description, String name, String address) {
        this.id = id;
        this.user_id = user_id;
        this.description = description;
        this.nom = name;
        this.addresse = address;
    }

    public Address(int id, int user_id, String description, String name, String address, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.description = description;
        this.nom = name;
        this.addresse = address;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    @Override
    public String toString() {
        return addresse;
    }

    public String toJSON() {
        return "{" +
                "\"user_id\":\"" + user_id + '\"' +
                ", \"description\":\"" + description + '\"' +
                ", \"nom\":\"" + nom + '\"' +
                ", \"addresse\":\"" + addresse + '\"' +
                '}';
    }

    public String toJSON_update() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ", \"user_id\":\"" + user_id + '\"' +
                ", \"description\":\"" + description + '\"' +
                ", \"nom\":\"" + nom + '\"' +
                ", \"addresse\":\"" + addresse + '\"' +
                '}';
    }
}
