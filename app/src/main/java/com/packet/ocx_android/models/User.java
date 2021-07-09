package com.packet.ocx_android.models;

public class User {
    public int id;
    public String name;
    public String email;
    public String email_verified_at;
    public String password;
    public String provider_id;
    public String provider;
    public String remember_token;
    public String created_at;
    public String updated_at;

    public User(){}

    public User(int id, String name, String email, String email_verified_at, String password, String provider_id, String provider, String remember_token, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.email_verified_at = email_verified_at;
        this.password = password;
        this.provider_id = provider_id;
        this.provider = provider;
        this.remember_token = remember_token;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
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
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", email_verified_at='" + email_verified_at + '\'' +
                ", password='" + password + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", provider='" + provider + '\'' +
                ", remember_token='" + remember_token + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
