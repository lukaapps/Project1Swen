package automail;

import com.unimelb.swen30006.wifimodem.WifiModem;

//import java.awt.Robot;
import java.lang.*;
import automail.Robot;
import util.Configuration;

public class Charge {

    /** Details about a Charge **/
    private double fastService =0;
    private double bulkService = 0;
    private double normalService = 0;
    private double serviceFee = 0;
    private double maintenanceCost = 0;
    private double avgOperatingTime = 0;
    private static boolean feeCharging = false;


    public WifiModem modem;


    public Charge(WifiModem modem) {
        this.modem = modem;
    }

    public String bill(MailItem mailItem, Robot robot) throws Exception {
            int floor = mailItem.getDestFloor();
            double charge = this.getCharge(floor, robot);
            double maintenanceCost = this.getMaintenanceCost();
            double serviceFee = this.serviceFee;
            double avgOperatingTime = this.avgOperatingTime;

            if (feeCharging == true){
                return String.format(" | Service Fee: %.2f | Maintenance: %.2f | Avg. Operating Time: %.2f | Total Charge: %.2f",
                        serviceFee, maintenanceCost, avgOperatingTime, charge);
            }
            else {
                return "";
            }
    }

    public static void setFeeCharging (boolean status){
        Charge.feeCharging = status;
    }

    public void setServiceFee(int floor, Robot robot) throws Exception {
        WifiModem w = WifiModem.getInstance(floor);
        double serviceFee = w.forwardCallToAPI_LookupPrice(floor);
        if (serviceFee >= 0) {
            if(robot instanceof BulkRobot){
                this.bulkService = serviceFee;
                this.serviceFee = bulkService;

            }
            else if(robot instanceof FastRobot){
                this.fastService = serviceFee;
                this.serviceFee = fastService;

            }
            else{
                this.normalService = serviceFee;
                this.serviceFee = normalService;

            }
        }
        else{
            if(robot instanceof BulkRobot){
                this.serviceFee = bulkService;
            }
            else if(robot instanceof FastRobot){
                this.serviceFee = fastService;
            }
            else{
                this.serviceFee = normalService;
            }
        }
    }

    public double getServiceFee() {
        return this.serviceFee;
    }

    public void calculateMaintenanceCost(int floor, Robot robot) {
        if (robot instanceof BulkRobot) {
            BulkRobot.setAvgBulkOpTime();
            this.maintenanceCost = BulkRobot.getBULKRATE() * BulkRobot.getAvgBulkOpTime();
            this.avgOperatingTime = BulkRobot.getAvgBulkOpTime();
        }
        else if (robot instanceof FastRobot) {
            FastRobot.setAvgFastOpTime();
            this.maintenanceCost = FastRobot.getFASTRATE() * FastRobot.getAvgFastOpTime();
            this.avgOperatingTime = FastRobot.getAvgFastOpTime();
        }
        else  {
            NormalRobot.setAvgNormalOpTime();
            this.maintenanceCost = NormalRobot.getNORMALRATE() * NormalRobot.getAvgNormalOpTime();
            this.avgOperatingTime = NormalRobot.getAvgNormalOpTime();
        }
    }

    public double getMaintenanceCost() {
        return  this.maintenanceCost;
    }



    //TOTAL CHARGE
    public double getCharge(int floor, Robot robot) throws Exception {
        if(feeCharging) {
            setServiceFee(floor, robot);
            calculateMaintenanceCost(floor, robot);
            return this.getServiceFee() + this.getMaintenanceCost();
        }
        else{ return 0.0;}
    }



}
