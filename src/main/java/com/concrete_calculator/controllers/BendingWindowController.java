package com.concrete_calculator.controllers;

import com.concrete_calculator.utils.FxmlUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class BendingWindowController {

    public static final String RECTANGULAR_SECTION_BENDING_FXML = "/fxml/RectangularSectionBending.fxml";

    @FXML
    BorderPane bendingBorderPane;

    public void openRectangularSectionBending() {
        bendingBorderPane.setCenter(FxmlUtils.fxmlLoader(RECTANGULAR_SECTION_BENDING_FXML));
    }

    public void openTSectionBending() {
    }
}
