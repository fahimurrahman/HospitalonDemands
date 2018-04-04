package com.example.hp.hospitalondemand.DatabaseHelper;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 30-Sep-17.
 */
public class Viewholder {
    String hospitalName;
    String description;
    Double latitude;
    Double longitude;

    public Viewholder(){

    }

    public Viewholder(String hospitalName, String description, Double latitude, Double longitude) {
        this.hospitalName = hospitalName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("hospitalName", hospitalName);
        result.put("hospital_description",description);
        result.put("Latitude", latitude);
        result.put("Longitude", longitude);


        return result;
    }
}
