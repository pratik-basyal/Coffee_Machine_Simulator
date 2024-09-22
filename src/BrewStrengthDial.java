public class BrewStrengthDial {
    private int selectedStrength;

    public void rotateDial(int position) {
        this.selectedStrength = position;
        System.out.println("Brew strength set to: " + position);
    }

    public int getSelectedStrength() {
        return selectedStrength;
    }
}
