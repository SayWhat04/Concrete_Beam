package com.concrete_calculator.controllers;

import com.concrete_calculator.models.CalculationModel;
import com.concrete_calculator.models.ForcesSet;
import com.concrete_calculator.reinforcement.ReinforcementProperties;
import com.concrete_calculator.geometry.Geometry;
import com.concrete_calculator.geometry.RectangularSection;
import com.concrete_calculator.geometry.Section;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.reinforcement.Rebar;
import com.concrete_calculator.materials.Steel;
import com.concrete_calculator.solvers.Solver;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Arrays;

public class RectangularSectionBendingController {

    private static final Object[] MAIN_BARS_DIAMETERS = {8, 10, 12, 14, 16, 20, 25, 28, 32, 40};
    private static final Object[] STIRRUPS_DIAMETERS = {6, 8, 10, 12};

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

        Section rectangularSection = new RectangularSection(Double.parseDouble(heightTextField.getText().toString()), Double.parseDouble(widthTextField.getText().toString()));
        Geometry calculatedGeometry = new Geometry(rectangularSection, 5000);

        ForcesSet pureBendingForcesSet = new ForcesSet();
        pureBendingForcesSet.setBendingMoment(Double.parseDouble(bendingMomentTextField.getText()));

        Rebar bottomMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(bottomMainBarsComboBox.getValue().toString()));
        Rebar topMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(topMainBarsComboBox.getValue().toString()));
        Rebar stirrups = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(stirrupsComboBox.getValue().toString()));
        double bottomReinforcementCoverage = Double.parseDouble(bottomCoverageTextField.getText().toString());
        double topReinforcementCoverage = Double.parseDouble(topCoverageTextField.getText().toString());
        ReinforcementProperties reinforcementProperties = new ReinforcementProperties(bottomMainBars, topMainBars, bottomReinforcementCoverage, topReinforcementCoverage);

        String selectedConcreteClass = concreteClassComboBox.getValue().toString();

        CalculationModel pureBendingCalculationModel = new CalculationModel(calculatedGeometry, pureBendingForcesSet, reinforcementProperties, Concrete.valueOf(selectedConcreteClass));

        Solver pureBendingSolver = new Solver();

        double[] reinforcement = pureBendingSolver.calculateReinforcement(pureBendingCalculationModel);

        System.out.println(Arrays.toString(reinforcement));

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
