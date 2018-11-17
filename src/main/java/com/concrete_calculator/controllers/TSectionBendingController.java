package com.concrete_calculator.controllers;

import com.concrete_calculator.geometry.Geometry;
import com.concrete_calculator.geometry.RectangularSection;
import com.concrete_calculator.geometry.Section;
import com.concrete_calculator.geometry.TSection;
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

public class TSectionBendingController {

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
    private TextField flangeWidthTextField;

    @FXML
    private TextField flangeHeightTextField;

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
    private ComboBox<Concrete> concreteClassComboBox;

    @FXML
    private ComboBox<Steel> steelClassComboBox;

    @FXML
    private TextField bendingMomentTextField;

    @FXML
    private TextField bottomCalculatedReinforcementTextField;

    @FXML
    private TextField bottomBarsNumberTextField;

    @FXML
    private TextField bottomActualReinforcementTextField;

    @FXML
    private TextField topCalculatedReinforcementTextField;

    @FXML
    private TextField topBarsNumberTextField;

    @FXML
    private TextField topActualReinforcementTextField;

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
                .or(Bindings.isEmpty(flangeWidthTextField.textProperty()))
                .or(Bindings.isEmpty(flangeHeightTextField.textProperty()))
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
        bendingMomentTextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(ANY_DOUBLE_REGEX)) {
                    bendingMomentTextField.setText(oldValue);
                }
            }
        });
    }

    public void calculateBending() {
        //TODO:Verify if method is working
        Section tSection = new TSection(Double.parseDouble(heightTextField.getText()), Double.parseDouble(widthTextField.getText()), Double.parseDouble(flangeHeightTextField.getText()), Double.parseDouble(flangeWidthTextField.getText()));
        Geometry calculatedGeometry = new Geometry(tSection, Double.parseDouble(lengthTextField.getText()));

        ForcesSet pureBendingForcesSet = new ForcesSet();
        pureBendingForcesSet.setBendingMoment(Double.parseDouble(bendingMomentTextField.getText()));

        Rebar bottomMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(bottomMainBarsComboBox.getValue().toString()));
        Rebar topMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(topMainBarsComboBox.getValue().toString()));
        Rebar stirrups = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(stirrupsComboBox.getValue().toString()));
        double bottomReinforcementCoverage = Double.parseDouble(bottomCoverageTextField.getText());
        double topReinforcementCoverage = Double.parseDouble(topCoverageTextField.getText());
        ReinforcementProperties reinforcementProperties = new ReinforcementProperties(bottomMainBars, topMainBars, stirrups, bottomReinforcementCoverage, topReinforcementCoverage);

        String selectedConcreteClass = concreteClassComboBox.getValue().toString();
        CalculationModel pureBendingCalculationModel = new CalculationModel(calculatedGeometry, pureBendingForcesSet, reinforcementProperties, Concrete.valueOf(selectedConcreteClass));
        Solver pureBendingSolver = new Solver();

        double[] reinforcement = pureBendingSolver.calculateReinforcementPureBendingTSection(pureBendingCalculationModel);

        double bottomReinforcementCrossSection = reinforcement[0];
        double topReinforcementCrossSection = reinforcement[1];
        //TEST
        System.out.println(Arrays.toString(reinforcement));

        int numberOfBottomBars = Solver.calculateNumberOfBars(bottomReinforcementCrossSection, bottomMainBars.getBarDiameter());
        int numberOfTopBars = Solver.calculateNumberOfBars(topReinforcementCrossSection, topMainBars.getBarDiameter());

        double actualBottomReinforcementCrossSection = numberOfBottomBars * (Math.PI * Math.pow(Integer.parseInt(bottomMainBarsComboBox.getValue().toString()), 2)) / (4);
        double actualTopReinforcementCrossSection = numberOfTopBars * (Math.PI * Math.pow(Integer.parseInt(topMainBarsComboBox.getValue().toString()), 2)) / (4);

        //TEST
        System.out.println("Number of Bottom Bars: " + numberOfBottomBars);
        System.out.println("Number of Top Bars: " + numberOfTopBars);

        bottomCalculatedReinforcementTextField.setText(twoDigitsAfterDecimal.format(bottomReinforcementCrossSection));
        bottomBarsNumberTextField.setText(Integer.valueOf(numberOfBottomBars).toString());
        bottomActualReinforcementTextField.setText(twoDigitsAfterDecimal.format(actualBottomReinforcementCrossSection));
        topCalculatedReinforcementTextField.setText(twoDigitsAfterDecimal.format(topReinforcementCrossSection));
        topBarsNumberTextField.setText(Integer.valueOf(numberOfTopBars).toString());
        topActualReinforcementTextField.setText(twoDigitsAfterDecimal.format(actualTopReinforcementCrossSection));
    }
}
