/**********
 * In this class we work for brewing process.
 */
public class BrewController {
    private final TemperatureInfo temperature = new TemperatureInfo();

    protected void startHeater(){
        float roomTemp = temperature.roomTemp;
        while (roomTemp != temperature.getBrewingTemperature()) {
            roomTemp += 2;
            System.out.println("Temperature of water : " + roomTemp);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
