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

public class AddMotor extends Button {
    public AddMotor(String ev3){
        AddMotor addMotor = this;
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

                Stage primaryStage = (Stage) addMotor.getScene().getWindow();
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
            }
        });
    }
}
