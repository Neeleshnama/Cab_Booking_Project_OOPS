package classes;

import java.sql.*;
import java.util.Objects;

public class management_of_bookings {
    // able to do show all bookings and cancel a ride also

    public void show_bookings(String name, String pwd) throws SQLException {
        Connection conn3 = null;
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";

        conn3 = DriverManager.getConnection(url1, user, password);


        //String show_b="select * from cabbooking where booking_id="+booking_id+"";
        PreparedStatement stm5 = conn3.prepareStatement("select * from cabbooking where rider= ? and password= ? order by booking_count desc");
        stm5.setString(1, name);
        stm5.setString(2, pwd);


        // adding booking status also
        System.out.printf("booking_id\trider_name\tmob_no\tpassword\tbooking status\trating\tcount\tsource\tdestination\tfare\n");

        ResultSet r = stm5.executeQuery();
        while (r.next()) {
            String name_r = r.getString("rider");
            Integer book_id = r.getInt("booking_id");
            String mob = r.getString("mob_no");
            String pwd_r = r.getString("password");
            String book_s = r.getString("booking_status");
            String rating = r.getString("rating");
            Integer count = r.getInt("booking_count");
            String source = r.getString("source");
            String destination = r.getString("destination");
            Integer fare = r.getInt("fare");

            System.out.printf("%d\t%s\t%s\t%s\t%s\t%s\t %d\t %s\t %s\t%d \n", book_id, name_r, mob, pwd_r, book_s, rating, count, source, destination, fare);
            //


        }
    }

    public void cancel_booking(int booking_id, int count, String source, String destination) throws SQLException {
        Connection conn3 = null;
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";
        String status = null;

        conn3 = DriverManager.getConnection(url1, user, password);
        PreparedStatement check = conn3.prepareStatement("select booking_status from cabbooking where booking_id=?");
        check.setInt(1, booking_id);
        ResultSet res = check.executeQuery();
        while (res.next()) {
            status = res.getString("booking_status");
        }
        if (Objects.equals(status, "cancelled")) {
            System.out.printf("you have already cancelled this trip\n");
        } else {
            if ((status != null)) {
                PreparedStatement stm5 = conn3.prepareStatement("update cabbooking set booking_status='cancelled' where booking_id=?");
                stm5.setInt(1, booking_id);

                // adding booking status also

                stm5.execute();
                PreparedStatement stm6 = conn3.prepareStatement("update cabbooking set booking_count=? where booking_id=?");
                stm6.setInt(1, count - 1);
                stm6.setInt(2, booking_id);
                stm6.execute();
                System.out.printf("your current trip from %s to %s is cancelled ", source, destination);
            } else {
                System.out.printf("sorry,First book your cab we are unable to fatch your current booking\n");
            }

        }
        // able to cancel booking based on current trip
//            PreparedStatement stm5 = conn3.prepareStatement("update cabbooking set booking_status='cancelled' where booking_id="+booking_id+"");
//
//            // adding booking status also
//
//            stm5.execute();
//            PreparedStatement stm6=conn3.prepareStatement("update cabbooking set booking_count=? where booking_id=?");
//            stm6.setInt(1,count-1);
//            stm6.setInt(2,booking_id);
//            stm6.execute();}


    }

    public String edit_booking(int booking_id, String source, String destination, String dest, int fare, String dest_a) throws SQLException {
        try {
            Connection conn3 = null;
            String url1 = "jdbc:mysql://localhost:3306/neelesh";
            String user = "root";
            String password = "Darshil@2306";

            Integer fare_u = fare;
            String status = null;
            conn3 = DriverManager.getConnection(url1, user, password);
            // for safety purposes we must check  if trip is cancelled or not then go for updating
            PreparedStatement check = conn3.prepareStatement("select booking_status from cabbooking where booking_id=?");
            check.setInt(1, booking_id);
            ResultSet res = check.executeQuery();
            while (res.next()) {
                status = res.getString("booking_status");
            }
            if (Objects.equals(status, "cancelled")) {
                System.out.printf("YOU can't edit your destination stop because your trip is already cancelled\n");
                return destination;

            } else {
                if (status != null) {
                    int ptr = 1;
                    PreparedStatement stm5 = conn3.prepareStatement("update cabbooking set fare=? where booking_id=?");

                    stm5.setInt(1, fare_u);
                    stm5.setInt(2, booking_id);
                    stm5.execute();
                    PreparedStatement stm9 = conn3.prepareStatement("update cabbooking set destination=? where booking_id=?");

                    stm9.setString(1, dest);
                    stm9.setInt(2, booking_id);
                    stm9.execute();
                    PreparedStatement stm10 = conn3.prepareStatement("update cabbooking set destination_address=? where booking_id=?");

                    stm10.setString(1, dest_a);
                    stm10.setInt(2, booking_id);
                    stm10.execute();
                    System.out.printf("your trip is updated your booking is now from %s to %s \n ", source, dest);
                    System.out.printf("please pay your driver %d rupees\n ", fare_u);
                    return dest;
                } else {
                    System.out.printf("sorry,we can not fetch your current bookinf please first book atrip\n");


                }
            }
        } catch (SQLException exception) {
            System.out.printf("please enter valid destination city\n");
            exception.printStackTrace();

        }
        return destination;
    }


}
