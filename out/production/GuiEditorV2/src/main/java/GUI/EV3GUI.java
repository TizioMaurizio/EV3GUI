package GUI;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;

import static java.lang.System.currentTimeMillis;

public class EV3GUI extends Stage{

    private AnchorPane arena = new AnchorPane();
    public Scene scene1;

    // button and pane are created
    public EV3GUI() throws MqttException{

        PahoDemo demo = new PahoDemo();
        demo.doDemo();
        // button added to pane and pane added to scene
        arena.getChildren().addAll((new MotorPanel()).getButtons());
        arena.getChildren().addAll((new MotorPanel()).getButtons());
        arena.getChildren().addAll((new MotorPanel()).getButtons());
        arena.getChildren().addAll((new MotorPanel()).getButtons());

        scene1 = new Scene(arena);
        this.setScene(scene1);
        this.setWidth(1920/2);
        this.setHeight(1080/2);
        this.show();
    }
}
