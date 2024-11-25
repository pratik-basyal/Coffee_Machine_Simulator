package Main_Logic;

/**********
 * In this class we work for brewing process.
 */
public class BrewController {
    private int defaultWaterLevel;
    private float plateTemp;
    private final ValveController valveController;
    private final WeightCheck weightCheck;
    private final TemperatureInfo temperatureInfo;
    private final AlertController alertController;
    private final WaterInfo waterInfo;

    private final UserInput userInput;

    public BrewController(ValveController valveController, WeightCheck weightCheck,
                          TemperatureInfo temperatureInfo, AlertController alertController,
                          WaterInfo waterInfo, UserInput userInput, int waterLevel) {
        this.valveController = valveController;
        this.weightCheck = weightCheck;
        this.temperatureInfo = temperatureInfo;
        this.alertController = alertController;
        this.waterInfo = waterInfo;
        this.userInput = userInput;
        this.defaultWaterLevel = waterLevel;
    }


    /********
     * Starts heater to boil water
     */
    protected void startHeater(){
        temperatureInfo.brewingTempInfo();
    }

    /******
     * Starts heatingPlate
     */
    protected void startHeatingPlate() {
        temperatureInfo.heatingPlateTempInfo();
    }

    /********
     * This method is to start brewing process
     * @param cupSize
     * @param brewStrength
     */
    public void startBrewing(int cupSize, int brewStrength) {
        if(userInput.isBrewPressed()) {
            System.out.println("Cup Size : " + cupSize + "Brew Strength : " + brewStrength);
            if(cupSize==-1|| brewStrength ==-1){
                alert("coffeCupSizeNotSelected");
            }
            else if (waterInfo.checkWaterLevelInfo(defaultWaterLevel, cupSize)) {
                //System.out.println("Water Level is Okay!");
                alertController.displayMessage("WaterLevelOK");
                startHeater();

                int flowRate = valveController.setFlowRate(brewStrength);
                valveController.openValve(brewStrength);

                alert("pouringCoffee");
                float targetWeight = weightCheck.getTotalWeight(cupSize);

                // Start the weight thread in WeightCheck
                weightCheck.startBrewing(flowRate, targetWeight);
                startHeatingPlate();

                temperatureInfo.setPlateTemperature(temperatureInfo.roomTemp);
                plateTemp = temperatureInfo.getPlateTemp();

                System.out.println("Water Flow Rate = " + flowRate + "ml/s.");
                float initialWeight = weightCheck.emptyPotWeight;

                // Monitor current weight while brewing
                do {
                    System.out.println("Current weight : " + weightCheck.getCurrentWeight() + "g");
                    try {
                        Thread.sleep(1000);  // Update weight every second
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                } while (weightCheck.getCurrentWeight() < targetWeight);

                weightCheck.stopBrewing();

                valveController.closeValve();

                System.out.println("Current weight : " + initialWeight);
                alertController.displayMessage("COFFEE IS READY........");


            } else {
                alertController.displayMessage("WaterLevelLow");
            }
        }
    }

    public void alert(String message) {
        alertController.displayMessage(message);
    }
}
