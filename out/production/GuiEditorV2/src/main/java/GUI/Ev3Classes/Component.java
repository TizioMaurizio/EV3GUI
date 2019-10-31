package GUI.Ev3Classes;

import GUI.CustomButtons.MqttButton;

//  Represents a Motor or a Sensor
public class Component {
    public String getName() {
        return name;
    }

    public Ev3 getEv3(){
        return ev3;
    }
    public String getKind() {
        return kind;
    }

    public String getType() {
        return type;
    }

    public int getPort() {
        return port;
    }

    public MqttButton getPanel() {
        return panel;
    }

    public void setPanel(MqttButton panel) {
        this.panel = panel;
    }

    private String name;
    private Ev3 ev3;
    private String kind;
    private String type;

    private MqttButton panel = new MqttButton();

    public void setPort(int port) {
        this.port = port;
    }

    private int port; // (position in ev3 initialize array, 1-3-5-7 for motors, 9-10-11-12 for sensors)

    public Component(Ev3 newEv3, String newName, String newType){
        ev3 = newEv3;
        name = newName;
        type = newType;
    }

    public static int parsePort(String port){
        try {
            //  port assegnato dagli switch dei ButtonAdd
            return Integer.parseInt(port.toString());
        } catch(NumberFormatException q){
            return -1;
        }
    }

}
