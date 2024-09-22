public class BrewPressed {
    private boolean brewPressed;

    public void pressButton() {
        this.brewPressed = true;
        System.out.println("Brew start button pressed.");
    }

    public boolean isBrewPressed() {
        return brewPressed;
    }
}
