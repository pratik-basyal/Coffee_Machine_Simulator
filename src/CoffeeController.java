import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.SQLOutput;

public class CoffeeController implements Runnable {

    private final UserInput userInput;
    private final ValveController valveController;

    private final BrewController brewController;

    private final WeightCheck weightCheck;

    private final TemperatureInfo temperatureInfo;

    private float plateTemp;

    private AlertController alertController;

    private WaterInfo waterInfo;

    private Sound sound;

    public CoffeeController(UserInput userInput, ValveController valveController, BrewController brewController, WeightCheck weightCheck, TemperatureInfo temperatureInfo, AlertController alertController, WaterInfo waterInfo, Sound sound) {
        this.userInput = userInput;
        this.valveController = valveController;
        this.brewController = brewController;
        this.weightCheck = weightCheck;
        this.temperatureInfo = temperatureInfo;
        this.alertController = alertController;
        this.waterInfo = waterInfo;
        this.sound = sound;
    }

    @Override
    public void run() {
        int cupSize = userInput.getCupSize();
        int brewStrength = userInput.getBrewStrength();
        if (userInput.isBrewPressed()) {
            if(waterInfo.checkWaterLevelInfo(600, cupSize)){
                System.out.println("Water Level Passed.");
                alertController.displayMessage("START BREWING WILL LIGHT UP ICON..............");
                brewController.startHeater();

                int flowRate = valveController.setFlowRate(brewStrength);

                valveController.openValve(brewStrength);
                System.out.println("Plate Heater Activated");
                temperatureInfo.setPlateTemperature(100);
                plateTemp = temperatureInfo.getPlateTemp();

                System.out.println("Water Flow Rate = "+ flowRate + "ml/s.");

                float totalWeight = weightCheck.getTotalWeight(cupSize);
                float initialWeight = weightCheck.emptyPotWeight;

                do {
                    System.out.println("Current weight : " + initialWeight);
                    initialWeight += flowRate;
                    try {
                        Thread.sleep(1000);

                    }
                    catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                while (initialWeight != totalWeight);
                System.out.println("Current weight : " + initialWeight);
                System.out.println();
                alertController.displayMessage("COFFEE IS READY........");
                sound.playSound("/Users/pratik/Desktop/Courses/CS_361/src/CoffeMachineSimulator/sound/second-hand-149907.wav");
                System.out.println();
                System.out.println("Plate Heater set to: "+ plateTemp);


                if(userInput.coffeeReady()){
                    sound.stopSound();
                    System.out.println("COFFEE DELIVERED SUCCESSFULLY......");
                    System.out.println("Keep Warm Function Deactivated.......");
                    do {
                        System.out.println(plateTemp);
                        plateTemp -= 2;
                        try {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    while(plateTemp != temperatureInfo.roomTemp);
                    System.out.println("SUCCESS......");
                }
                else{
                    System.out.println("Your Coffee is being warmed by plate heater.......");
                }

            }
            else{
                alertController.displayMessage("WATER LEVEL LOW..........");
            }
        }
    }

    public static void main(String[] args) {
        CoffeeController coffeeController = new CoffeeController(new UserInput(), new ValveController(), new BrewController(), new WeightCheck(), new TemperatureInfo(), new AlertController(), new WaterInfo(), new Sound());
        Thread controllerThread = new Thread(coffeeController);
        controllerThread.start();
    }
}
