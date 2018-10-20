package com.concrete_calculator.controllers;

import com.concrete_calculator.utils.FxmlUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TopMenuButtonsController topMenuButtonsController;

    @FXML
    private void initialize() {
        topMenuButtonsController.setMainController(this);
    }

    public void setCenter(String fxmlPath) {
        mainBorderPane.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
    }

    public void closeApplication() {
        //TODO: Add popup asking if user really wants to exit

        Platform.exit();
        System.exit(0);
    }

    public void aboutApplication() {
    }
}
