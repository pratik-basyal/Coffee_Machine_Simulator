public class ValveController {
    private boolean valveOpen; // Valve status

    public int setFlowRate(int brewStrength) {
        // Calculate flow rate based on cup size and brew strength
        // Flow rate in milliliters per second
        return (30 - (10 * (brewStrength - 1)));
    }

    /******
     * this method returns true when the condition to open the valve is true
     * @return
     */
    protected void openValve(int strength) {
        int delay;
        String valveStatus;

        switch(strength) {
            case 3 -> {
                delay = 5000; //longest for strong coffee
                valveStatus = "Opening valve for Strong coffee";
            }
            case 2 -> {
                delay = 4000;
                valveStatus = "Opening valve for Medium coffee";
            }
            case 1 -> {
                delay = 3000;
                valveStatus = "Opening valve for Light coffee";
            }
            default -> throw new IllegalArgumentException("Invalid Coffe Strength");
        }
        System.out.println(valveStatus);
        System.out.println("Coffee is pouring down to the pot.....");

        //now simulating the time it takes to brew the coffee
        try {
            Thread.sleep(delay);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt(); //handled interrupted exception
        }

//        System.out.println("Coffee is BREWED!!");
    }


    public void closeValve() {
        if (valveOpen) {
            valveOpen = false;
            System.out.println("Valve is now closed.");
        }
    }

}
