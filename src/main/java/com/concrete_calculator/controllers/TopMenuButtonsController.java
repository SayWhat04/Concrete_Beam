package com.concrete_calculator.controllers;

public class TopMenuButtonsController {

    private static final String BENDING_WINDOW_FXML = "/fxml/BendingWindow.fxml";
    private static final String SHEAR_WINDOW_FXML = "/fxml/ShearWindow.fxml";
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void openCalculationBending() {
        mainController.setCenter(BENDING_WINDOW_FXML);
    }

    public void openCalculationShear() {
        mainController.setCenter(SHEAR_WINDOW_FXML);
    }

    public void openCalculationTorsion() {
    }
}

