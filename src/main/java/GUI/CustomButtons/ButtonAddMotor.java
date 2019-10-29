package GUI.CustomButtons;


import GUI.Ev3Classes.Component;
import GUI.ControlPanels.ControlPanel;
import GUI.ControlPanels.MotorPanel;
import GUI.EV3GUI;
import GUI.Ev3Classes.Ev3;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.*;

import static GUI.EV3GUI.*;

public class ButtonAddMotor extends MqttButton {

    public ButtonAddMotor(Ev3 ev3){
        ButtonAddMotor buttonAddMotor = this;
        this.setText("+Motor");
        this.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                GridPane secondaryLayout = new GridPane();

                Scene secondScene = new Scene(secondaryLayout, 230, 180);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("New Motor");
                newWindow.setScene(secondScene);

                // Specifies the modality for new window.
                newWindow.initModality(Modality.WINDOW_MODAL);

                Stage primaryStage = (Stage) buttonAddMotor.getScene().getWindow();
                // Specifies the owner Window (parent) for new window
                newWindow.initOwner(primaryStage);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();

                Label motor = new Label("Configure\nnew motor");
                motor.setStyle("-fx-text-fill: GREEN");
                Label portText = new Label("EV3 Port: \n(outA,B,C,D)");
                Label motorText = new Label("Motor type: \n(Large/Medium)");
                TextField port = new TextField();
                port.setPrefWidth(100);
                TextField type = new TextField();
                type.setPrefWidth(100);
                Button confirm = new Button("Confirm");

                secondaryLayout.add(motor,0,0);
                secondaryLayout.add(portText,0,2);
                secondaryLayout.add(motorText,0,3);
                secondaryLayout.add(port,1,2);
                secondaryLayout.add(type,1,3);
                secondaryLayout.add(confirm,0,4);

                confirm.setOnMousePressed(e -> {
                    try {
                        Component newComponent = new Component(ev3, port.getCharacters().toString(), type.getCharacters().toString());
                        //JSONObject obj2 = new JSONObject();
                        //obj2.put("ev3", ev3.getName());
                        int k = 0;
                        switch(port.getCharacters().toString()){
                            case("outA"):
                                k=1;
                                break;
                            case("outB"):
                                k=3;
                                break;
                            case("outC"):
                                k=5;
                                break;
                            case("outD"):
                                k=7;
                                break;
                            default:
                                throw new Exception();
                        }
                        //obj2.put(Integer.toString(k), port.getCharacters().toString());
                        //int k = Integer.parseInt(port.getCharacters().toString());
                        //obj2.put(Integer.toString(k+1), type.getCharacters().toString());

                        for (Ev3 ev: Ev3List) {
                            if(ev.getName().equals(ev3.getName())){
                                ev.jsonPut("ev3", ev3.getName());
                                ev.jsonPut(Integer.toString(k), port.getCharacters().toString());
                                ev.jsonPut(Integer.toString(k+1), type.getCharacters().toString());
                            }
                        }
                        int i=0;
                        for(i=1;i<13;i++){  // fill the json with None values for the EV3 python function
                            if(ev3.jsonGet(Integer.toString(i))!=null){
                                //obj2.put(Integer.toString(i), obj2.get(Integer.toString(i)));
                            }
                            else
                                ev3.jsonPut(Integer.toString(i), null);
                                //obj2.put(Integer.toString(i), null);
                        }

                        System.out.print(ev3.getJsonObject());
                        MqttMessage message2 = new MqttMessage();
                        message2.setPayload(ev3.getJsonObject().toJSONString().getBytes());
                        GuiClient.publish("ev3_config", message2);
                        ControlPanel newPanel = new MotorPanel(newComponent);
                        arena.getChildren().addAll((newPanel.getButtons()));
                        EV3GUI.panels.add(newPanel);
                        newPanel.getEv3Name().setStyle("-fx-background-color: " + ev3.getColor() + "; -fx-border-color: GREY");
                        newPanel.setLayout(400 + EV3GUI.panels.indexOf(newPanel) * 10,200 + EV3GUI.panels.indexOf(newPanel) * 10);
                        newWindow.close();
                    } catch(Exception q){
                        //q.printStackTrace();
                        System.out.println("ERROR: Invalid configuration by catching GENERIC exception (printStackTrace to debug)");
                        Label error = new Label("Invalid configuration\nRetry or exit");
                        secondaryLayout.add(error,1,0);
                        error.setStyle("-fx-text-fill: RED");
                    }
                });
            }
        });
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
