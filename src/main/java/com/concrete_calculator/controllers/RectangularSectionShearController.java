package com.concrete_calculator.controllers;

import com.concrete_calculator.geometry.Geometry;
import com.concrete_calculator.geometry.RectangularSection;
import com.concrete_calculator.geometry.Section;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.materials.Steel;
import com.concrete_calculator.models.CalculationModel;
import com.concrete_calculator.models.ForcesSet;
import com.concrete_calculator.reinforcement.Rebar;
import com.concrete_calculator.reinforcement.ReinforcementProperties;
import com.concrete_calculator.solvers.Solver;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.util.Arrays;

public class RectangularSectionShearController {

    //Values in [mm]
    private static final Object[] MAIN_BARS_DIAMETERS = {8, 10, 12, 14, 16, 20, 25, 28, 32, 40};
    private static final Object[] STIRRUPS_DIAMETERS = {6, 8, 10, 12};

    private static final String POSITIVE_DOUBLE_REGEX = "\\d{0,7}([.]\\d{0,4})?";
    private static final String ANY_DOUBLE_REGEX = "-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?";
    private static final String POSITIVE_DOUBLE_NOT_STARTING_WITH_ZERO_REGEX = "(^[1-9]| \\d*)([.]\\d{0,4})?";
    private static final String POSITIVE_TWO_DIGITS_INTEGER = "^[1-9]{1,2}?$";

    //TODO: Create helper Class with number formats and units
    DecimalFormat twoDigitsAfterDecimal = new DecimalFormat("#0.00");

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField lengthTextField;

    @FXML
    private TextField thetaAngleTextField;

    @FXML
    private ComboBox bottomMainBarsComboBox;

    @FXML
    private ComboBox topMainBarsComboBox;

    @FXML
    private ComboBox stirrupsComboBox;

    @FXML
    private TextField bottomCoverageTextField;

    @FXML
    private TextField topCoverageTextField;

    @FXML
    private TextField bottomBarsQuantityTextField;

    @FXML
    private TextField stirrupArmsTextField;

    @FXML
    private TextField topBarsQuantityTextField;

    @FXML
    private ComboBox<Concrete> concreteClassComboBox;

    @FXML
    private ComboBox<Steel> steelClassComboBox;

    @FXML
    private TextField axialForceTextField;

    @FXML
    private TextField shearForceTextField;

    @FXML
    private TextField bottomCalculatedReinforcementTextField;

    @FXML
    private TextField topCalculatedReinforcementTextField;

    @FXML
    private TextField stirrupsTextField;

    @FXML
    private Button calculateButton;

    @FXML
    public void initialize() {
        fillComboBoxes();
        initBindings();
        //initListeners();
    }

    private void fillComboBoxes() {
        bottomMainBarsComboBox.getItems().addAll(MAIN_BARS_DIAMETERS);
        topMainBarsComboBox.getItems().addAll(MAIN_BARS_DIAMETERS);
        stirrupsComboBox.getItems().addAll(STIRRUPS_DIAMETERS);
        concreteClassComboBox.getItems().setAll(Concrete.values());
        steelClassComboBox.getItems().setAll(Steel.values());
    }

    private void initBindings() {
        calculateButton.disableProperty().bind(Bindings.isEmpty(widthTextField.textProperty())
                .or(Bindings.isEmpty(heightTextField.textProperty()))
                .or(Bindings.isEmpty(lengthTextField.textProperty()))
                .or(Bindings.isEmpty(thetaAngleTextField.textProperty()))
                .or(Bindings.isNull(bottomMainBarsComboBox.valueProperty()))
                .or(Bindings.isEmpty(bottomBarsQuantityTextField.textProperty()))
                .or(Bindings.isNull(topMainBarsComboBox.valueProperty()))
                .or(Bindings.isEmpty(topBarsQuantityTextField.textProperty()))
                .or(Bindings.isNull(stirrupsComboBox.valueProperty()))
                .or(Bindings.isEmpty(stirrupArmsTextField.textProperty()))
                .or(Bindings.isEmpty(bottomCoverageTextField.textProperty()))
                .or(Bindings.isEmpty(topCoverageTextField.textProperty()))
                .or(Bindings.isNull(concreteClassComboBox.valueProperty()))
                .or(Bindings.isNull(steelClassComboBox.valueProperty()))
                .or(Bindings.isEmpty(axialForceTextField.textProperty()))
                .or(Bindings.isEmpty(shearForceTextField.textProperty()))
        );
    }

    //TODO: Redesign method to use TextFormatter instead of Listeners?
    private void initListeners() {
        widthTextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(POSITIVE_DOUBLE_NOT_STARTING_WITH_ZERO_REGEX)) {
                    widthTextField.setText(oldValue);
                }
            }
        });
        heightTextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(POSITIVE_DOUBLE_NOT_STARTING_WITH_ZERO_REGEX)) {
                    heightTextField.setText(oldValue);
                }
            }
        });
    }

    public void calculateShear() {
        Section rectangularSection = new RectangularSection(Double.parseDouble(heightTextField.getText()), Double.parseDouble(widthTextField.getText()));
        Geometry calculatedGeometry = new Geometry(rectangularSection, Double.parseDouble(lengthTextField.getText()));

        ForcesSet shearForcesSet = new ForcesSet();
        shearForcesSet.setShearForce(Double.parseDouble(shearForceTextField.getText()));
        shearForcesSet.setAxialForce(Double.parseDouble(axialForceTextField.getText()));

        double numberOfBottomBars = Double.parseDouble(bottomBarsQuantityTextField.getText());
        double numberOfTopBars = Double.parseDouble(topBarsQuantityTextField.getText());
        double numberOfStirrupArms =Double.parseDouble(stirrupArmsTextField.getText());

        Rebar bottomMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(bottomMainBarsComboBox.getValue().toString()));
        Rebar topMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(topMainBarsComboBox.getValue().toString()));
        Rebar stirrups = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(stirrupsComboBox.getValue().toString()));
        double bottomReinforcementCoverage = Double.parseDouble(bottomCoverageTextField.getText());
        double topReinforcementCoverage = Double.parseDouble(topCoverageTextField.getText());
        ReinforcementProperties reinforcementProperties = new ReinforcementProperties(bottomMainBars, topMainBars, stirrups, bottomReinforcementCoverage, topReinforcementCoverage);

        reinforcementProperties.setThetaAngleShear(Double.parseDouble(thetaAngleTextField.getText()));
        double bottomReinforcementCrossSection = numberOfBottomBars * (Math.PI * Math.pow(Integer.parseInt(bottomMainBarsComboBox.getValue().toString()), 2)) / (4);
        double topReinforcementCrossSection = numberOfTopBars * (Math.PI * Math.pow(Integer.parseInt(topMainBarsComboBox.getValue().toString()), 2)) / (4);
        double stirrupCrossSection = numberOfStirrupArms * (Math.PI * Math.pow(Integer.parseInt(stirrupsComboBox.getValue().toString()), 2)) / (4);

        reinforcementProperties.setBottomReinforcementCrossSection(bottomReinforcementCrossSection);
        reinforcementProperties.setTopReinforcementCrossSection(topReinforcementCrossSection);
        reinforcementProperties.setStirrupsCrossSection(stirrupCrossSection);

        String selectedConcreteClass = concreteClassComboBox.getValue().toString();
        CalculationModel shearCalculationModel = new CalculationModel(calculatedGeometry, shearForcesSet, reinforcementProperties, Concrete.valueOf(selectedConcreteClass));
        Solver shearSolver = new Solver();

        double shearReinforcementSpan = shearSolver.calculateReinforcementShearRectangularSection(shearCalculationModel);

        //TEST
        System.out.println("********************* " + new Object() {
        }.getClass().getEnclosingMethod().getName() + " ************************************");
        System.out.println("Shear reinforcement span: " + shearReinforcementSpan);
        System.out.println("Number of Bottom Bars: " + numberOfBottomBars);
        System.out.println("Number of Top Bars: " + numberOfTopBars);

        bottomCalculatedReinforcementTextField.setText(twoDigitsAfterDecimal.format(bottomReinforcementCrossSection));
        topCalculatedReinforcementTextField.setText(twoDigitsAfterDecimal.format(topReinforcementCrossSection));
        stirrupsTextField.setText(twoDigitsAfterDecimal.format(shearReinforcementSpan));
    }
}
