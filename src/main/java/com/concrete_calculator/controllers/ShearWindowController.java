package com.concrete_calculator.controllers;

import com.concrete_calculator.utils.FxmlUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class ShearWindowController {

    private static final String RECTANGULAR_SECTION_SHEAR_FXML = "/fxml/RectangularSectionShear.fxml";
    private static final String T_SECTION_SHEAR_FXML = "/fxml/TSectionShear.fxml";

    @FXML
    BorderPane bendingBorderPane;

    public void openRectangularSectionShear() {
        bendingBorderPane.setCenter(FxmlUtils.fxmlLoader(RECTANGULAR_SECTION_SHEAR_FXML));
    }

    public void openTSectionShear() {
        bendingBorderPane.setCenter(FxmlUtils.fxmlLoader(T_SECTION_SHEAR_FXML));
    }
}
