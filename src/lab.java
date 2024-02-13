import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.time.LocalTime;
import java.util.*;

// distance calculator from given lat ana longitude
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
    }

    //    public distance_calculation(double lat1,double long1,double lat2,double long2){
    //        latitude1=lat1;
    //        longitude1=long1;
    //        latitude2=lat2;
    //        longitude2=long2;
    //
    //    }

}
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
class method_define implements methods{


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



class distance_calculation extends connection {


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
class feeder extends distance_calculation  {
    public  double final_d;
    //connection c=new connection();
    protected String source_i;
    protected String dest_i;


    public feeder(String s,String d){
        this.source_i=s;
        this.dest_i=d;


    }





    public double extractor_and_calculator(String source,String dest) throws SQLException {
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
            final_d=super.distance(lat1,lat2,lon1,lon2);



            res.next();
            res2.next();


            //rs.getNextResultset();

            //return final_d;
        }
        return final_d;

    }




}
class payment extends feeder{
    String source_i;
    public payment(String s, String d) {
        super(s, d);
        // this.source_i=s;

    }


    //String source_i=s;

//    public payment(double lat1, double long1, double lat2, double long2) {
//        super(lat1, long1, lat2, long2);
//    }

    public double get_payment(int choice,String source_i,String dest_i) throws SQLException {
        double amount;
        switch(choice){//
            case 1:{// fare of mini car $10 per km
                amount=super.extractor_and_calculator(source_i,dest_i)*10+15;//tax and GSt included
                //return amount;
                break;

            }
            case 2:{
                // for XL
                amount=super.extractor_and_calculator(source_i,dest_i)*17+15;//tax and GSt included
                //return amount;
                break;
            }
            case 3:{//for premium
                amount=super.extractor_and_calculator(source_i,dest_i)*15+15;//tax and GSt included
                //return amount;
                break;

            }
            case 4:{// for force/bus
                amount=super.extractor_and_calculator(source_i,dest_i)*23+15;//tax and GSt included
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
}
class booking_info{
    Random rand = new Random();

    // Generate random integers in range 0 to 9999
    int rand_int1 = rand.nextInt(10000);
    //int rand_int2 = rand.nextInt(10000);



    public Integer booking_id=rand_int1;
    public int get_booking_id(){
        return rand_int1;

    }
    public String booking_status(int st_pay){
        if(st_pay==1){
            return "booking confirmed ";
        } else  {
            return "not booked";
        }


    }



    public String vehicle_type(int choice){
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

    public void get_name(){



    }}
//    class search_cab extends booking_info{
//     public void searching(){
//
//
//
//     }
//    }

class management_of_bookings{
    // able to do show all bookings and cancel a ride also

    public void show_bookings(String name,String pwd) throws SQLException{
        Connection conn3 = null;
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";

        conn3 = DriverManager.getConnection(url1, user, password);





        //String show_b="select * from cabbooking where booking_id="+booking_id+"";
        PreparedStatement stm5 = conn3.prepareStatement("select * from cabbooking where rider= ? and password= ? order by booking_count desc");
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
    public void cancel_booking(int booking_id,int count,String source,String destination) throws SQLException {
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
        } else  {
            if ((status!=null)){
                PreparedStatement stm5 = conn3.prepareStatement("update cabbooking set booking_status='cancelled' where booking_id=?");
                stm5.setInt(1,booking_id);

                // adding booking status also

                stm5.execute();
                PreparedStatement stm6=conn3.prepareStatement("update cabbooking set booking_count=? where booking_id=?");
                stm6.setInt(1,count-1);
                stm6.setInt(2,booking_id);
                stm6.execute();
                System.out.printf("your current trip from %s to %s is cancelled ",source,destination);}
            else{
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
    public String edit_booking(int booking_id,String source,String destination,String dest,int fare,String dest_a) throws SQLException {
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
            return destination;

        } else  {
            if (status!=null){
                int ptr=1;
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
                System.out.printf("please pay your driver %d rupees\n ",fare_u);
                return  dest;}
            else{System.out.printf("sorry,we can not fetch your current bookinf please first book atrip\n");
                return destination;

            }
        }




    }

}
class driver{
    // getting the driver id
    public List<Object> name_driver=new ArrayList<>();
    public List<Object> distance_driver=new ArrayList<>();
    public List<Object> time_driver=new ArrayList<>();


    Random r=new Random();
    public Integer driver_id=r.nextInt(10000);
    public String select_driver() throws SQLException {
        // to show possible 3-4 nearest drivers and pickup one that accepts
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
        try {System.out.printf("requsesting ...\n");

            //System.out.printf("Start Time: %s\n", LocalTime.now());
            Thread.sleep(4 * 1000); // Wait for 4 seconds
            //System.out.printf("End Time: %s\n", LocalTime.now());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s accepted your trip and is %s away arriving in %s",(String) name_driver.get(i+k),(String) distance_driver.get(i+k),(String) time_driver.get(i+k));
        return (String) name_driver.get(i+k);



    }




    public Integer getDriver_id() {
        return driver_id;
    }
    // driver name has been described in main class
    // for searching a driver based on time
    // function pending


}







public class lab {
    public static  void main(String[] args) throws SQLException {

        // creates three different Connection objects
        Connection conn1 = null;

        Integer booking_id = 0;
        Integer choice = 0;
        //String name;
        //DECLARING THE INTERFACE CALL
        method_define int_call=new method_define();



        // try {
        Scanner sc=  new Scanner(System.in);
        List<String> l1 = new ArrayList<String>();
        String l2[]={"nitish","mohit","ram","shyam","mohan","garvit","avinash"};
        List<String> suggestion_box = new ArrayList<String>();


        l1.addAll(List.of(l2));




        // connect way #1
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";

        conn1 = DriverManager.getConnection(url1, user, password);
        if (conn1 != null) {
            System.out.println("Connected to the database neelesh");
        }
        Statement stmt = conn1.createStatement();
        ResultSet rs;
        //stmt.executeQuery("use neelesh");
//            System.out.printf("enter city\n");
//             String city = sc.next();

        Integer login_pointer=0;
        Integer count = 0;
        String source = null;
        String destination = null;
        String source_address= null;
        String destination_address=null;
        //Integer booking_id;

        // START OF MAIN PART(CONNECTION COMPLETED)
        //1) LOGIN WINDOW + REGISTRATION OF NEW USER
        System.out.print("\tWELCOME TO ONLINE CAB BOOKING APP WISHING YOU A SUCCESFUL AND COMFORTABLE JOURNEY\n");
        String name = null;
        Integer check=1;
        payment pn=new payment(source,destination);
//        int pt=0;
        int ch=0;

        while (ch!=3){
            // String name;
//            System.out.print("login window\n");
            //String name;
            try {
                String mob_no;
                String pwd;
                String cmp_p=null;
                String cmp_m = null;
                if(ch==3){break;}



                int flag = 0;
                while (true) {
                    Scanner ln=new Scanner(System.in);
                    int pt=0,ds=0;

                    System.out.printf("1--> <login>\n2--> <register yourself>\n3--> <exit>\n");
                    ch=ln.nextInt();
                    if(ch==2){ System.out.printf("please register yourself\n");
                        try{
                            Scanner p = new Scanner(System.in);
                            System.out.printf("enter your name:-\n");

                            String named = p.next();
                            System.out.printf("enter password that you want to set:-\n");

                            String pwde = p.next();
                            // BigInteger mob = p.nextBigInteger();
                            System.out.printf("enter the mobile number:-\n");
                            String mob=p.next();



                            // PREPARING
//                       PreparedStatement stm2 = conn1.prepareStatement("insert into registration values("named "," + mob + "," + pwde + ")");
//                          stm2.execute();
                            PreparedStatement stm2 = conn1.prepareStatement("insert into registration values(? , ? , ? )");
                            //stm2.setString("+named+","");
                            stm2.setString(1, (""+ named + ""));
                            stm2.setString(2, ("" + mob + ""));
                            stm2.setString(3, ("" + pwde + ""));
                            stm2.execute();

                            System.out.printf("user succesfully registered\n");
                            flag = 1;}
                        catch (Exception e){
                            System.out.printf("user already registered\n");
                            break;
                        }
                        //login_pointer=1;
                        //break;

                    }
                    if(ch==3){break;}


//                    String name;
                    //System.out.printf("check point");
                    System.out.print("-------------------------------------<<login window>>------------------------------------\n");
                    System.out.print("enter your mobile number please\n");
                    Scanner sp = new Scanner(System.in);
                    mob_no = sp.next();
                    System.out.print("enter your password please\n");
                    pwd = sp.next();
                    // writing sql queries
                    PreparedStatement stm_ = conn1.prepareStatement("select mob_no from registration WHERE mob_no = ? ");
                    stm_.setString(1, ("" + mob_no + ""));
                    ResultSet res = stm_.executeQuery();

                    //String name;

                    PreparedStatement stm1_ = conn1.prepareStatement("select password from registration WHERE password = ? ");
                    stm1_.setString(1, (""+ pwd + ""));
                    ResultSet res1 = stm1_.executeQuery();
//                    if (res.next()) {
//                        res1.next();
//                    }
                    flag=1;
//                    int no=1;
                    while(res.next() && res1.next()){
                        cmp_m=res.getString("mob_no");
                        cmp_p=res1.getString("password");
                        // System.out.printf("%s",cmp_m);
                        break;
                    }
                    while(true){
                        //ds=0;
                        if(pt==1){break;}
                        Scanner c_=new Scanner(System.in);
                        flag=2;
                        System.out.printf(" 2--> to continue booking\n");
                        int ct=c_.nextInt();
                        if (ct==1){break;
                        }
                        if(ds==2){break;}
                        int no=0;
                        // if(flag==1 || flag==0 ){
                        while(res.next() && res1.next() || no<5){
                            // System.out.println("entered the loop for check only\n");
                            if (Objects.equals(mob_no,cmp_m) && Objects.equals(pwd, cmp_p)) {
                                // user sucesfully loginned
                                flag=0;
                                login_pointer=1;
                                System.out.printf("-----------------------------[loginned]-----------------------------------------\n");
                                String str1 = "select name from registration where mob_no =? and password=?";
                                PreparedStatement stm8=conn1.prepareStatement(str1);
                                stm8.setString(1, ("" + mob_no + ""));
                                stm8.setString(2,"" + pwd + "");
                                ResultSet n=stm8.executeQuery();
                                while (n.next()){
                                    name=n.getString("name");
                                    n.next();
                                }
                                while (true){
                                    Scanner dash = new Scanner(System.in);



                                    System.out.printf("\t hello %s welcome again let's start a new journey\n", name);
                                    //SETTING UP A DASH BOARD MENU FOR CONVINIENCE

                                    System.out.printf("<*> Show your Previous Trips(1)\n<*> Settings(2)\n<*> Let's book a Ride(3)\n");
                                    int dash_b=dash.nextInt();
                                    if(dash_b==1){
                                        int_call.show_bookings(name,pwd);

                                    } else if (dash_b==2) {
                                        // in this show the account details and give user the options to add favorite places
                                        System.out.printf("Account Information\n \t Name:-%s \n\t Mobile No. %s",name,mob_no);
                                        System.out.printf("\nAdd your Favorite Stops in one place to enjoy Hastle Free journey\n");
                                        System.out.printf("1->ADD FAVORITE STOPS\n2->Delete your Account\n3->thanks,i want to continue booking\n");
                                        Scanner dt = new Scanner(System.in);
                                        ds=dt.nextInt();
                                        if(ds==1){
                                            System.out.printf("Enter a label for your Source {i.e-Home,Work etc.}\n");
                                            Scanner s_l=new Scanner(System.in);
                                            String src_l=s_l.next();
                                            System.out.printf("Enter your Source city :-\n");
                                            String src_c=s_l.next();

                                            System.out.printf("Enter a label for your Destination {i.e-Home,Work etc.}\n");

                                            String dst_l=s_l.next();
                                            System.out.printf("Enter your Destination  city :-\n");
                                            String dst_c=s_l.next();
                                            // now inttializing the value to labels and storing them in database
                                            src_l=src_c;
                                            dst_l=dst_c;


                                            String lat_s=null;
                                            String lon_s=null;
                                            // selecting latitude,longitude of mentioned cities
                                            PreparedStatement stm_l=conn1.prepareStatement("select latitude,longitude from worldcities where city=?");
                                            stm_l.setString(1,src_c);
                                            ResultSet s_=stm_l.executeQuery();
                                            while (s_.next()){
                                                lat_s=s_.getString("latitude");
                                                lon_s=s_.getString("longitude");







                                                s_.next();
                                            }
                                            String str2 = "insert into worldcities(city,latitude,longitude) values(?,?,?)";
                                            PreparedStatement stm=conn1.prepareStatement(str2);
                                            stm.setString(1,src_l);
                                            stm.setString(2,lat_s);
                                            stm.setString(3,lon_s);
                                            stm.executeUpdate();
                                            String lat = null;
                                            String lon = null;

                                            // for destination label part
                                            PreparedStatement stm_d=conn1.prepareStatement("select latitude,longitude from worldcities where city=?");
                                            stm_d.setString(1,dst_c);
                                            ResultSet d_=stm_d.executeQuery();
                                            while (d_.next()){
                                                lat=d_.getString("latitude");
                                                lon=d_.getString("longitude");







                                                d_.next();
                                            }
                                            String str3 = "insert into worldcities values(?,?,?)";
                                            PreparedStatement stm2=conn1.prepareStatement(str3);
                                            stm2.setString(1,dst_l);
                                            stm2.setString(2,lat);
                                            stm2.setString(3,lon);
                                            stm2.executeUpdate();




                                            System.out.printf("your favorite stops are added enjoy the trip\n");


                                        }
                                        else if (ds==2) {
                                            // deleting user account
                                            PreparedStatement stm2=conn1.prepareStatement("delete from registration where password=? and mob_no=?");
                                            stm2.setString(1,pwd);
                                            stm2.setString(2,mob_no);
                                            stm2.execute();
                                            System.out.printf("your account is succesfully deleted\n");



                                        }


                                    } else{break;
                                    }}
                                if(ds!=2){

                                    // NOW CALLING THE FUNCTIONALITY
                                    Scanner so = new Scanner(System.in);
                                    Scanner sop = new Scanner(System.in);
                                    System.out.printf("enter the source city:-->\n");

                                    source = so.next();
                                    System.out.printf("enter the pickup point :-\n");
                                    source_address=sop.nextLine();
                                    System.out.printf("enter the destination  city:-->\n");
                                    destination = so.next();
                                    System.out.printf("enter your drop off point:-\n");
                                    destination_address=sop.nextLine();}
                                else{System.out.printf("your account not found please make an account\n");
                                    break;}



                                // resting it till further action
                                payment pe = new payment(source, destination);
                                double fare_check=pe.get_payment(1,source,destination);
                                if (fare_check>1){
                                    Integer i;
                                    System.out.printf("vehicle type \t fare(rupees) \n");
                                    for (i = 1; i <= 4; i++) {
                                        double show = pe.get_payment(i,source,destination);
                                        switch (i) {
                                            case 1: {
                                                System.out.printf(" 1 -> MINI(GO) \t %f\n", show);
                                                break;

                                            }
                                            case 2: {
                                                System.out.printf(" 2 ->XL \t \t%f\n", show);
                                                break;
                                            }
                                            case 3: {
                                                System.out.printf(" 3 -> premium \t %f\n", show);
                                                break;
                                            }
                                            case 4: {
                                                System.out.printf(" 4-> force/bus \t %f\n", show);
                                                break;
                                            }

                                        }

                                    }}
                                else{
                                    System.out.printf("WARNING : please enter valid source or destination city\n");
                                    break;
                                }
                                Scanner bk = new Scanner(System.in);

                                System.out.printf("-----------------[Please select the cab you want to book]-----------------\n");
                                System.out.printf("1 > MINI(GO)\n 2->XL \n 3-> premium\n 4->book a bus/force\n");
                                Integer dr_id;
                                feeder f = new feeder(source, destination);
                                Integer booking_count = 0;
//                                Integer count = 0;

                                Scanner ctr=new Scanner(System.in);


                                choice = ctr.nextInt();
                                switch (choice) {
                                    case 1: {

                                        int c;

                                        // showing the fare details for selected car
                                        System.out.printf("hello %s you have choosed mini(go) for your trip\n the fare details for your trip are shown below\n", name);
                                        System.out.printf(" 1 -> MINI(GO) \t %f\n", int_call.get_payment(choice,source,destination));
                                        System.out.printf("%s--->%s\n",source,destination);
                                        System.out.printf("Search a cab \n1 -> to start search\n 2-> to abort booking/go back\n");
                                        c = bk.nextInt();
                                        //FOR OFFER PURPOSE A BOOKING_COUNT MUST BE ADDED
                                        if (c == 1) {
                                            // assigning driver id's randomly
                                            // String driver_id=rand.nextInt()
                                            driver dr = new driver();
                                            booking_info b_info = new booking_info();
                                            System.out.printf("searching for nearby available drivers\n");
                                            System.out.printf("searching...\n");
                                            try {System.out.printf("wait...\n");

                                                //System.out.printf("Start Time: %s\n", LocalTime.now());
                                                Thread.sleep(8 * 1000); // Wait for 10 seconds
                                                //System.out.printf("End Time: %s\n", LocalTime.now());
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            dr_id = dr.getDriver_id();
                                            Random r = new Random();
                                            int l = r.nextInt(l1.toArray().length);// re
                                            String name_d = dr.select_driver();

                                            booking_id = b_info.get_booking_id();
                                            PreparedStatement stm12 = conn1.prepareStatement("select Mob_no from driver where name=? limit 1");
                                            stm12.setString(1,name_d);
                                            ResultSet mob=stm12.executeQuery();
                                            String dr_mob = null;
                                            while(mob.next()){
                                                dr_mob=mob.getString("Mob_No");
                                                mob.next();

                                            }

                                            // System.out.printf("driver found\n");
                                            //dr.select_driver();
                                            System.out.printf("\ndriver name-%s\t driver id-%d\t Mob_no-%s \n", name_d, dr_id,dr_mob);
                                            System.out.printf("hi %s your booking is confirmed your driver is reaching you shortly\nplease wait\n", name);
                                            System.out.printf("your total distance of journey is nearly %f km\n",(int_call.extractor_and_calculator(source, destination))/1000);

                                            System.out.printf("your estimated time to reach your destination is % f hours\n", ((int_call.extractor_and_calculator(source, destination)) / 60)/1000);
                                            PreparedStatement stm4 = conn1.prepareStatement("insert into driver_info values(?,?,?,?)");
                                            stm4.setInt(1,booking_id);  // note that column names are same as parameter setters  so to avoid confusion use them only
                                            stm4.setInt(2,dr_id);
                                            stm4.setString(3,name_d);
                                            stm4.setString(4,int_call.vehicle_type(choice));

                                            stm4.execute();// to check *
                                            //columns are  for booking relation -->bK_id/rider/mob_no/pwd/booking_status/rating
                                            //int book_c=booking_count++;

                                            PreparedStatement stm5 = conn1.prepareStatement("insert into cabbooking values(?,?,?,?,?,'not rated',?,?,?,?,?,?)");
                                            // adding booking status also
                                            // some bug fixes are pending in stm5
                                            stm5.setInt(1,booking_id);
                                            stm5.setString(2,name);
                                            stm5.setString(3,mob_no);
                                            stm5.setString(4,pwd);
                                            stm5.setString(5,b_info.booking_status(c));
                                            stm5.setInt(6,booking_count);
                                            stm5.setString(7,source);
                                            stm5.setString(8,destination);
                                            stm5.setString(9,source_address);
                                            stm5.setString(10,destination_address);
                                            stm5.setInt(11, (int) int_call.get_payment(choice,source,destination));

                                            //Kernel;

                                            // altered the parameter index


                                            stm5.execute();
                                            // getting booking count from previous booking
                                            PreparedStatement stm7 = conn1.prepareStatement("select booking_count from cabbooking where mob_no like ? and password= ? order by booking_count DESC limit 1");


                                            //ResultSet result = stm7.executeQuery();
                                            stm7.setString(1,mob_no);
                                            stm7.setString(2,pwd);

                                            ResultSet result = stm7.executeQuery();
                                            while(result.next()){
                                                count = result.getInt("booking_count") + 1;
                                                result.next();}

                                            //now we can initiate booking offers from this


                                            // updating the booking count of new booking
                                            //PreparedStatement stm6 = conn1.prepareStatement("update cabbooking set booking_count=" + count + " where booking_id=" + booking_id + " and rider=" + name + " and password=" + pwd + "");
                                            PreparedStatement stm6 = conn1.prepareStatement("update cabbooking set booking_count= ? where booking_id=?  and password= ?");
                                            stm6.setInt(1,count);
                                            stm6.setInt(2,booking_id);
                                            stm6.setString(3,pwd);

                                            // adding booking status also

                                            stm6.execute();
                                            // adding booking status also




                                            // ResultSet IN = stm4.executeQuery();


//                                       reach your destination is % f hours\n", ((f.extractor_and_calculator(source, destination)) / 60)/1000);
                                            if (count==5 || count==10 || count==20){
                                                int dis= (int) Math.round(pe.get_payment(choice,source,destination))*20/100;
                                                System.out.printf("SPECIAL OFFER APPLIED ON YOUR %d booking enjoy",count);

                                                System.out.printf("please pay your driver rupees %d\n", Math.round(int_call.get_payment(choice,source,destination))-dis);}
                                            else{
                                                System.out.printf("please pay your driver rupees %d\n", Math.round(int_call.get_payment(choice,source,destination)));
                                            }


                                            System.out.printf("would you like to rate the trip\n PRESS 1 OR 0\n");
                                            Scanner rat = new Scanner(System.in);
                                            int rating = rat.nextInt();
                                            if (rating == 1) {
                                                System.out.printf("how was your experience\n 1->very poor\n 2->poor\n 3->satisfactory\n 4->good\n5->very good\n");
                                                int rat_v = rat.nextInt();

                                                PreparedStatement stm3 = conn1.prepareStatement("update cabbooking set rating=? where booking_id= ? and password= ?");
                                                stm3.setInt(1,rat_v);
                                                stm3.setInt(2,booking_id);
                                                stm3.setString(3,pwd);
                                                stm3.execute();
                                                if(rat_v==1 || rat_v==2){
                                                    System.out.printf("would you like to give us suggestions :-\n");
                                                    int suggest=rat.nextInt();
                                                    if(suggest==1){
                                                        System.out.printf("please enter your suggedtions :--\n");
                                                        Scanner sug=new Scanner(System.in);
                                                        String su=sug.nextLine();
                                                        suggestion_box.add(su);

                                                    }
                                                }

                                                System.out.printf("thanks for feedback ride again\n");

                                            } else {
                                                System.out.printf("thanks  ride again\n");
                                                break;
                                            }



                                        }
                                        else {
                                            System.out.printf("BOOKING ABORTED\n");
                                            break;
                                        }
                                        break;

                                    }
                                    case 2: { int c;

                                        // showing the fare details for selected car
                                        System.out.printf("hello %s you have choosed XL for your trip\n the fare details for your trip are shown below\n", name);
                                        System.out.printf(" 1 -> XL \t %f\n", pe.get_payment(choice,source,destination));
                                        System.out.printf("%s--->%s\n",source,destination);
                                        System.out.printf("Search a cab \n1 -> to start search\n 2-> to abort booking/go back\n");
                                        c = bk.nextInt();
                                        //FOR OFFER PURPOSE A BOOKING_COUNT MUST BE ADDED
                                        if (c == 1) {
                                            // assigning driver id's randomly
                                            // String driver_id=rand.nextInt()
                                            driver dr = new driver();
                                            booking_info b_info = new booking_info();
                                            System.out.printf("searching for nearby available drivers\n");
                                            System.out.printf("searching...\n");
                                            try {System.out.printf("wait...\n");

                                                //System.out.printf("Start Time: %s\n", LocalTime.now());
                                                Thread.sleep(10 * 1000); // Wait for 10 seconds
                                                //System.out.printf("End Time: %s\n", LocalTime.now());
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            dr_id = dr.getDriver_id();
                                            Random r = new Random();
                                            int l = r.nextInt(l1.toArray().length);// re
                                            String name_d = dr.select_driver();
                                            booking_id = b_info.get_booking_id();
                                            PreparedStatement stm12 = conn1.prepareStatement("select Mob_no from driver where name=? limit 1");
                                            stm12.setString(1,name_d);
                                            ResultSet mob=stm12.executeQuery();
                                            String dr_mob = null;
                                            while(mob.next()){
                                                dr_mob=mob.getString("Mob_No");
                                                mob.next();

                                            }

                                            // System.out.printf("driver found\n");
                                            //dr.select_driver();
                                            System.out.printf("\ndriver name-%s\t driver id-%d\t Mob_no-%s \n", name_d, dr_id,dr_mob);


                                            // System.out.printf("driver found\n");
                                            //dr.select_driver();
                                            // System.out.printf("\ndriver name-%s\t driver id-%d\t \n", name_d, dr_id);
                                            System.out.printf("hi %s your booking is confirmed your driver is reaching you shortly\nplease wait\n", name);
                                            System.out.printf("your total distance of journey is nearly %f km\n",(int_call.extractor_and_calculator(source, destination))/1000);
                                            System.out.printf("your estimated time to reach your destination is % f hours\n", ((f.extractor_and_calculator(source, destination)) / 60)/1000);
                                            PreparedStatement stm4 = conn1.prepareStatement("insert into driver_info values(?,?,?,?)");
                                            stm4.setInt(1,booking_id);  // note that column names are same as parameter setters  so to avoid confusion use them only
                                            stm4.setInt(2,dr_id);
                                            stm4.setString(3,name_d);
                                            stm4.setString(4,b_info.vehicle_type(choice));

                                            stm4.execute();// to check *
                                            //columns are  for booking relation -->bK_id/rider/mob_no/pwd/booking_status/rating

//
//                                        // adding booking status also
//
//                                        stm5.execute();
                                            PreparedStatement stm5 = conn1.prepareStatement("insert into cabbooking values(?,?,?,?,?,'not rated',?,?,?,?,?,?)");
                                            // adding booking status also
                                            // some bug fixes are pending in stm5
                                            stm5.setInt(1,booking_id);
                                            stm5.setString(2,name);
                                            stm5.setString(3,mob_no);
                                            stm5.setString(4,pwd);
                                            stm5.setString(5,b_info.booking_status(c));
                                            stm5.setInt(6,booking_count);
                                            stm5.setString(7,source);
                                            stm5.setString(8,destination);
                                            stm5.setString(9,source_address);
                                            stm5.setString(10,destination_address);
                                            stm5.setInt(11, (int) pe.get_payment(choice,source,destination));

                                            //Kernel;

                                            // altered the parameter index


                                            stm5.execute();
                                            // getting booking count from previous booking
                                            PreparedStatement stm7 = conn1.prepareStatement("select booking_count from cabbooking where mob_no like ? and password=? order by booking_count DESC limit 1");

                                            stm7.setString(1,mob_no);
                                            stm7.setString(2,pwd);


                                            ResultSet result = stm7.executeQuery();
                                            while (result.next()){
                                                count = result.getInt("booking_count") + 1;
                                                result.next();}
                                            //now we can initiate booking offers from this


                                            // updating the booking count of new booking
                                            PreparedStatement stm6 = conn1.prepareStatement("update cabbooking set booking_count= ? where booking_id= ? and rider=? and password=?");
                                            stm6.setInt(1,count);
                                            stm6.setInt(2,booking_id);
                                            stm6.setString(3,name);
                                            stm6.setString(4,pwd);


                                            // adding booking status also

                                            stm6.execute();


                                            // ResultSet IN = stm4.executeQuery();



                                            if (count==5 || count==10 || count==20){
                                                int dis= (int) Math.round(pe.get_payment(choice,source,destination))*15/100;
                                                System.out.printf("SPECIAL OFFER APPLIED ON YOUR %d booking enjoy\n",count);

                                                System.out.printf("please pay your driver rupees %d\n", Math.round(pe.get_payment(choice,source,destination))-dis);}
                                            else{
                                                System.out.printf("please pay your driver rupees %d\n", Math.round(pe.get_payment(choice,source,destination)));
                                            }


                                            System.out.printf("would you like to rate the trip\n PRESS 1 OR 0\n");
                                            Scanner rat = new Scanner(System.in);
                                            int rating = rat.nextInt();
                                            if (rating == 1) {
                                                System.out.printf("how was your experience\n 1->very poor\n 2->poor\n 3->satisfactory\n 4->good\n5->very good\n");
                                                int rat_v = rat.nextInt();
                                                PreparedStatement stm3 = conn1.prepareStatement("update cabbooking set rating=? where booking_id= ? and password= ?");
                                                stm3.setInt(1,rat_v);
                                                stm3.setInt(2,booking_id);
                                                stm3.setString(3,pwd);
                                                stm3.execute();

                                                System.out.printf("thanks for feedback ride again\n");

                                            } else {
                                                System.out.printf("thanks  ride again\n");
                                                break;
                                            }



                                        }
                                        else {
                                            System.out.printf("BOOKING ABORTED\n");
                                            break;

                                        }
                                        break;


                                    }
                                    case 3:{
                                        int c;

                                        // showing the fare details for selected car
                                        System.out.printf("hello %s you have selected premium for your trip\n the fare details for your trip are shown below\n", name);
                                        System.out.printf(" 1 -> premium \t %f\n", pe.get_payment(choice,source,destination));
                                        System.out.printf("%s--->%s\n",source,destination);
                                        System.out.printf("Search a cab \n 1 -> to start search\n 2-> to abort booking/go back\n");
                                        c = bk.nextInt();
                                        //FOR OFFER PURPOSE A BOOKING_COUNT MUST BE ADDED
                                        if (c == 1) {
                                            // assigning driver id's randomly
                                            // String driver_id=rand.nextInt()
                                            driver dr = new driver();
                                            booking_info b_info = new booking_info();
                                            System.out.printf("searching for nearby available drivers\n");
                                            System.out.printf("searching...\n");
                                            try {System.out.printf("wait...\n");

                                                //System.out.printf("Start Time: %s\n", LocalTime.now());
                                                Thread.sleep(10 * 1000); // Wait for 10 seconds
                                                //System.out.printf("End Time: %s\n", LocalTime.now());
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            dr_id = dr.getDriver_id();
                                            Random r = new Random();
                                            int l = r.nextInt(l1.toArray().length);// re
                                            String name_d = dr.select_driver();
                                            booking_id = b_info.get_booking_id();
                                            PreparedStatement stm12 = conn1.prepareStatement("select Mob_no from driver where name=? limit 1");
                                            stm12.setString(1,name_d);
                                            ResultSet mob=stm12.executeQuery();
                                            String dr_mob = null;
                                            while(mob.next()){
                                                dr_mob=mob.getString("Mob_No");
                                                mob.next();

                                            }

                                            // System.out.printf("driver found\n");
                                            //dr.select_driver();
                                            System.out.printf("\ndriver name-%s\t driver id-%d\t Mob_no-%s \n", name_d, dr_id,dr_mob);


                                            // System.out.printf("driver found\n");
                                            //dr.select_driver();
                                            //System.out.printf("\ndriver name-%s\t driver id-%d\t \n", name_d, dr_id);
                                            System.out.printf("hi %s your booking is confirmed your driver is reaching you shortly\nplease wait\n", name);
                                            System.out.printf("your total distance of journey is nearly %f km\n",(int_call.extractor_and_calculator(source, destination))/1000);
                                            System.out.printf("your estimated time to reach your destination is % f hours\n", ((f.extractor_and_calculator(source, destination)) / 60)/1000);
                                            PreparedStatement stm4 = conn1.prepareStatement("insert into driver_info values(?,?,?,?)");
                                            stm4.setInt(1,booking_id);  // note that column names are same as parameter setters  so to avoid confusion use them only
                                            stm4.setInt(2,dr_id);
                                            stm4.setString(3,name_d);
                                            stm4.setString(4,b_info.vehicle_type(choice));

                                            stm4.execute();// to check *
                                            //columns are  for booking relation -->bK_id/rider/mob_no/pwd/booking_status/rating
                                            //int book_c=booking_count++;

                                            // PreparedStatement stm5 = conn1.prepareStatement("insert into cabbooking values(" + booking_id + "," + name + "," + mob_no + "," + pwd + "," + b_info.booking_status(c) + ",'not rated'," + booking_count + ")");
                                            // adding booking status also

                                            //stm5.executeQuery();
                                            PreparedStatement stm5 = conn1.prepareStatement("insert into cabbooking values(?,?,?,?,?,'not rated',?,?,?,?,?,?)");
                                            // adding booking status also
                                            // some bug fixes are pending in stm5
                                            stm5.setInt(1,booking_id);
                                            stm5.setString(2,name);
                                            stm5.setString(3,mob_no);
                                            stm5.setString(4,pwd);
                                            stm5.setString(5,b_info.booking_status(c));
                                            stm5.setInt(6,booking_count);
                                            stm5.setString(7,source);
                                            stm5.setString(8,destination);
                                            stm5.setString(9,source_address);
                                            stm5.setString(10,destination_address);
                                            stm5.setInt(11, (int) pe.get_payment(choice,source,destination));

                                            //Kernel;

                                            // altered the parameter index


                                            stm5.execute();

                                            // getting booking count from previous booking
                                            PreparedStatement stm7 = conn1.prepareStatement("select booking_count from cabbooking where mob_no like ? and password= ? order by booking_count DESC limit 1");
                                            stm7.setString(1,mob_no);
                                            stm7.setString(2,pwd);

                                            ResultSet result = stm7.executeQuery();
                                            while(result.next()){
                                                count = result.getInt("booking_count") + 1;
                                                result.next();}
                                            //now we can initiate booking offers from this


                                            // updating the booking count of new booking
                                            PreparedStatement stm6 = conn1.prepareStatement("update cabbooking set booking_count= ? where booking_id=?  and password= ?");
                                            stm6.setInt(1,count);
                                            stm6.setInt(2,booking_id);
                                            stm6.setString(3,pwd);

                                            // adding booking status also

                                            stm6.execute();


                                            // ResultSet IN = stm4.executeQuery();



                                            if (count==5 || count==10 || count==20){
                                                int dis= (int) Math.round(pe.get_payment(choice,source,destination))*13/100;
                                                System.out.printf("SPECIAL OFFER APPLIED ON YOUR %d booking enjoy\n",count);

                                                System.out.printf("please pay your driver rupees %d\n", Math.round(pe.get_payment(choice,source,destination))-dis);}
                                            else{
                                                System.out.printf("please pay your driver rupees %d\n", Math.round(pe.get_payment(choice,source,destination)));
                                            }


                                            System.out.printf("would you like to rate the trip\n PRESS 1 OR 0\n");
                                            Scanner rat = new Scanner(System.in);
                                            int rating = rat.nextInt();
                                            if (rating == 1) {
                                                System.out.printf("how was your experience\n 1->very poor\n 2->poor\n 3->satisfactory\n 4->good\n5->very good\n");
                                                int rat_v = rat.nextInt();
//                                    PreparedStatement stm3 = conn1.prepareStatement("update cabbooking set rating=" + rat_v + " where rider=" + name + " and password=" + pwd + "");
//
//                                    stm3.executeQuery();
                                                PreparedStatement stm3 = conn1.prepareStatement("update cabbooking set rating=? where booking_id= ? and password= ?");
                                                stm3.setInt(1,rat_v);
                                                stm3.setInt(2,booking_id);
                                                stm3.setString(3,pwd);
                                                stm3.execute();

                                                System.out.printf("thanks for feedback ride again\n");

                                            } else {
                                                System.out.printf("thanks  ride again\n");
                                                break;
                                            }



                                        }
                                        else {
                                            System.out.printf("BOOKING ABORTED\n");
                                            break;

                                        }
                                        break;

                                    }
                                    case 4:{
                                        int c;

                                        // showing the fare details for selected car
                                        System.out.printf("hello %s you have choosed Bus/force for your trip\n the fare details for your trip are shown below\n", name);
                                        System.out.printf(" 1 -> bus/force \t %f\n", pe.get_payment(choice,source,destination));
                                        System.out.printf("%s--->%s\n",source,destination);
                                        System.out.printf("Search a cab \n1 -> to start search\n 2-> to abort booking/go back\n");
                                        c = bk.nextInt();
                                        //FOR OFFER PURPOSE A BOOKING_COUNT MUST BE ADDED
                                        if (c == 1) {
                                            // assigning driver id's randomly
                                            // String driver_id=rand.nextInt()
                                            driver dr = new driver();
                                            booking_info b_info = new booking_info();
                                            System.out.printf("searching for nearby available drivers\n");
                                            System.out.printf("searching...\n");
                                            try {System.out.printf("wait...\n");

                                                //System.out.printf("Start Time: %s\n", LocalTime.now());
                                                Thread.sleep(10 * 1000); // Wait for 10 seconds
                                                //System.out.printf("End Time: %s\n", LocalTime.now());
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            dr_id = dr.getDriver_id();
                                            Random r = new Random();
                                            int l = r.nextInt(l1.toArray().length);// re
                                            String name_d = dr.select_driver();
                                            booking_id = b_info.get_booking_id();
                                            PreparedStatement stm12 = conn1.prepareStatement("select Mob_no from driver where name=? limit 1");
                                            stm12.setString(1,name_d);
                                            ResultSet mob=stm12.executeQuery();
                                            String dr_mob = null;
                                            while(mob.next()){
                                                dr_mob=mob.getString("Mob_No");
                                                mob.next();

                                            }

                                            // System.out.printf("driver found\n");
                                            //dr.select_driver();
                                            System.out.printf("\ndriver name-%s\t driver id-%d\t Mob_no-%s \n", name_d, dr_id,dr_mob);


                                            // System.out.printf("driver found\n");
                                            //dr.select_driver();
                                            //System.out.printf("\ndriver name-%s\t driver id-%d\t \n", name_d, dr_id);
                                            System.out.printf("hi %s your booking is confirmed your driver is reaching you shortly\nplease wait\n", name);
                                            System.out.printf("your total distance of journey is nearly %f km\n",(int_call.extractor_and_calculator(source, destination))/1000);
                                            System.out.printf("your estimated time to reach your destination is % f hours\n", ((f.extractor_and_calculator(source, destination)) / 60)/1000);
                                            PreparedStatement stm4 = conn1.prepareStatement("insert into driver_info values(?,?,?,?)");
                                            stm4.setInt(1,booking_id);  // note that column names are same as parameter setters  so to avoid confusion use them only
                                            stm4.setInt(2,dr_id);
                                            stm4.setString(3,name_d);
                                            stm4.setString(4,b_info.vehicle_type(choice));

                                            stm4.execute();// to check *
                                            //columns are  for booking relation -->bK_id/rider/mob_no/pwd/booking_status/rating
                                            //int book_c=booking_count++;

//                                    PreparedStatement stm5 = conn1.prepareStatement("insert into cabbooking values(" + booking_id + "," + name + "," + mob_no + "," + pwd + "," + b_info.booking_status(c) + ",'not rated'," + booking_count + ")");
//                                    // adding booking status also
//
//                                    s
                                            PreparedStatement stm5 = conn1.prepareStatement("insert into cabbooking values(?,?,?,?,?,'not rated',?,?,?,?,?,?)");
                                            // adding booking status also
                                            // some bug fixes are pending in stm5
                                            stm5.setInt(1,booking_id);
                                            stm5.setString(2,name);
                                            stm5.setString(3,mob_no);
                                            stm5.setString(4,pwd);
                                            stm5.setString(5,b_info.booking_status(c));
                                            stm5.setInt(6,booking_count);
                                            stm5.setString(7,source);
                                            stm5.setString(8,destination);
                                            stm5.setString(9,source_address);
                                            stm5.setString(10,destination_address);
                                            stm5.setInt(11, (int) pe.get_payment(choice,source,destination));

                                            //Kernel;

                                            // altered the parameter index


                                            stm5.execute();

                                            // getting booking count from previous booking
                                            PreparedStatement stm7 = conn1.prepareStatement("select booking_count from cabbooking where mob_no like ? and password= ? order by booking_count DESC limit 1");
                                            stm7.setString(1,mob_no);
                                            stm7.setString(2,pwd);

                                            ResultSet result = stm7.executeQuery();
                                            while(result.next()){
                                                count = result.getInt("booking_count") + 1;
                                                result.next();}

//

                                            // updating the booking count of new booking
//                                    PreparedStatement stm6 = conn1.prepareStatement("update cabbooking set booking_count=" + count + " where booking_id=" + booking_id + " and rider=" + name + " and password=" + pwd + "");
//
//                                    // adding booking status also
//
//                                    stm6.executeQuery();
                                            PreparedStatement stm6 = conn1.prepareStatement("update cabbooking set booking_count= ? where booking_id=?  and password= ?");
                                            stm6.setInt(1,count);
                                            stm6.setInt(2,booking_id);
                                            stm6.setString(3,pwd);

                                            // adding booking status also

                                            stm6.execute();


                                            // ResultSet IN = stm4.executeQuery();



                                            if (count==5 || count==10 || count==20){
                                                int dis= (int) Math.round(pe.get_payment(choice,source,destination))*18/100;
                                                System.out.printf("SPECIAL OFFER APPLIED ON YOUR %d th booking enjoy\n",count);

                                                System.out.printf("please pay your driver rupees %d\n", Math.round(pe.get_payment(choice,source,destination))-dis);}
                                            else{
                                                System.out.printf("please pay your driver rupees %d\n", Math.round(pe.get_payment(choice,source,destination)));
                                            }


                                            System.out.printf("would you like to rate the trip\n PRESS 1 OR 0\n");
                                            Scanner rat = new Scanner(System.in);
                                            int rating = rat.nextInt();
                                            if (rating == 1) {
                                                System.out.printf("how was your experience\n 1->very poor\n 2->poor\n 3->satisfactory\n 4->good\n5->very good\n");
                                                int rat_v = rat.nextInt();


                                                PreparedStatement stm3 = conn1.prepareStatement("update cabbooking set rating=? where booking_id= ? and password= ?");
                                                stm3.setInt(1,rat_v);
                                                stm3.setInt(2,booking_id);
                                                stm3.setString(3,pwd);
                                                stm3.execute();

                                                System.out.printf("thanks for feedback ride again\n");

                                            } else {
                                                System.out.printf("thanks  ride again\n");
                                                break;
                                            }



                                        }

                                        else {
                                            System.out.printf("BOOKING ABORTED\n");
                                            break;

                                        }
                                        break;
                                    }
                                    default: {
                                        System.out.printf("select right choice\n");


                                    }

                                }


                            }else {pt=1;}
                            res.next();
                            res1.next();
//                    System.out.printf("would you like to book another trip\n");
//                    Scanner nt=new Scanner(System.in);
//                    int cn=nt.nextInt();
//                    if(cn==1){break;
//                    }
                            System.out.printf("1--> if you want to go for management part\n");
                            break;
                        }
                    }
                    if (flag==0){
                        System.out.printf("BOOKING COMPLETED\n");
                    } else if (flag==2) {
                        System.out.printf("<-------------------------------------------------------------------------->\n");

                    } else {

//                        flag = 1;
//                        //login_pointer=1;
//                        //break;

                        System.out.printf("PLEASE REGISTER\n");
                        break;

                    }
//                            res.next();
//                            res1.next();
                    Integer condition=check;
                    check=1;

                    while(true){
                        // login_pointer==1 is ommited here for maintenance purpose
                        if( login_pointer==1 && check==1 && ds!=2){
                            System.out.printf("\nWOULD you like to manage your bookings\n");
                            System.out.printf(" 1--> show your bookings\n2-->cancel your current booking\n3--> add a new driver or register yourself as a driver\n4-->Edit your destination stop \n 5--> exit\n");
                            Scanner set = new Scanner(System.in);
                            Scanner set_a = new Scanner(System.in);


                            int man_g=set.nextInt();
                            management_of_bookings manage=new management_of_bookings();
                            // to edit the destination stop
                            double fare_d;
                            Integer fare;

                            switch (man_g){
                                case 1:{manage.show_bookings(name,pwd);

                                    break;


                                }
//                                ;
//                                }
                                case 2:{
                                    manage.cancel_booking(booking_id,count,source,destination);// via this it assures that user is not able to cancel booking if he is logged off or take a another booking( in other words only current
                                    // booking is allowed to cancel)
//                                    System.out.printf("your current trip from %s to %s is cancelled ",source,destination);
                                    break;
                                }
                                case 3:{
                                    //incomplete
                                    System.out.printf("enter driver name\n");
                                    String n= set.next();
                                    l1.add(n);
                                    System.out.printf("driver registered\n");
                                    break;

                                }
                                case 4:{
                                    System.out.printf("enter your new destination\n");
                                    String dest=set.next();
                                    System.out.printf("enter your new drop off point\n");
                                    String dest_a=set_a.nextLine();

                                    fare_d=pn.get_payment(choice,source,dest);
                                    fare= Math.toIntExact(Math.round(fare_d));



                                    destination= manage.edit_booking(booking_id,source,destination,dest,fare,dest_a);


                                    break;

                                }
                                case 5:{
                                    check=0;
                                    break;

                                }
                                default:{
                                    System.out.printf("select right choice\n");
                                }

                            }}
                        else {
                            check=0;
                            System.out.printf("PLEASE REGISTER\n");
                            // System.out.printf("")
                            break;
                        }}
//



                }// end of booking


            } finally {
                System.out.printf("AN INTERNAL ERROR OCCURED or EXIT \n");

            }
        }
//            catch(SQLException ex){
//              System.out.printf("sql exception");
//            }




    }



//        }
}

//}
