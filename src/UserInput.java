import java.util.Scanner;
public class UserInput {
    private final Scanner scanner;
    private final CupSizeDial cupSizeDial;
    private final BrewStrengthDial brewStrengthDial;
    private final BrewPressed brewPressed;
    private boolean isSizeTaken;
    private boolean isStrengthTaken;

    public UserInput() {
        scanner = new Scanner(System.in);
        cupSizeDial = new CupSizeDial();
        brewStrengthDial = new BrewStrengthDial();
        brewPressed = new BrewPressed();
    }

    public int getCupSize() {
        System.out.print("Enter the position for cup size (1-3): ");
        int size = scanner.nextInt();
        isSizeTaken = true;
        cupSizeDial.rotateDial(size);
        return cupSizeDial.getSelectedSize();
    }

    public int getBrewStrength() {
        System.out.print("Enter the position for brew strength (1-2-3): ");
        int strength = scanner.nextInt();
        isStrengthTaken = true;
        brewStrengthDial.rotateDial(strength);
        return brewStrengthDial.getSelectedStrength();
    }

    public boolean isBrewPressed() {
        System.out.print("Press '1' to start brewing, any other number to cancel: ");
        int input = scanner.nextInt();
        if (input == 1) {
            brewPressed.pressButton();
            if(isStrengthTaken && isSizeTaken)
                return brewPressed.isBrewPressed();
            return false;
        }
        else
            System.out.println("Brew Cancelled");
        return false;
    }
    public boolean coffeeReady(){
        System.out.println("Do you want to grab your coffee: Y/N ");
        String userDecision = scanner.next();
        if(userDecision.equals("Y")){
            return true;
        }
        else if(userDecision.equals("N")){

            return false;
        }
        else{
            System.out.println("Invalid Input");
            coffeeReady();
        }
        return false;
    }
}