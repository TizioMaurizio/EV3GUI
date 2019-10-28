package GUI;

import GUI.CustomButtons.*;
import javafx.application.Platform;
import javafx.scene.control.Control;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import org.json.simple.JSONObject;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.parser.JSONParser;

import static java.lang.System.currentTimeMillis;

public class SensorPanel implements MqttCallback{

    String ev3;
    String sensor;
    String type = "COLOR";
    String value;

    MqttClient client2;
    Button runForever = new Button();
    Button slider = new Button();
    Button color = new Button();
    List<Region> buttons = new ArrayList<>();
    Dragbar dragBar = new Dragbar(buttons);
    Ev3Name ev3Name = new Ev3Name();
    ComponentName componentName = new ComponentName();
    boolean holdCtrl = true;

    StackPane panel = new StackPane();

    public SensorPanel(String ev3, String motor, String type){

        double width = 120;
        double height = 200;


        dragBar.setPrefWidth(width);
        dragBar.setPrefHeight(10);
        dragBar.setMinHeight(10);
        dragBar.setMaxHeight(10);

        ev3Name.setTranslateY(dragBar.getPrefHeight());
        ev3Name.setPrefWidth(40);
        ev3Name.setPrefHeight(40);
        ev3Name.setStyle("-fx-background-color: BLUE; ");

        componentName.setTranslateY(dragBar.getPrefHeight());
        componentName.setTranslateX(ev3Name.getPrefWidth());
        componentName.setPrefWidth(width - ev3Name.getPrefWidth());
        componentName.setPrefHeight(ev3Name.getPrefHeight());
        componentName.setText("Color\nSensor");

        runForever.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight());
        runForever.setPrefWidth(width);
        runForever.setPrefHeight(40);
        //runForever();

        slider.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + 15);
        slider.setTranslateX(10);
        slider.setPrefWidth(10);
        slider.setMinWidth(10);
        slider.setMaxWidth(10);
        slider.setPrefWidth(15);
        slider.setMinHeight(15);
        slider.setMaxHeight(15);
        //slider();

        color.setPrefWidth(runForever.getPrefWidth() - 20);
        color.setPrefHeight(runForever.getPrefHeight() - 10);
        color.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + runForever.getPrefHeight()/2 - color.getPrefHeight()/2 );
        color.setTranslateX(runForever.getPrefWidth()/2 - color.getPrefWidth()/2 );



        //  panel components
        buttons.add(dragBar);
        buttons.add(ev3Name);
        buttons.add(componentName);
        buttons.add(runForever);
        //buttons.add(slider);
        buttons.add(color);
        panel.getChildren().addAll(buttons);

        try {
            client2 = new MqttClient("tcp://localhost:1883", ev3);
            client2.connect();
            client2.setCallback(this);
            client2.subscribe("sensor/on_demand/answer", 2);
            client2.subscribe("time0_init", 2);
            ev3Name.setClient(client2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // MotorPanel functions

    public List<Region> getButtons(){
        return buttons;
    }

    public StackPane getPanel(){
        return panel;
    }

    public void setLayout(double x, double y) {
        for (Node bt : buttons) {
            bt.setLayoutX(bt.getLayoutX() + x);
            bt.setLayoutY(bt.getLayoutY() + y);
        }
    }


    // panel parts methods
    long instant;
    long previnstant=0;

    /*private void runForever() {
        runForever.setOnMousePressed(press -> {
            instant = currentTimeMillis();
            if ((instant - previnstant <= 500)) {
                ////
                JSONObject obj2 = new JSONObject();

                obj2.put("ev3", "ev3B");
                obj2.put("motor", "outA");
                try {
                    obj2.put("value", Integer.parseInt(color.getCharacters().toString()));
                } catch(NumberFormatException e){
                    obj2.put("value", 0);
                }

                System.out.print(obj2);
                MqttMessage message2 = new MqttMessage();
                message2.setPayload(obj2.toJSONString().getBytes());
                try {
                    client2.publish("motor/action/forever", message2);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                ////////////////
                previnstant = 0;
            } else previnstant = instant;
        });
    }*/
    int i=0;
    /*private void slider() {
        slider.setOnMousePressed(t -> {
            dragDelta.x = slider.getLayoutX() - t.getSceneX();
        });

        slider.setOnMouseDragged(t -> {
            double result = t.getSceneX() + dragDelta.x;
            if((result > (runForever.getLayoutX() + 5))||(result < (runForever.getLayoutX() + runForever.getPrefWidth() - 10)))
                slider.setTranslateX(result);
        });
    }*/

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(message.toString());
        String received = jsonObject.get("value").toString();
        if (topic.equals("sensor/on_change")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    color.setStyle("-fx-background-color: " + received + "; ");
                }
            });
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}
