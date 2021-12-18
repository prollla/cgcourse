package com.vsu.cgcourse;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameOverController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label resultLabel;

    @FXML
    void initialize() {
        resultLabel.setText("Player: " + NameController.name  +  System.lineSeparator() +
                "Game Over." + System.lineSeparator() + "Your difficulty level:" + GameController.lvlProfile + System.lineSeparator()
                + "Your result:" + GameController.score);
    }

}