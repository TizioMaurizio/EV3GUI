package GUI.ControlPanels;

import GUI.Ev3Classes.Component;
import GUI.CustomButtons.ButtonCompName;
import GUI.CustomButtons.ButtonEv3Name;
import GUI.CustomButtons.Dragbar;
import GUI.EV3GUI;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

public class ControlPanel {
    public Component getComponent() {
        return component;
    }

    public Button getEv3Name() {
        return ev3Name;
    }

    public List<Region> getButtons(){
        return buttons;
    }

    protected void componentName(){
        buttonCompName.setTranslateY(dragBar.getPrefHeight());
        buttonCompName.setTranslateX(ev3Name.getPrefWidth());
        buttonCompName.setPrefWidth(width - ev3Name.getPrefWidth());
        buttonCompName.setPrefHeight(ev3Name.getPrefHeight());
        buttonCompName.setText("\n" + component.getType());
        buttonCompName.setStyle("-fx-background-color: WHITE; -fx-border-color: GREY");
        buttons.add(buttonCompName);
    }

    protected void closePanel(){
        closePanel.setPrefWidth(10);
        closePanel.setPrefHeight(10);
        closePanel.setMinHeight(10);
        closePanel.setMaxHeight(10);
        closePanel.setTranslateX(dragBar.getPrefWidth() - 19);
        closePanel.setStyle("-fx-background-color: BLACK; -fx-border-color: GREY");
        closePanel.setOnMousePressed(press ->{
            instant = currentTimeMillis();
            if ((instant - previnstant <= 500)) {
                EV3GUI.panels.remove(this);
                EV3GUI.arena.getChildren().removeAll(buttons);
                previnstant = 0;
            } else previnstant = instant;
        });
        buttons.add(closePanel);
    }

    protected void stop(){
        stop.setPrefWidth(10);
        stop.setPrefHeight(10);
        stop.setMinHeight(10);
        stop.setMaxHeight(10);
        stop.setTranslateX(dragBar.getPrefWidth() - 34);
        stop.setStyle("-fx-background-color: RED; -fx-border-color: GREY");
        stop.setOnMousePressed(e ->{
            JSONObject obj2 = new JSONObject();
            obj2.put("ev3", component.getEv3());
            obj2.put("motor", "out" + component.getPort());
            obj2.put("value", "COMMAND_STOP");
            MqttMessage message2 = new MqttMessage();
            message2.setPayload(obj2.toJSONString().getBytes());
            try {
                EV3GUI.GuiClient.publish("motor/action/stop", message2);
            } catch (MqttException q) {
                q.printStackTrace();
            }
        });
        buttons.add(stop);
    }

    protected void dragBar(){
        dragBar.setPrefWidth(this.getWidth());
        dragBar.setPrefHeight(10);
        dragBar.setMinHeight(10);
        dragBar.setMaxHeight(10);
        dragBar.setStyle("-fx-background-color: WHITE; -fx-border-color: GREY");
        buttons.add(dragBar);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setLayout(double x, double y) {
        for (Region bt : buttons) {
            bt.setLayoutX(bt.getLayoutX() + x);
            bt.setLayoutY(bt.getLayoutY() + y);
        }
    }

    protected void name(){
        name.setStyle("-fx-background-color: WHITE; -fx-border-color: GREY");
        name.setText("insert name");
        name.setOnMousePressed(e ->{
            if(name.getText().equals("insert name"))
                name.setText("");
        });
        name.setTranslateY(dragBar.getPrefHeight());
        name.setTranslateX(ev3Name.getPrefWidth());
        name.setPrefWidth(width - ev3Name.getPrefWidth());
        name.setPrefHeight(buttonCompName.getPrefHeight()/2);
        buttons.add(name);
    }

    protected void ev3Name(){
        ev3Name.setTranslateY(dragBar.getPrefHeight());
        ev3Name.setPrefWidth(60);
        ev3Name.setPrefHeight(60);
        ev3Name.setText(component.getEv3().getName() + "\n" + component.getName());
        buttons.add(ev3Name);
    }

    protected MqttClient client2;
    protected TextField name = new TextField();
    protected long instant;
    protected long previnstant=0;
    protected double width = 180;
    protected double height = 200;
    protected Component component;
    protected ButtonCompName buttonCompName = new ButtonCompName();
    protected Button ev3Name = new ButtonEv3Name();
    protected Button closePanel = new Button();
    protected Button stop = new Button();
    protected List<Region> buttons = new ArrayList<>();
    protected Dragbar dragBar = new Dragbar(buttons);
}
