package classes;

import java.sql.*;

public class feeder extends distance_calculation {
    public double final_d;
    //connection c=new connection();
    protected String source_i;
    protected String dest_i;


    public feeder(String s, String d) {
        this.source_i = s;
        this.dest_i = d;


    }
    connection conn2=new connection() {
        @Override
        public connection connector() throws SQLException {
            return super.connector();
        }
    };



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
        stm.setString(1, ("" + source + ""));
        ResultSet res = stm.executeQuery();

        PreparedStatement stm2 = conn2.prepareStatement("select latitude,longitude from worldcities WHERE city LIKE ? ");
        stm2.setString(1, ("" + dest + ""));
        ResultSet res2 = stm2.executeQuery();
        double final_d = 0;

        while (res.next() && res2.next()) {
            //String lastName = rs.getString("rs");

            double lat1 = res.getDouble("latitude");
            double lon1 = res.getDouble("longitude");
            double lat2 = res2.getDouble("latitude");
            double lon2 = res2.getDouble("longitude");

            //rs.getNextResultset();

            //distance_calculation dc=new distance_calculation(lat1,lon1,lat2,lon2);
            final_d = super.distance(lat1, lat2, lon1, lon2);


            res.next();
            res2.next();


            //rs.getNextResultset();

            //return final_d;
        }
        return final_d;

    }


}
