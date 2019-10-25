package GUI;

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

    long instant;
    long previnstant=0;
    Button dragBar = new Button();
    Button ev3Name = new Button();
    Button componentName = new Button();
    Button runForever = new Button();
    Button slider = new Button();
    TextField motorSpeed = new TextField();
    List<Node> buttons = new ArrayList<>();
    boolean holdCtrl = true;
    final Delta dragDelta = new Delta();
    MqttClient client2;

    public MotorPanel(){


        double width = 100;
        double height = 200;

        ev3Name.setPrefWidth(30);
        ev3Name.setPrefHeight(40);
        ev3Name();

        componentName.setTranslateX(ev3Name.getPrefWidth());
        componentName.setPrefWidth(width - ev3Name.getPrefWidth());
        componentName.setPrefHeight(ev3Name.getPrefHeight());

        dragBar.setTranslateY(ev3Name.getPrefHeight());
        dragBar.setPrefWidth(width);
        dragBar.setPrefHeight(10);
        dragBar.setMinHeight(10);
        dragBar.setMaxHeight(10);
        dragBar();

        runForever.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight());
        runForever.setPrefWidth(width);
        runForever.setPrefHeight(40);
        runForever();

        slider.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + 15);
        slider.setTranslateX(10);
        slider.setPrefWidth(10);
        slider.setMinWidth(10);
        slider.setMaxWidth(10);
        slider.setPrefWidth(15);
        slider.setMinHeight(15);
        slider.setMaxHeight(15);
        //slider();

        motorSpeed.setTranslateY(ev3Name.getPrefHeight() + dragBar.getPrefHeight() + 13 );
        motorSpeed.setTranslateX(40);
        motorSpeed.setPrefWidth(runForever.getPrefWidth() - 50);


        //  panel components
        buttons.add(dragBar);
        buttons.add(ev3Name);
        buttons.add(componentName);
        buttons.add(runForever);
        //buttons.add(slider);
        buttons.add(motorSpeed);

        try {
            UUID uuid = UUID.randomUUID();
            client2 = new MqttClient("tcp://192.168.1.6:1883", uuid.toString());
            client2.connect();
            client2.setCallback(this);
            client2.subscribe("sensor/on_demand/question", 2);
            client2.subscribe("motor/action/stop", 2);
            client2.subscribe("motor/action/rel", 2);
            client2.subscribe("motor/action/rel1verse", 2);
            client2.subscribe("motor/action/forever", 2);
            client2.subscribe("motor/action/timed", 2);
            client2.subscribe("motor/action/abs", 2);
            client2.subscribe("time0_init", 2);
            client2.subscribe("ev3_config", 2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public List<Node> getButtons(){
        return buttons;
    }

    private void dragBar() {
        // dragBar functions
        dragBar.setOnMousePressed(t -> {
            if (holdCtrl) {
                dragDelta.x = dragBar.getLayoutX() - t.getSceneX();
                dragDelta.y = dragBar.getLayoutY() - t.getSceneY();
            }
        });
        dragBar.setOnMouseDragged(t -> {
            if (holdCtrl) {
                for (Node bt : buttons) {
                    bt.setLayoutX(t.getSceneX() + dragDelta.x);
                    bt.setLayoutY(t.getSceneY() + dragDelta.y);
                }
                dragBar.setCursor(Cursor.MOVE);
            }
        });
    }

    private void ev3Name() {
        ev3Name.setStyle("-fx-background-image: url('src/main/java/GUI/AmmoCubes/YellowAmmo.png')");
        ev3Name.setOnMousePressed(press -> {
            instant = currentTimeMillis();
            if ((instant - previnstant <= 500)) {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("filename2.txt"), "utf-8"))) {
                    writer.write("somethings");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //////////////////////
                JSONObject obj = new JSONObject();

                obj.put("ev3", "ev3B");
                obj.put("1", "outA");
                obj.put("2", "Large");
                System.out.print(obj);
                MqttMessage message = new MqttMessage();
                message.setPayload(obj.toJSONString().getBytes());
                try {
                    client2.publish("ev3_config", message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                ////
                previnstant = 0;
            } else previnstant = instant;
        });
    }


    private void runForever() {
        runForever.setOnMousePressed(press -> {
            instant = currentTimeMillis();
            if ((instant - previnstant <= 500)) {
                ////
                JSONObject obj2 = new JSONObject();

                obj2.put("ev3", "ev3B");
                obj2.put("motor", "outA");
                obj2.put("value", Integer.parseInt(motorSpeed.getCharacters().toString()));

                System.out.print(obj2);
                MqttMessage message2 = new MqttMessage();
                message2.setPayload(obj2.toJSONString().getBytes());
                try {
                    client2.publish("motor/action/forever", message2);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                ////////////////
                i++;
                componentName.setText(Integer.toString(i));
                previnstant = 0;
            } else previnstant = instant;
        });
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
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println(String.format("Ricevuto da Gui %s", new String(message.getPayload())));
        update();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }

    public void update(){
        System.out.println("ciao");
    }
}
