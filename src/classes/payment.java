package classes;

import java.sql.SQLException;

public class payment extends feeder {
    String source_i;

    public payment(String s, String d) {
        super(s, d);
        // this.source_i=s;

    }


    //String source_i=s;

//    public payment(double lat1, double long1, double lat2, double long2) {
//        super(lat1, long1, lat2, long2);
//    }

    public double get_payment(int choice, String source_i, String dest_i) throws SQLException {
        double amount;
        switch (choice) {//
            case 1: {// fare of mini car $10 per km
                amount = super.extractor_and_calculator(source_i, dest_i) * 10 + 15;//tax and GSt included
                //return amount;
                break;

            }
            case 2: {
                // for XL
                amount = super.extractor_and_calculator(source_i, dest_i) * 17 + 15;//tax and GSt included
                //return amount;
                break;
            }
            case 3: {//for premium
                amount = super.extractor_and_calculator(source_i, dest_i) * 15 + 15;//tax and GSt included
                //return amount;
                break;

            }
            case 4: {// for force/bus
                amount = super.extractor_and_calculator(source_i, dest_i) * 23 + 15;//tax and GSt included
                //return amount;
                break;
            }
            default: {
                System.out.printf("enter valid choice");
                return 0;
            }


        }
        return (amount / 1000);

    }
}
