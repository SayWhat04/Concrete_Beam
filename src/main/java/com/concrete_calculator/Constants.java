package com.concrete_calculator;

import com.concrete_calculator.materials.Concrete;

import java.text.DecimalFormat;

public class Constants {
    //DISTANCE
    public static final double METER_TO_CENTIMETER = 100;
    public static final double METER_TO_MILIMETER = 1000;
    public static final double CENTIMETER_TO_METER = 0.01;
    public static final double MILIMETER_TO_METER = 0.001;

    //FORCES
    public static final double KILONEWTON_TO_NEWTON = 1000;
    public static final double NEWTON_TO_KILONEWTON = 0.001;

    //AREA
    public static final double SQUAREMILIMETERS_TO_SQUARECENTIMETERS = 0.01;

    //SHEAR
    public static final double SHEAR_FACTOR_k_l_ = 0.15;
    public static final double SHEAR_FACTOR_C_Rd_c = 0.18 / Concrete.CONCRETE_PARTIAL_FACTOR;
    public static final String THETA_ANGLE_MAX ="45";
    public static final String THETA_ANGLE_MIN ="26.7";

    //BARS DIAMETERS LISTS, Values in [mm]
    public static final Object[] MAIN_BARS_DIAMETERS = {8, 10, 12, 14, 16, 20, 25, 28, 32, 40};
    public static final Object[] STIRRUPS_DIAMETERS = {6, 8, 10, 12};

    //NUMBER FORMATS
    public static final DecimalFormat twoDigitsAfterDecimal = new DecimalFormat("#0.00");

}
