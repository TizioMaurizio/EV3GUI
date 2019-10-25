package GUI;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
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
    int ev3count = 2;
    GridPane configGrid = new GridPane();
    Button scan = new Button("Scan");
    Text username = new Text("Configuration Menu");
    List<Button> ev3ButtonList = new ArrayList<>();
    public ConfigPanel() throws MqttException {
        client = new MqttClient("tcp://192.168.1.6:1883", "configPanel");
        client.connect();
        client.setCallback(this);
        client.subscribe("hello", 2);

        configGrid.setPadding(new Insets(10, 10, 10, 10));
        configGrid.setMinSize(200, 9999);
        configGrid.setMaxSize(200,9999);
        configGrid.setVgap(5);
        configGrid.setHgap(5);


        configGrid.add(username, 0, 0);


        configGrid.add(scan, 0, 1);
        scan();
        configGrid.setStyle("-fx-background-color: #D8BFD8;");

    }

    public GridPane getPanel(){
        return configGrid;
    }

    private void scan(){
        scan.setOnMouseClicked(press -> {
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

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        boolean isPresent = false;
        JSONParser parser = new JSONParser();
        try{
            JSONObject jsonObject = (JSONObject) parser.parse(message.toString());
            String ev3name = jsonObject.get("name").toString();
            System.out.println("EV3 Found: " + ev3name);
            for (Button bt: ev3ButtonList) {
                if (bt.getText() == ev3name)
                    isPresent = true;
            }
            if(!isPresent){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Button newEv3 = new Button(ev3name);
                        ev3ButtonList.add(newEv3);
                        configGrid.add(newEv3, 0, ev3count);
                    }
                });
                ev3count++;
            }
        }catch(Exception pe) {
            pe.printStackTrace();
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}
