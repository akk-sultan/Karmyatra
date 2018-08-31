package com.example.rahul_jareda.karmyatra;

public class findCurrentLocationID extends MainActivity{
    static int findCurrentLocationID_method(double currentLatitude, double currentLongitude) // using Haversine Formula
    {
        final double R = 6371; //earth's radius
        int listNumber;
        int i;
        int listCount = coordinates.length;
        for(i = 0;i<listCount;i++) {
            double lat1 = Math.toRadians(currentLatitude); // Latitude 1
            double lat2 = Math.toRadians(coordinates[i][0]); // Latitude 2
            double latDistance = Math.toRadians(coordinates[i][0] - currentLatitude);
            double longDistance = Math.toRadians(coordinates[i][1] - currentLongitude);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(lat1)*Math.cos(lat2)*Math.sin(longDistance/2)*Math.sin(longDistance/2);
            double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double dist = R*c ; //in km
            if(dist<2)
                break;
        }
        listNumber = i;
        return listNumber;
    }
}
