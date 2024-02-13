package com.function_interface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

interface methods{
    public  double distance(double lat1, double lat2, double lon1,
                            double lon2) ;
    public double extractor_and_calculator(String source,String dest) throws SQLException;
    public double get_payment(int choice,String source_i,String dest_i) throws SQLException;
    public String booking_status(int st_pay);
    public String vehicle_type(int choice);
    public void show_bookings(String name,String pwd) throws SQLException;
    public void cancel_booking(int booking_id,int count) throws SQLException;
    public void edit_booking(int booking_id,String source,String dest,int fare,String dest_a) throws SQLException;
    public String select_driver() throws SQLException;


//    public abstract double distance(double lat1, double lat2, double lon1,
//                                    double lon2);

}
public class method_define implements methods{


    @Override
    public double distance(double lat1, double lat2, double lon1, double lon2) {
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

    @Override
    public double extractor_and_calculator(String source, String dest) throws SQLException {
        Connection conn2 = null;

        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";

        conn2 = DriverManager.getConnection(url1, user, password);
        if (conn2 != null) {
            //System.out.println("Connected to the database neelesh");
        }
//         dest_i=dest;
//         source_i=source;


        assert conn2 != null;
        PreparedStatement stm = conn2.prepareStatement("select latitude,longitude from worldcities WHERE city LIKE ? ");
        stm.setString(1, ("%" + source + "%"));
        ResultSet res = stm.executeQuery();

        PreparedStatement stm2 = conn2.prepareStatement("select latitude,longitude from worldcities WHERE city LIKE ? ");
        stm2.setString(1, ("%" + dest + "%"));
        ResultSet res2 = stm2.executeQuery();
        double final_d = 0;

        while (res.next() && res2.next() ) {
            //String lastName = rs.getString("rs");

            double lat1 = res.getDouble("latitude");
            double lon1 = res.getDouble("longitude");
            double lat2 = res2.getDouble("latitude");
            double lon2 = res2.getDouble("longitude");

            //rs.getNextResultset();

            //distance_calculation dc=new distance_calculation(lat1,lon1,lat2,lon2);
            final_d=distance(lat1,lat2,lon1,lon2);



            res.next();
            res2.next();


            //rs.getNextResultset();

            //return final_d;
        }
        return final_d;

    }


    @Override
    public double get_payment(int choice, String source_i, String dest_i) throws SQLException {
        double amount;
        switch(choice){//
            case 1:{// fare of mini car $10 per km
                amount=extractor_and_calculator(source_i,dest_i)*10+15;//tax and GSt included
                //return amount;
                break;

            }
            case 2:{
                // for XL
                amount=extractor_and_calculator(source_i,dest_i)*17+15;//tax and GSt included
                //return amount;
                break;
            }
            case 3:{//for premium
                amount=extractor_and_calculator(source_i,dest_i)*15+15;//tax and GSt included
                //return amount;
                break;

            }
            case 4:{// for force/bus
                amount=extractor_and_calculator(source_i,dest_i)*23+15;//tax and GSt included
                //return amount;
                break;
            }
            default:{
                System.out.printf("enter valid choice");
                return 0;
            }




        }
        return (amount/1000);

    }

    @Override
    public String booking_status(int st_pay) {
        if(st_pay==1){
            return "booking confirmed";
        } else  {
            return "not booked";
        }


    }

    @Override
    public String vehicle_type(int choice) {
        if (choice==1){
            return "Mini(go)";

        } else if (choice==2) {
            return "XL";


        } else if (choice==3) {
            return "PREMIUM";

        }
        else{
            return "force/bus";

        }

    }

    @Override
    public void show_bookings(String name, String pwd) throws SQLException {
        Connection conn3 = null;
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";

        conn3 = DriverManager.getConnection(url1, user, password);





        //String show_b="select * from cabbooking where booking_id="+booking_id+"";
        PreparedStatement stm5 = conn3.prepareStatement("select * from cabbooking where rider= ? and password= ?");
        stm5.setString(1,name);
        stm5.setString(2,pwd);


        // adding booking status also
        System.out.printf("booking_id\trider_name\tmob_no\tpassword\tbooking status\trating\tcount\tsource\tdestination\tfare\n");

        ResultSet r=stm5.executeQuery();
        while (r.next()){
            String name_r=r.getString("rider");
            Integer book_id=r.getInt("booking_id");
            String mob= r.getString("mob_no");
            String pwd_r=r.getString("password");
            String book_s=r.getString("booking_status");
            String rating=r.getString("rating");
            Integer count=r.getInt("booking_count");
            String source=r.getString("source");
            String destination=r.getString("destination");
            Integer fare=r.getInt("fare");

            System.out.printf("%d\t%s\t%s\t%s\t%s\t%s\t %d\t %s\t %s\t%d \n",book_id,name_r,mob,pwd_r,book_s,rating,count,source,destination,fare);
            //



        }


    }

    @Override
    public void cancel_booking(int booking_id, int count) throws SQLException {
        Connection conn3 = null;
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";
        String status=null;

        conn3 = DriverManager.getConnection(url1, user, password);
        PreparedStatement check = conn3.prepareStatement("select booking_status from cabbooking where booking_id=?");
        check.setInt(1,booking_id);
        ResultSet res=check.executeQuery();
        while(res.next()){
            status=res.getString("booking_status");
        }
        if(Objects.equals(status, "cancelled")){
            System.out.printf("you have already cancelled this trip\n");
        }
        else{
            // able to cancel booking based on current trip
            PreparedStatement stm5 = conn3.prepareStatement("update cabbooking set booking_status='cancelled' where booking_id="+booking_id+"");

            // adding booking status also

            stm5.execute();
            PreparedStatement stm6=conn3.prepareStatement("update cabbooking set booking_count=? where booking_id=?");
            stm6.setInt(1,count-1);
            stm6.setInt(2,booking_id);
            stm6.execute();}



    }

    @Override
    public void edit_booking(int booking_id, String source, String dest, int fare, String dest_a) throws SQLException {
        Connection conn3 = null;
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";

        Integer fare_u=fare;
        String status = null;
        conn3 = DriverManager.getConnection(url1, user, password);
        // for safety purposes we must check  if trip is cancelled or not then go for updating
        PreparedStatement check = conn3.prepareStatement("select booking_status from cabbooking where booking_id=?");
        check.setInt(1,booking_id);
        ResultSet res=check.executeQuery();
        while(res.next()){
            status=res.getString("booking_status");
        }
        if(Objects.equals(status, "cancelled")){
            System.out.printf("YOU can't edit your destination stop because your trip is already cancelled\n");
        }
        else{
            PreparedStatement stm5 = conn3.prepareStatement("update cabbooking set fare=? where booking_id=?");

            stm5.setInt(1,fare_u);
            stm5.setInt(2,booking_id);
            stm5.execute();
            PreparedStatement stm9 = conn3.prepareStatement("update cabbooking set destination=? where booking_id=?");

            stm9.setString(1,dest);
            stm9.setInt(2,booking_id);
            stm9.execute();
            PreparedStatement stm10 = conn3.prepareStatement("update cabbooking set destination_address=? where booking_id=?");

            stm10.setString(1,dest_a);
            stm10.setInt(2,booking_id);
            stm10.execute();
            System.out.printf("your trip is updated your booking is now from %s to %s \n ",source,dest);
            System.out.printf("please pay your driver %d rupees\n ",fare_u);}


    }
    public List<Object> name_driver=new ArrayList<>();
    public List<Object> distance_driver=new ArrayList<>();
    public List<Object> time_driver=new ArrayList<>();
    Random r=new Random();

    @Override
    public String select_driver() throws SQLException {
        Connection conn3 = null;
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";


        conn3 = DriverManager.getConnection(url1, user, password);
        PreparedStatement stm5 = conn3.prepareStatement("select * from driver");
        ResultSet res=stm5.executeQuery();
        while(res.next()){
            name_driver.add(res.getString("name"));
            distance_driver.add(res.getString("Distance"));
            time_driver.add(res.getString("Time"));




        }
        System.out.printf("3 drivers found near by\n");
        Integer i=r.nextInt(47);
        Integer k=r.nextInt(3);
        for(int j=0;j<3;j++){
            String name_d= (String) name_driver.get(i+j);
            String dist= (String) distance_driver.get(i+j);
            String time= (String) time_driver.get(i+j);
            System.out.printf("%s\t%s\t%s\n",name_d,dist,time);

        }
        System.out.printf("%s accepted your trip and is %s away arriving in %s",(String) name_driver.get(i+k),(String) distance_driver.get(i+k),(String) time_driver.get(i+k));
        return (String) name_driver.get(i+k);



    }

}


