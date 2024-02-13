package classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
abstract class connection{
    public connection connector() throws SQLException {
        try {
            Connection conn2 = null;
            String url1 = "jdbc:mysql://localhost:3306/neelesh";
            String user = "root";
            String password = "Darshil@2306";

            conn2 = DriverManager.getConnection(url1, user, password);
            if (conn2 != null) {
                System.out.println("Connected to the database neelesh");
            }
            return (connection) conn2;

        } finally {
            System.out.print("connection error");

        }
    }}

public class distance_calculation extends connection {


    private  double lat1;
    private  double lon1;
    private double lat2;
    private double lon2;



    //    public distance_calculation(double lat1,double long1,double lat2,double long2){
//        latitude1=lat1;
//        longitude1=long1;
//        latitude2=lat2;
//        longitude2=long2;
//
//    }

    public double distance(double lat1, double lat2, double lon1,
                           double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 1.23;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

//    @Override
//    public double distance() {
//        return 0;
//    }
}

//    class search_cab extends booking_info{
//     public void searching(){
//
//
//
//     }
//    }

