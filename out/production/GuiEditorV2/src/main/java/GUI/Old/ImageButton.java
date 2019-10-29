package GUI.Old;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Rotate;

import java.util.Map;

public class ImageButton extends Button {
    private Rotate rotation=new Rotate();
    public double getRotation(){
        return rotation.getAngle();
    }
    private int group=0;
    public int getGroup(){
        return group;
    }
    Map imageByInt;
    public void setMap(Map imageByInt){
        this.imageByInt=imageByInt;
    }
    private int imageID=0;
    public void setGroup(int gr){
        group=gr;
    }
    private boolean fire=false;
    private ImageView image;
    private double scale=0.5;


    private String command;
    public void setCommand(String cmd){
        command=cmd;
    }
    public String getCommand(){
        return command;
    }
    private String toShow;
    public void setToShow(String shw){
        toShow=shw;
    }
    public String getToShow(){
        return toShow;
    }
    private String util;
    public void setUtil(String utl){
        util=utl;
    }
    public String getUtil(){
        return util;
    }



    private boolean justCreated=true;
    private boolean holdCtrl=false;
    private boolean up=true;
    private double startH;
    private double startW;
    private boolean magnifyFive=false;
    public void setMagnifyFive(boolean set){
        this.magnifyFive=set;
    }
    public boolean getMagnifyFive(){
        return this.magnifyFive;
    }
    public ImageButton(double x, double y,double scale, Image img){
        this.scale=scale;
        command="none";
        this.setOpacity(0.3);
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setPrefSize(img.getWidth()*scale,img.getHeight()*scale);
        image=new ImageView(img);
        setImage(img);
        image.setLayoutX(0);
        image.setLayoutY(0);
        fit();
        start();
    }
    public ImageButton(double x, double y,Image img){
        command="none";
        this.setOpacity(0.3);
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setPrefSize(img.getWidth(),img.getHeight());
        image=new ImageView(img);
        setImage(img);
        image.setLayoutX(0);
        image.setLayoutY(0);
        fit();
        start();
    }
    private void start(){
        this.getImage().getTransforms().add(rotation);
        this.getTransforms().add(rotation);
        this.setGroup(0);
        this.setOnAction(action ->{
            this.requestFocus();
        });
        this.setOnKeyPressed(key ->{
            this.setText("cmd:"+this.getCommand()+"\nshw:"+this.getToShow()+"\nutl:"+this.getUtil()+"\ngroup:"+this.group);
            if (key.getCode() == KeyCode.T) {
                rotation.setAngle(rotation.getAngle() + 90);
            }

            if (key.getCode() == KeyCode.Q) {
                group--;
            }
            if (key.getCode() == KeyCode.E) {
                group++;
            }
            if (key.getCode() == KeyCode.W) {
                moveOthers(-scale,0);
            }
            if (key.getCode() == KeyCode.S) {
                moveOthers(+scale,0);
            }
            if (key.getCode() == KeyCode.A) {
                moveOthers(0,-scale);
            }
            if (key.getCode() == KeyCode.D) {
                moveOthers(0,scale);
            }
            if(key.getCode()==KeyCode.X){
                magnifyFive=!magnifyFive;
                if(magnifyFive){
                    scale*=5;
                    this.toScale(scale);
                }
                else {
                    scale /= 5;
                    this.toScale(scale);
                }
            }
            if(key.getCode()==KeyCode.PERIOD){
                scaleUp();
                updateScale();
            }
            if(key.getCode()==KeyCode.COMMA){
                scaleDown();
                updateScale();
            }
            if(key.getCode()==KeyCode.R){
                imageID++;
                if(imageID<0)
                    imageID=imageByInt.size()-imageID;
                this.setImage((Image)imageByInt.get(imageID%imageByInt.size()));
                fit();
            }
            if(key.getCode()==KeyCode.F){
                imageID--;
                if(imageID<0)
                    imageID=imageByInt.size()-imageID;
                this.setImage((Image)imageByInt.get(imageID%imageByInt.size()));
                fit();
            }
        });
    }
    public void moveOthers(double deltaY, double deltaX){
        for(Node nd: this.getParent().getChildrenUnmodifiable()) {
            if (nd instanceof ImageButton) {
                if (((ImageButton) nd).getGroup()==group){
                        nd.setTranslateY((nd.getTranslateY() + deltaY));
                        ((ImageButton) nd).getImage().setTranslateY(nd.getTranslateY());
                        nd.setTranslateX((nd.getTranslateX() + deltaX));
                        ((ImageButton) nd).getImage().setTranslateX(nd.getTranslateX());
                    }
            }
        }
    }

    public void updateScale(){//starting dimensions have to be multiplied by scale
        startH = this.getImage().getImage().getHeight();
        startW = this.getImage().getImage().getWidth();
        if(startW>startH) {
            scale = this.getPrefWidth() / startW;
        }
        else
            scale = this.getPrefHeight() / startH;
    }
    public double getScale(){
        return scale;
    }

    public void scaleTranslate(double scale){
        this.setPrefSize(this.getPrefWidth()*scale,this.getPrefHeight()*scale);
        this.getImage().setFitHeight(this.getPrefHeight());
        this.getImage().setFitWidth(this.getPrefWidth());
        this.getImage().setVisible(false);
        this.getImage().setVisible(true);
        this.setTranslateX(this.getTranslateX()*scale);
        this.setTranslateY(this.getTranslateY()*scale);
        this.getImage().setTranslateY(this.getTranslateY());
        this.getImage().setTranslateX(this.getTranslateX());
        updateScale();
    }
    public void toScale(double scale){
        double prevH;
        double prevW;
        if(this.getImage().getImage()!=null) {
             prevH = this.getImage().getImage().getHeight();
             prevW = this.getImage().getImage().getWidth();
        }
        else{
            prevH = this.getPrefHeight();
            prevW = this.getPrefWidth();
        }
        double ratio = prevW / prevH;
        double currW = prevW*scale;
        this.setPrefSize(currW,currW/ratio);
        this.getImage().setFitWidth(currW);
        this.getImage().setFitHeight(currW/ratio);
        this.getImage().setVisible(false);
        this.getImage().setVisible(true);
        this.scale=scale;
        updateScale();
    }
    private void scale(double s){
        double prevH = this.getPrefHeight();
        double prevW = this.getPrefWidth();
        double ratio = prevW / prevH;
        double currW = prevW+s;
        this.setPrefSize(currW,currW/ratio);
        this.getImage().setFitWidth(currW);
        this.getImage().setFitHeight(currW/ratio);
        this.getImage().setVisible(false);
        this.getImage().setVisible(true);
        updateScale();
    }
    private void scaleUp(){
        double s=10;
        scale(s);

    }
    private void scaleDown(){
        double s=-10;
        scale(s);
    }
    public ImageView getImage(){
        return image;
    }
    public double getW(){
        return image.getImage().getWidth();
    }
    public double getH(){
        return image.getImage().getHeight();
    }

    private void fit(){
        this.setPrefSize(this.getImage().getFitWidth(),this.getImage().getFitHeight());
    }

    public void setImage(Image img){

        image.setTranslateY(this.getTranslateY());
        image.setTranslateX(this.getTranslateX());
        image.setImage(img);
        if(justCreated) {
            this.toScale(this.scale);
            justCreated = false;
        }
        image.setPreserveRatio(true);
        image.setFitWidth(this.getPrefWidth());
        image.setFitHeight(this.getPrefHeight());
        image.setVisible(false);
        image.setVisible(true);
        updateScale();
    }

}
