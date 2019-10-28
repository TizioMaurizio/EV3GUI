package GUI.CustomButtons;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddSensor extends Button {
    GridPane grid = new GridPane();
    Scene addSensor = new Scene(grid, 230, 180);
    Button confirm = new Button("Confirm");

    public AddSensor(String ev3){
        AddSensor addSensor = this;
        this.setText("+Sensor");
        this.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                GridPane secondaryLayout = new GridPane();

                Scene secondScene = new Scene(secondaryLayout, 230, 180);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("New Sensor");
                newWindow.setScene(secondScene);

                // Specifies the modality for new window.
                newWindow.initModality(Modality.WINDOW_MODAL);

                Stage primaryStage = (Stage) addSensor.getScene().getWindow();
                // Specifies the owner Window (parent) for new window
                newWindow.initOwner(primaryStage);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();

                Label sensor = new Label("Configure\nnew sensor");
                sensor.setStyle("-fx-text-fill: BLUE");
                Label portText = new Label("EV3 Port: \n(inA,B,C,D)");
                Label sensorText = new Label("Sensor type: \n(COL-COLOR\n /TOUCH)");
                TextField port = new TextField();
                port.setPrefWidth(100);
                TextField type = new TextField();
                type.setPrefWidth(100);
                Button confirm = new Button("Confirm");

                secondaryLayout.add(sensor,0,0);
                secondaryLayout.add(portText,0,2);
                secondaryLayout.add(sensorText,0,3);
                secondaryLayout.add(port,1,2);
                secondaryLayout.add(type,1,3);
                secondaryLayout.add(confirm,0,4);
            }
        });

        confirm.setOnMousePressed(e ->{

        });
    }
}
