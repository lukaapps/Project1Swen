package automail;

import com.unimelb.swen30006.wifimodem.WifiModem;
import java.lang.*;

public class Charge {

    /** Details about a Charge **/

    private double serviceFee = 0;
    private double maintenanceCost = 0;
    private double operatingTime = 0;

    private WifiModem modem;


    public Charge(WifiModem modem) {
        this.modem = modem;
    }

    public String bill(MailItem mailItem){
            int floor = mailItem.getDestFloor();
            double charge = this.getCharge(floor);
            double maintenanceCost = this.getMaintenanceCost();
            double serviceFee = this.serviceFee;
            return String.format(" | Service Fee: %4f | Maintenance: %2f | Avg. Operating Time: %4f | Total Charge: %4f",
                     serviceFee, maintenanceCost, operatingTime, charge);
    }


    public void setServiceFee(int floor) {
        double serviceFee = this.modem.forwardCallToAPI_LookupPrice(floor);
        if (serviceFee >= 0) {
            this.serviceFee = serviceFee;
        }
    }

    public double getServiceFee() {
        return this.serviceFee;
    }

    public void setMaintenanceCost(int floor) {
    }

    public double getMaintenanceCost() {
        return  this.maintenanceCost;
    }



    //TOTAL CHARGE
    public double getCharge(int floor) {
        System.out.println(15);
        System.out.println(modem.forwardCallToAPI_LookupPrice(10));
        this.setServiceFee(floor);
        setMaintenanceCost(floor);
        return this.getServiceFee() + this.getMaintenanceCost();
    }



}
