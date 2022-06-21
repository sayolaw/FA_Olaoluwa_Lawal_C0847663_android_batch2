package com.example.fa_olaoluwa_lawal_c0857663_android;

public class Place {
    private int id, status;
    private String name,dateCreated;
    private Double latitude,longitude;

    public Place(int id, String name,int status, Double latitude,Double longitude,String dateCreated) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateCreated = dateCreated;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public int getStatus() {
        return status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDate() {
        return dateCreated;
    }


}
