package com.vsu.cgcourse;

import java.net.URL;
import java.util.ResourceBundle;

import com.vsu.cgcourse.math.utils.Matrix4f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.obj_writer.ObjWriter;
import com.vsu.cgcourse.render_engine.GraphicConveyor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SaveController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonWithout;

    @FXML
    private TextField giveText;

    private Mesh mesh = null;
    private Mesh unmoddedMesh = null;

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setUnmoddedMesh(Mesh unmoddedMesh) {

        this.unmoddedMesh = unmoddedMesh;
    }

    @FXML
    void initialize() {
        buttonSave.setOnAction(event -> {
            if (mesh != null) {
                Matrix4f matrix4f = new Matrix4f(mesh.matrix);
                for (int i = 0; i < mesh.vertices.size(); i++) {
                    mesh.vertices.set(i, GraphicConveyor.multiplyMatrix4ByVector3(matrix4f, mesh.vertices.get(i)));
                }
                mesh.matrix = new Float[][]{
                        {1f, 0f, 0f, 0f},
                        {0f, 1f, 0f, 0f},
                        {0f, 0f, 1f, 0f},
                        {0f, 0f, 0f, 1f}
                };
                ObjWriter.saveOutput(mesh, giveText.getText());
            } else {
                GuiController.windowCall("Error: Model not found");

            }

        });
        buttonWithout.setOnAction(event -> {
            if (mesh != null) {
                ObjWriter.saveOutput(unmoddedMesh, giveText.getText());
            } else {
                GuiController.windowCall("Error: Model not found");

            }
        });

    }

}
