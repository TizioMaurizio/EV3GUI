package view;

import GUI.EV3GUI;
import javafx.application.*;
import javafx.stage.*;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.concurrent.Future;


public class Main extends Application{
    //TODO AdrenalinaButton extends Button, attributo CHOICE

    //TODO dividere le classi in GUIModel (output) e GUIController (input)
    public EV3GUI ev3;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, MalformedURLException, MqttException {
        //RTGuiKeyboard = new GuiKeyboard();
        ev3 = new EV3GUI(); //initializes launcher stage, from which you can browse various menus or start the game stage

        //new GuiEditor();

    }

}
