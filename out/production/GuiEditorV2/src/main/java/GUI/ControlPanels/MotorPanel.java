package GUI.ControlPanels;

import GUI.Ev3Classes.Component;
import GUI.CustomButtons.*;
import javafx.scene.input.KeyCode;
import org.json.simple.JSONObject;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import org.eclipse.paho.client.mqttv3.*;

import static java.lang.System.currentTimeMillis;

public class MotorPanel extends ControlPanel implements MqttCallback{

    private Button runForever = new Button("Run");
    private TextField motorSpeed = new TextField();
    private Button absRotate = new Button("Abs");
    private TextField absAngle = new TextField();
    //private Button slider = new Button();
    private MinimizeButton minimize = new MinimizeButton(runForever, buttons);
    private MinimizeButton minimizeRotate = new MinimizeButton(absRotate, buttons);

    public MotorPanel(Component newComponent){

        component = newComponent;
        dragBar();
        minimize();
        minimizeRotate();
        closePanel();
        stop();
        ev3Name();
        componentName();
        name();
        runForever();
        absRotate();
        absAngle();
        //slider();
        motorSpeed();

        try {
            client2 = new MqttClient("tcp://localhost:1883", component.getName()+component.getEv3());
            client2.connect();
            client2.setCallback(this);
            client2.subscribe("motor/action/stop", 2);
            client2.subscribe("motor/action/rel", 2);
            client2.subscribe("motor/action/rel1verse", 2);
            client2.subscribe("motor/action/forever", 2);
            client2.subscribe("motor/action/timed", 2);
            client2.subscribe("motor/action/abs", 2);
            client2.subscribe("time0_init", 2);
            client2.subscribe("motor/action/stop", 2);
            //  client2.subscribe("ev3_config", 2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // MotorPanel functions


    // panel parts methods
    private void minimize(){
        buttons.add(minimize);
    }

    private void minimizeRotate(){
        minimizeRotate.setTranslateX(minimize.getPrefWidth()+8);
        buttons.add(minimizeRotate);
    }


    private void runForever() {
        runForever.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight());
        runForever.setPrefWidth(width);
        runForever.setPrefHeight(60);
        runForever.setStyle("-fx-alignment: center-left; -fx-background-color: GREY; ");
        buttons.add(runForever);
        runForever.setOnMousePressed(press -> {
            instant = currentTimeMillis();
            if ((instant - previnstant <= 500)) {
                ////
                sendValue(motorSpeed);
                ////////////////
                previnstant = 0;
            } else previnstant = instant;
        });

        /*runForever.setOnMouseReleased(e -> {
            runForever.setStyle("-fx-background-color: GREY; ");
            runForever.setText("Run");
        });*/
    }

    private void absRotate(){
        absRotate.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight()+ runForever.getPrefHeight());
        absRotate.setPrefWidth(width);
        absRotate.setPrefHeight(60);
        absRotate.setStyle("-fx-alignment: center-left; -fx-background-color: BROWN; ");
        buttons.add(absRotate);
    }

    /*private void slider(){
        slider.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + 15);
        slider.setTranslateX(10);
        slider.setPrefWidth(10);
        slider.setMinWidth(10);
        slider.setMaxWidth(10);
        slider.setPrefWidth(15);
        slider.setMinHeight(15);
        slider.setMaxHeight(15);
        slider.setOnMousePressed(t -> {
            dragDelta.x = slider.getLayoutX() - t.getSceneX();
        });

        slider.setOnMouseDragged(t -> {
            double result = t.getSceneX() + dragDelta.x;
            if((result > (runForever.getLayoutX() + 5))||(result < (runForever.getLayoutX() + runForever.getPrefWidth() - 10)))
                slider.setTranslateX(result);
        });
        //buttons.add(slider);
    }*/

    private void motorSpeed(){
        motorSpeed.setId("forever");
        motorSpeed.setStyle("-fx-background-color: WHITE; -fx-border-color: GREY");
        motorSpeed.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + 19 );
        motorSpeed.setTranslateX(40);
        motorSpeed.setPrefWidth(runForever.getPrefWidth()/4);
        buttons.add(motorSpeed);
        motorSpeed.setOnKeyPressed(e ->{
            if(e.getCode()== KeyCode.ENTER){
                sendValue(motorSpeed);
            }
        });
    }

    private void absAngle(){
        absAngle.setId("abs");
        absAngle.setStyle("-fx-background-color: WHITE; -fx-border-color: GREY");
        absAngle.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + runForever.getPrefHeight() + 19 );
        absAngle.setTranslateX(40);
        absAngle.setPrefWidth(absRotate.getPrefWidth()/4);
        buttons.add(absAngle);
        absAngle.setOnKeyPressed(e ->{
            if(e.getCode()== KeyCode.ENTER){
                sendValue(absAngle);
            }
        });
    }

    int i=0;

    private void sendValue(TextField field){
        JSONObject obj2 = new JSONObject();

        obj2.put("ev3", component.getEv3().getName());
        obj2.put("motor", component.getName());
        try {
            obj2.put("value", Integer.parseInt(field.getCharacters().toString()));
        } catch(NumberFormatException e){
            obj2.put("value", 0);
        }

        System.out.print(obj2);
        MqttMessage message2 = new MqttMessage();
        message2.setPayload(obj2.toJSONString().getBytes());
        try {
            client2.publish("motor/action/" + field.getId(), message2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
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
