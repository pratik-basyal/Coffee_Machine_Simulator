package Main_Logic;

public class TemperatureInfo {
    private float brewingTemp = 112;

    private float reqPlateTemp = 90;
    private float plateTemp;

    protected float roomTemp = 70;

    /******
     * setter for plate temperature
     * @param plateTemp
     */
    public void setPlateTemperature(float plateTemp) {
        this.plateTemp = plateTemp;
    }

    /******
     * getter for brewingTemp
     * @return
     */
    public float getBrewingTemperature() {
        return brewingTemp;
    }

    /*******
     * getter for plateTemp
     * @return
     */
    public float getPlateTemp() {
        return plateTemp;
    }

    protected void brewingTempInfo() {
        float room = roomTemp;
        while (room != getBrewingTemperature()) {
            room += 2;
            System.out.println("Temperature of water : " + room);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    protected void heatingPlateTempInfo() {
        while (plateTemp != reqPlateTemp) {
            plateTemp += 2;
            System.out.println("Temperature of PLATE : " + plateTemp);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
