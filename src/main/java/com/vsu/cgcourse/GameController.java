package com.vsu.cgcourse;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;
import com.vsu.cgcourse.math.utils.Vector3f;
import com.vsu.cgcourse.model.Collision;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.obj_reader.ObjReader;
import com.vsu.cgcourse.render_engine.Camera;
import com.vsu.cgcourse.render_engine.GraphicConveyor;
import com.vsu.cgcourse.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;

import javax.swing.*;

public class GameController implements ActionListener{

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MenuItem easy;
    @FXML
    private MenuItem simple;
    @FXML
    private MenuItem unreal;

    @FXML
    private Canvas canvas;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem loadGame;

    @FXML
    private MenuItem loadObstacle;
    @FXML
    private MenuItem startGame;
    @FXML
    private MenuItem getResult;

    @FXML
    private Label hpLabel;
    String fileObstacle;
    final private float TRANSLATION = 30F;
    LinkedList<Mesh> currentMeshes = new LinkedList<>();
    private final Timer timer = new Timer(200, this);
    int spawner = 0;
    static int score;
    static String lvlProfile;
    int carPos = 0;
    int lvl=8;
    int buttonClick1=0;
    int buttonClick2=0;
    int buttonClick3=0;
    int obstaclePosition;
    private  int health = 3;
    private final Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);


    @FXML
    void moveLeft(ActionEvent event) {
        if (carPos > -1) {
            currentMeshes.getFirst().matrix = GraphicConveyor.modelMatrix(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), new Vector3f(TRANSLATION * 3 / 4, 0, 0),
                    currentMeshes.getFirst()).matrix;
            carPos--;
        }

    }

    @FXML
    void moveRight(ActionEvent event) {
        if (carPos < 1) {
            currentMeshes.getFirst().matrix = GraphicConveyor.modelMatrix(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), new Vector3f(-TRANSLATION*3/4, 0, 0),
                    currentMeshes.getFirst()).matrix;
            carPos++;
        }

    }

    @FXML
    void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));
            if (currentMeshes != null) {
                hpLabel.setText("Player: " + NameController.name  + System.lineSeparator()  + "Health: " +  "  "  + String.valueOf(health) + System.lineSeparator()+ "Score: " + " " +  String.valueOf(score));
                for (Mesh mesh : currentMeshes) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
                }
            }
        });
        timeline.getKeyFrames().add(frame);
        timeline.play();
        startGame.setOnAction(event -> {
            if(buttonClick1==1 && buttonClick2==1 && buttonClick3==1 && health==3){
            timer.start();}
        });
        easy.setOnAction(event -> {
            if(buttonClick3==0) {
                buttonClick3++;
                lvl = 6;
                lvlProfile="Easy.";
            }

        });
        simple.setOnAction(event -> {
            if(buttonClick3==0) {
                buttonClick3++;
                lvl = 4;
                lvlProfile="Simple.";
            }

        });
        unreal.setOnAction(event -> {
            if(buttonClick3==0) {
                buttonClick3++;
                lvl = 2;
                lvlProfile="Unreal.";
            }
        });
        loadGame.setOnAction(event -> {
            if(buttonClick1==0) {
                buttonClick1++;
                camera.movePosition(new Vector3f(0, 50, 0));
                camera.movePosition(new Vector3f(0, 0, TRANSLATION / 2));
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
                fileChooser.setTitle("Load Model");
                File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
                if (file == null) {
                    return;
                }
                Path fileName = Path.of(file.getAbsolutePath());
                try {
                    String fileContent = Files.readString(fileName);
                    currentMeshes.addFirst(ObjReader.read(fileContent));
                    currentMeshes.getFirst().matrix = GraphicConveyor.modelMatrix(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), new Vector3f(0, 0, TRANSLATION * (3f / 2)),
                            currentMeshes.getFirst()).matrix;
                } catch (IOException exception) {
                    GuiController.windowCall(exception.getMessage());
                }
            }

        });
        loadObstacle.setOnAction(event -> {
            if(buttonClick2==0) {
                buttonClick2++;
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
                fileChooser.setTitle("Load Model");
                File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
                if (file == null) {
                    return;
                }
                Path fileName = Path.of(file.getAbsolutePath());
                try {
                    fileObstacle = Files.readString(fileName);
                    currentMeshes.addLast(ObjReader.read(fileObstacle));
                } catch (IOException exception) {
                    GuiController.windowCall(exception.getMessage());
                }
            }
        });
        getResult.setOnAction(event -> {
            if(health==0) {
                Stage stage;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/gameOver.fxml"));
                Parent parent = null;

                try {
                    parent = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Stats");
                stage.setResizable(false);
                stage.setScene(new Scene(parent));
                stage.show();
            }
        });

    }
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == timer) {
            spawner++;
            score++;
            if (spawner == lvl) {
                generateObstacles();
                spawner = 0;

            }
            if (currentMeshes.size() > 12-lvl) {
               currentMeshes.remove(1);

            }
            moveObstacles(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), new Vector3f(0, 0, 3F));

            for (int i = 1; i < currentMeshes.size(); i++) {
                if (Collision.testInterSection(currentMeshes.getFirst(), currentMeshes.get(i))) {
                    health-=1;
                    if(health==0){
                        timer.stop();
                    }
                    }
                }
            }
        }
       /* private void windowOver(){
            Stage stage;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/gameOver.fxml"));
            Parent parent = null;

            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Stats");
            stage.setResizable(false);
            stage.setScene(new Scene(parent));
            stage.show();
        } */
        private void generateObstacles() {
            obstaclePosition = rnd.nextInt(3);
            Mesh obs = ObjReader.read(fileObstacle);
            generatePosition(obstaclePosition, obs);
            currentMeshes.addLast(obs);
        }

        private void generatePosition(int obstaclePosition, Mesh obs) {
            obs.matrix = GraphicConveyor.modelMatrix(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), new Vector3f((1 - obstaclePosition) * 22.5F, 0, 0), obs).matrix;
        }

    private void moveObstacles(Vector3f scale, Vector3f rotate, Vector3f translate) {
        for (int i = 1; i < currentMeshes.size(); i++) {
            currentMeshes.get(i).matrix = GraphicConveyor.modelMatrix(scale, rotate, translate, currentMeshes.get(i)).matrix;
        }
    }
    Random rnd = new Random();
    }

