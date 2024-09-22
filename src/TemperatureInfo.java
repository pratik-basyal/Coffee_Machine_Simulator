public class TemperatureInfo {
    private float brewingTemp = 112;
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
}
