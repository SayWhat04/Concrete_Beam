package com.concrete_calculator.controllers;

import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.Rebar;
import com.concrete_calculator.materials.Steel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RectangularSectionBendingController {

    public static final Object[] MAIN_BARS_DIAMETERS = {8, 10, 12, 14, 16, 20, 25, 28, 32, 40};
    public static final Object[] STIRRUPS_DIAMETERS = {8, 10, 12};

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

        bottomMainBarsComboBox.getItems().addAll(MAIN_BARS_DIAMETERS);
        topMainBarsComboBox.getItems().addAll(MAIN_BARS_DIAMETERS);
        stirrupsComboBox.getItems().addAll(STIRRUPS_DIAMETERS);
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
        Rebar bottomMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(bottomMainBarsComboBox.getValue().toString()));
        Rebar topMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(topMainBarsComboBox.getValue().toString()));
        Rebar stirrups = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(stirrupsComboBox.getValue().toString()));

        /* ADDED FOR TESTS
        System.out.println("Bottom bars:");
        System.out.println(bottomMainBars.getBarDiameter());
        System.out.println(bottomMainBars.getSteelType());
        System.out.println("Top bars:");
        System.out.println(topMainBars.getBarDiameter());
        System.out.println(topMainBars.getSteelType());
        System.out.println("Stirrups bars:");
        System.out.println(stirrups.getBarDiameter());
        System.out.println(stirrups.getSteelType());
        */
    }
}
