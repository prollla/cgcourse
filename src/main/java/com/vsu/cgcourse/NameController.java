package com.vsu.cgcourse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NameController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nameField;

    @FXML
    private Button playButton;

    public static String name;

    @FXML
    void initialize() {
        playButton.setOnAction(event -> {
            if(nameField.getText().length()==0){
                return;
            }
            else {
                name = nameField.getText();
                Stage stage = (Stage) playButton.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/game.fxml"));
                Parent parent = null;

                try {
                    parent = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("PlayGame");
                stage.setResizable(false);
                stage.setScene(new Scene(parent));
                stage.show();
            }
        });


        }
    }