package classes;

import java.util.Random;

public class booking_info {
    Random rand = new Random();

    // Generate random integers in range 0 to 9999
    private int rand_int1 = rand.nextInt(10000);
    //int rand_int2 = rand.nextInt(10000);


    public Integer booking_id = rand_int1;

    public int get_booking_id() {
        return rand_int1;

    }

    public String booking_status(int st_pay) {
        if (st_pay == 1) {
            return "booking confirmed ";
        } else {
            return "not booked";
        }


    }


    public String vehicle_type(int choice) {
        if (choice == 1) {
            return "Mini(go)";

        } else if (choice == 2) {
            return "XL";


        } else if (choice == 3) {
            return "PREMIUM";

        } else {
            return "force/bus";

        }

    }

    public void get_name() {


    }
}
