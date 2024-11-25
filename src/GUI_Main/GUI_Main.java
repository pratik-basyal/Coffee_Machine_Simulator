package GUI_Main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

public class GUI_Main extends Application {

    public static void main(String[] args) {
        launch(args); //static method that belongs to application class
    }

    /*******
     * This function is used to load resources, if it affects UI during the laoding process
     * We can still load every resources here and use it on start method for future reference
     */
    @Override
    public void init() {

    }
    /***
     * Behind the scene :
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Circle circle = new Circle(20, 50, 60);
        BorderPane pane = new BorderPane(circle);
        Scene scene = new Scene(pane, 400, 400);

        stage.setTitle("TEST");
        stage.setScene(scene);
        stage.show();
    }
}
