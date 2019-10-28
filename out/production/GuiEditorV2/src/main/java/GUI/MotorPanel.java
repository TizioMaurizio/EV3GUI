package GUI;

import GUI.CustomButtons.*;
import javafx.scene.layout.Region;
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
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.*;

import static java.lang.System.currentTimeMillis;

public class MotorPanel implements MqttCallback{

    class Delta {
        double x, y;
    }

    String ev3 = "B";
    String motor = "outA";
    String type = "Large";
    String speed;

    long instant;
    long previnstant=0;

    MqttClient client2;
    Button runForever = new Button("Run");
    Button absRotate = new Button("Rotate");
    Button slider = new Button();
    TextField motorSpeed = new TextField();
    List<Region> buttons = new ArrayList<>();
    Dragbar dragBar = new Dragbar(buttons);
    Ev3Name ev3Name = new Ev3Name();
    ComponentName componentName = new ComponentName();
    MinimizeButton minimize = new MinimizeButton(runForever, buttons);
    MinimizeButton minimizeRotate = new MinimizeButton(absRotate, buttons);

    boolean holdCtrl = true;
    final Delta dragDelta = new Delta();

    public MotorPanel(String ev3, String motor, String type){

        double width = 120;
        double height = 200;

        dragBar.setPrefWidth(width);
        dragBar.setPrefHeight(10);
        dragBar.setMinHeight(10);
        dragBar.setMaxHeight(10);

        minimizeRotate.setTranslateX(minimize.getPrefWidth()+8);

        ev3Name.setTranslateY(dragBar.getPrefHeight());
        ev3Name.setPrefWidth(40);
        ev3Name.setPrefHeight(40);
        ev3Name.setStyle("-fx-background-color: RED; ");
        ev3Name.setText(ev3);

        componentName.setTranslateY(dragBar.getPrefHeight());
        componentName.setTranslateX(ev3Name.getPrefWidth());
        componentName.setPrefWidth(width - ev3Name.getPrefWidth());
        componentName.setPrefHeight(ev3Name.getPrefHeight());
        componentName.setText(type + "Motor\n" + motor);

        runForever.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight());
        runForever.setPrefWidth(width);
        runForever.setPrefHeight(40);
        runForever.setStyle("-fx-alignment: center-left; -fx-background-color: GREY; ");
        runForever();

        absRotate.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight()+ runForever.getPrefHeight());
        absRotate.setPrefWidth(width);
        absRotate.setPrefHeight(40);

        slider.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + 15);
        slider.setTranslateX(10);
        slider.setPrefWidth(10);
        slider.setMinWidth(10);
        slider.setMaxWidth(10);
        slider.setPrefWidth(15);
        slider.setMinHeight(15);
        slider.setMaxHeight(15);
        //slider();

        motorSpeed.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + 10 );
        motorSpeed.setTranslateX(40);
        motorSpeed.setPrefWidth(runForever.getPrefWidth() - 50);


        //  panel components
        buttons.add(dragBar);
        buttons.add(ev3Name);
        buttons.add(componentName);
        buttons.add(runForever);
        //buttons.add(slider);
        buttons.add(motorSpeed);
        buttons.add(absRotate);
        buttons.add(minimize);
        buttons.add(minimizeRotate);

        try {
            UUID uuid = UUID.randomUUID();
            client2 = new MqttClient("tcp://localhost:1883", ev3);
            client2.connect();
            client2.setCallback(this);
            client2.subscribe("motor/action/stop", 2);
            client2.subscribe("motor/action/rel", 2);
            client2.subscribe("motor/action/rel1verse", 2);
            client2.subscribe("motor/action/forever", 2);
            client2.subscribe("motor/action/timed", 2);
            client2.subscribe("motor/action/abs", 2);
            client2.subscribe("time0_init", 2);
            ev3Name.setClient(client2);
            //  client2.subscribe("ev3_config", 2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // MotorPanel functions

    public List<Region> getButtons(){
        return buttons;
    }

    public void setLayout(double x, double y) {
        for (Region bt : buttons) {
            bt.setLayoutX(bt.getLayoutX() + x);
            bt.setLayoutY(bt.getLayoutY() + y);
        }
    }


    // panel parts methods

    private void runForever() {
        runForever.setOnMousePressed(press -> {
            instant = currentTimeMillis();
            if ((instant - previnstant <= 500)) {
                ////
                JSONObject obj2 = new JSONObject();

                obj2.put("ev3", "ev3B");
                obj2.put("motor", "outA");
                try {
                    obj2.put("value", Integer.parseInt(motorSpeed.getCharacters().toString()));
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

        /*runForever.setOnMouseReleased(e -> {
            runForever.setStyle("-fx-background-color: GREY; ");
            runForever.setText("Run");
        });*/
    }
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

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}
