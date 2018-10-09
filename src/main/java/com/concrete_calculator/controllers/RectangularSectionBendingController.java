package com.concrete_calculator.controllers;

import com.concrete_calculator.Concrete;
import com.concrete_calculator.Steel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RectangularSectionBendingController {

    @FXML
    TextField widthTextField;

    @FXML
    TextField heightTextField;

    @FXML
    ComboBox bottomMainBarsComboBox;

    @FXML
    ComboBox topMainBarsComboBox;

    @FXML
    ComboBox stirrupsComboBox;

    @FXML
    TextField bottomCoverageTextField;

    @FXML
    TextField topCoverageTextField;

    @FXML
    ComboBox<Concrete> concreteClassComboBox;

    @FXML
    ComboBox steelClassComboBox;

    @FXML
    TextField bendingMomentTextField;

    @FXML
    Button calculateButton;

    @FXML
    public void initialize() {

        bottomMainBarsComboBox.getItems().addAll(8, 10, 12, 14, 16, 20, 25, 28, 32, 40);
        topMainBarsComboBox.getItems().addAll(8, 10, 12, 14, 16, 20, 25, 28, 32, 40);
        stirrupsComboBox.getItems().addAll(8, 10, 12, 14, 16, 20, 25, 28, 32, 40);
        concreteClassComboBox.getItems().setAll(Concrete.values());
        steelClassComboBox.getItems().setAll(Steel.values());

        initBindings();
    }

    private void initBindings() {
        calculateButton.disableProperty().bind(Bindings.isEmpty(widthTextField.textProperty())
                .or(Bindings.isEmpty(heightTextField.textProperty()))
                .or(Bindings.isNull(bottomMainBarsComboBox.valueProperty()))
                .or(Bindings.isNull(topMainBarsComboBox.valueProperty()))
                .or(Bindings.isNull(stirrupsComboBox.valueProperty()))
                .or(Bindings.isEmpty(bottomCoverageTextField.textProperty()))
                .or(Bindings.isEmpty(topCoverageTextField.textProperty()))
                .or(Bindings.isNull(concreteClassComboBox.valueProperty()))
                .or(Bindings.isNull(steelClassComboBox.valueProperty()))
                .or(Bindings.isEmpty(bendingMomentTextField.textProperty()))
        );
    }

    public void calculateBending() {

    }
}
