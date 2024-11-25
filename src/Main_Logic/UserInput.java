package Main_Logic;

import Socket_Main.SocketHandler;

import java.net.Socket;
import java.util.Scanner;
public class UserInput {
    private AlertController alertController;
    private int cupSizeDial;
    private int brewStrengthDial;

    private  boolean brewPressed;
    private boolean isSizeTaken;
    private boolean isStrengthTaken;

    public UserInput(int cupsSize, int brewStrengthDial, boolean brewPressed) {
        this.cupSizeDial = cupsSize;
        this.brewStrengthDial = brewStrengthDial;
        this.brewPressed = brewPressed;
    }

    public int getCupSize() {
        return cupSizeDial;
    }

    public int getBrewStrength() {
        return brewStrengthDial;
    }
    public boolean coffeeReady(){
//        System.out.println("Do you want to grab your coffee: Y/N ");
//        String userDecision = scanner.next();
//        if(userDecision.equals("Y")){
//            return true;
//        }
//        else if(userDecision.equals("N")){
//
//            return false;
//        }
//        else{
//            System.out.println("Invalid Input");
//            coffeeReady();
//        }
        return false;
    }

    public boolean isBrewPressed() {
        //brewPressed.pressButton();
        return brewPressed;
    }

    public String getMessage() {
        if (isBrewPressed()) {
            return "brewPressed";
        }
        return null;
    }
}