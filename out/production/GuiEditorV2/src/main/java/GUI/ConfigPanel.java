package GUI;

import GUI.CustomButtons.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.lang.System.currentTimeMillis;

public class ConfigPanel implements MqttCallback{
    MqttClient client;
    JSONParser parser = new JSONParser();
    int ev3count = 0;
    GridPane configGrid = new GridPane();
    Button refresh = new Button("⟲");
    Text title = new Text("Menu");
    Line line = new Line();
    List<Button> ev3ButtonList = new ArrayList<>();
    public ConfigPanel() throws MqttException {
        client = new MqttClient("tcp://localhost:1883", "configPanel");
        client.connect();
        client.setCallback(this);
        client.subscribe("hello", 2);

        configGrid.setPadding(new Insets(10, 10, 10, 10));
        configGrid.setMinSize(200, 9999);
        configGrid.setMaxSize(200,9999);
        configGrid.setVgap(5);
        configGrid.setHgap(5);


        configGrid.add(title, 0, 0);

        configGrid.add(refresh, 0, 1);
        scan();

        configGrid.add(line,0,0);

        configGrid.setStyle("-fx-background-color: YELLOW;");

    }

    public GridPane getPanel(){
        return configGrid;
    }

    private void scan(){
        refresh.setOnMouseClicked(press -> {
            ev3count = 2;
            configGrid.getChildren().removeAll(ev3ButtonList);
            ev3ButtonList.clear();
            MqttMessage message2 = new MqttMessage();
            message2.setPayload("ping".getBytes());
            try {
                client.publish("scan", message2);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    private boolean isPresent = false;
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        isPresent = false;
        JSONParser parser = new JSONParser();
        try{
            JSONObject jsonObject = (JSONObject) parser.parse(message.toString());
            String ev3name = jsonObject.get("name").toString();
            System.out.println("EV3 configGridnd: " + ev3name);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for(Button bt: ev3ButtonList){
                        if(bt.getText().equals(ev3name)){
                            isPresent = true;
                        }
                    }
                    if(!isPresent) {
                        Button newEv3 = new Button(ev3name);
                        ev3ButtonList.add(newEv3);
                        Button addSensor = new AddSensor(ev3name);
                        Button addMotor = new AddMotor(ev3name);
                        ev3ButtonList.add(addSensor);
                        ev3ButtonList.add(addMotor);
                        configGrid.add(newEv3, 0, 4 + ev3count);
                        configGrid.add(addMotor, 1, 4 + ev3count);
                        configGrid.add(addSensor, 2, 4 + ev3count);
                    }
                }
            });
            ev3count++;
        }catch(Exception pe) {
            pe.printStackTrace();
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}
