package Main_Logic;

import Socket_Main.SocketHandler;

public class BrewPressed {
    private boolean brewPressed = false;

    private AlertController display;
//    private SocketHandler socketHandler;

//    public BrewPressed() {
//        this.socketHandler = socketHandler;
//    }
    public void pressButton() {
        this.brewPressed = true;
    }

    public boolean isBrewPressed() {
        return brewPressed;
    }
}
