package GUI;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.*;


import static java.lang.System.currentTimeMillis;

public class EV3GUI extends Stage{

    private AnchorPane arena = new AnchorPane();
    public Scene scene1;
    MotorPanel motorPanel = new MotorPanel("ev3A","outA","Large");
    SensorPanel sensorPanel = new SensorPanel("ev3B", "outA", "Medium");
    ConfigPanel configPanel = new ConfigPanel();

    // button and pane are created
    public EV3GUI() throws MqttException{

        PahoDemo demo = new PahoDemo();
        demo.doDemo();
        arena.getChildren().add(configPanel.getPanel());
        // button added to pane and pane added to scene
        arena.getChildren().addAll(motorPanel.getButtons());
        arena.getChildren().addAll(sensorPanel.getButtons());
        sensorPanel.setLayout(400,200);
        motorPanel.setLayout(200,200);
        scene1 = new Scene(arena);
        this.setScene(scene1);
        this.setWidth(1920/2);
        this.setHeight(1080/2);
        this.show();
    }
}
