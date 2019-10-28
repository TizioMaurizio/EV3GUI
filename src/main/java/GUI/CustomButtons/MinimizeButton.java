package GUI.CustomButtons;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class MinimizeButton extends Button {
    boolean minimized = false;
    List<Region> hidden = new ArrayList<>();
    public MinimizeButton(Button toMinimize, List<Region> linkedNodes) {
        this.setPrefWidth(10);
        this.setPrefHeight(10);
        this.setMinHeight(10);
        this.setMaxHeight(10);
        this.setStyle("-fx-background-color: GREEN; ");
        // dragBar functions
        this.setOnMousePressed(t -> {if(!minimized) {
            this.setStyle("-fx-background-color: RED; ");
            toMinimize.setVisible(false);
            for (Region bt : linkedNodes) {
                if(bt.getLayoutY() + bt.getTranslateY() >= toMinimize.getLayoutY() + toMinimize.getTranslateY() && bt != toMinimize) {
                    if (bt.getLayoutY() + bt.getTranslateY() < toMinimize.getLayoutY() + toMinimize.getTranslateY() + toMinimize.getPrefHeight()) {
                        hidden.add(bt);
                        bt.setVisible(false);
                    }
                    else
                        bt.setTranslateY(bt.getTranslateY() - toMinimize.getPrefHeight());
                }
            }
            minimized = true;
        }
        else {
            this.setStyle("-fx-background-color: GREEN; ");
            toMinimize.setVisible(true);
            for (Region bt : linkedNodes) {
                if(bt.getLayoutY() + bt.getTranslateY() >= toMinimize.getLayoutY() + toMinimize.getTranslateY() && bt != toMinimize) {
                    if(hidden.contains(bt)){
                        bt.setVisible(true);
                        hidden.remove(bt);
                    }
                    else
                        bt.setTranslateY(bt.getTranslateY() + toMinimize.getPrefHeight());
                }
            }
            minimized = false;
        }
        });
    }
}


/*if(!minimized) {
                toMinimize.setVisible(false);
                for (Region bt : linkedNodes) {
                    if(bt.getLayoutY() + bt.getTranslateY() >= toMinimize.getLayoutY() + bt.getTranslateY() && bt != toMinimize) {
                        if (bt.getLayoutY() + bt.getTranslateY() <= toMinimize.getLayoutY() + bt.getTranslateY() - toMinimize.getPrefHeight()) {
                            bt.setVisible(false);
                        }
                        bt.setLayoutY(bt.getLayoutY() - toMinimize.getPrefHeight());
                    }
                }
                minimized = true;
            }
            else {
                toMinimize.setVisible(true);
                for (Region bt : linkedNodes) {
                    if(bt.getLayoutY() + bt.getTranslateY() >= toMinimize.getLayoutY() + bt.getTranslateY() && bt != toMinimize) {
                        if (bt.getLayoutY() + bt.getTranslateY() <= toMinimize.getLayoutY() + bt.getTranslateY() - toMinimize.getPrefHeight()) {
                            bt.setVisible(true);
                        }
                        bt.setLayoutY(bt.getLayoutY() + toMinimize.getPrefHeight());
                    }
                }
                minimized = false;
            }*/