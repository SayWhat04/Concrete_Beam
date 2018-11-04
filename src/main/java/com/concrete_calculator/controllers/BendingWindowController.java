package com.concrete_calculator.controllers;

import com.concrete_calculator.utils.FxmlUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class BendingWindowController {

    private static final String RECTANGULAR_SECTION_BENDING_FXML = "/fxml/RectangularSectionBending.fxml";
    private static final String T_SECTION_BENDING_FXML = "/fxml/TSectionBending.fxml";

    @FXML
    BorderPane bendingBorderPane;

    public void openRectangularSectionBending() {
        bendingBorderPane.setCenter(FxmlUtils.fxmlLoader(RECTANGULAR_SECTION_BENDING_FXML));
    }

    public void openTSectionBending() {
        bendingBorderPane.setCenter(FxmlUtils.fxmlLoader(T_SECTION_BENDING_FXML));
    }
}
