package classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class driver {
    // getting the driver id
    public List<Object> name_driver = new ArrayList<>();
    public List<Object> distance_driver = new ArrayList<>();
    public List<Object> time_driver = new ArrayList<>();


    Random r = new Random();
    private Integer driver_id = r.nextInt(10000);

    public String select_driver() throws SQLException {
        // to show possible 3-4 nearest drivers and pickup one that accepts
        Connection conn3 = null;
        String url1 = "jdbc:mysql://localhost:3306/neelesh";
        String user = "root";
        String password = "Darshil@2306";


        conn3 = DriverManager.getConnection(url1, user, password);
        PreparedStatement stm5 = conn3.prepareStatement("select * from driver");
        ResultSet res = stm5.executeQuery();
        while (res.next()) {
            name_driver.add(res.getString("name"));
            distance_driver.add(res.getString("Distance"));
            time_driver.add(res.getString("Time"));


        }
        System.out.printf("3 drivers found near by\n");
        Integer i = r.nextInt(47);
        Integer k = r.nextInt(3);
        for (int j = 0; j < 3; j++) {
            String name_d = (String) name_driver.get(i + j);
            String dist = (String) distance_driver.get(i + j);
            String time = (String) time_driver.get(i + j);
            System.out.printf("%s\t%s\t%s\n", name_d, dist, time);

        }
        try {
            System.out.printf("requsesting ...\n");

            //System.out.printf("Start Time: %s\n", LocalTime.now());
            Thread.sleep(4 * 1000); // Wait for 4 seconds
            //System.out.printf("End Time: %s\n", LocalTime.now());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s accepted your trip and is %s away arriving in %s", (String) name_driver.get(i + k), (String) distance_driver.get(i + k), (String) time_driver.get(i + k));
        return (String) name_driver.get(i + k);


    }


    public Integer getDriver_id() {
        return driver_id;
    }
    // driver name has been described in main class
    // for searching a driver based on time
    // function pending


}
