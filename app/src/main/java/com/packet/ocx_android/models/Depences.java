package com.packet.ocx_android.models;

public class Depences {

    public int id;
    public String dateDepence;
    public String imageFile;
    public String description;
    public double montant;
    public int depense_type_id;
    public String created_at;
    public String updated_at;

    public Depences() {
    }

    ;

    public Depences(int id, String dateDepence, String imageFile, String description, double montant, int depense_type_id, String created_at, String updated_at) {
        this.id = id;
        this.dateDepence = dateDepence;
        this.imageFile = imageFile;
        this.description = description;
        this.montant = montant;
        this.depense_type_id = depense_type_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Depences(int id, String dateDepence,String description, double montant, int depense_type_id, String created_at, String updated_at) {
        this.id = id;
        this.dateDepence = dateDepence;
        this.description = description;
        this.montant = montant;
        this.depense_type_id = depense_type_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Depences(int id, String dateDepence, String description, double montant, int depense_type_id) {
        this.id = id;
        this.dateDepence = dateDepence;
        this.description = description;
        this.montant = montant;
        this.depense_type_id = depense_type_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateDepence() {
        return dateDepence;
    }

    public void setDateDepence(String dateDepence) {
        this.dateDepence = dateDepence;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getDepense_type_id() {
        return depense_type_id;
    }

    public void setDepense_type_id(int depense_type_id) {
        this.depense_type_id = depense_type_id;
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
        return "Depences{" +
                "id=" + id +
                ", dateDepence='" + dateDepence + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", description='" + description + '\'' +
                ", montant=" + montant +
                ", depense_type_id=" + depense_type_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    public String toJSON(){
        return "{" +
                "\"dateDepence\":\"" + dateDepence + '\"' +
                ", \"imageLink\":\"data:image/x-icon;base64," + imageFile + '\"' +
                ", \"descriptionDepence\":\"" + description + '\"' +
                ", \"montantDepence\":\"" + montant + '\"' +
                ", \"type_id\":\"" + depense_type_id +'\"' +
                '}';
    }
}
