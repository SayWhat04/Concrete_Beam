package com.concrete_calculator.solvers;

import com.concrete_calculator.Constants;
import com.concrete_calculator.models.CalculationModel;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.materials.Steel;

public class Solver {

    //MAIN CALCULATION METHODS
    public double[] calculateReinforcementPureBendingRectangularSection(CalculationModel bendingCalculationModel) {

        double height = bendingCalculationModel.getElementGeometry().getSection().getHeight();
        double width = bendingCalculationModel.getElementGeometry().getSection().getWidth();

        double initialBendingMoment = bendingCalculationModel.getForcesSet().getBendingMoment();
        //For Calculation we need absolute value of BendingMoment
        double bendingMoment = Math.abs(initialBendingMoment);

        double bottomReinforcementDiameter = bendingCalculationModel.getReinforcementProperties().getBottomReinforcement().getBarDiameter();
        double topReinforcementDiameter = bendingCalculationModel.getReinforcementProperties().getTopReinforcement().getBarDiameter();
        double stirrupsDiameter = bendingCalculationModel.getReinforcementProperties().getStirrup().getBarDiameter();
        Steel bottomReinforcementSteelType = bendingCalculationModel.getReinforcementProperties().getBottomReinforcement().getSteelType();
        Steel topReinforcementSteelType = bendingCalculationModel.getReinforcementProperties().getTopReinforcement().getSteelType();
        Steel stirrupsSteelType = bendingCalculationModel.getReinforcementProperties().getStirrup().getSteelType();
        double bottomReinforcementCoverage = bendingCalculationModel.getReinforcementProperties().getBottomCoverage();
        double topReinforcementCoverage = bendingCalculationModel.getReinforcementProperties().getTopCoverage();

        Concrete concreteClassOfElement = bendingCalculationModel.getConcreteType();

        //a1: distance between bottom edge of beam and center of bottom reinforcement; a2: distance between top edge of beam and center of top reinforcement;
        double a1 = bottomReinforcementCoverage + 0.5 * bottomReinforcementDiameter + stirrupsDiameter;
        double a2 = topReinforcementCoverage + 0.5 * topReinforcementDiameter + stirrupsDiameter;

        double bottomReinforcementEffectiveDepth = height - a1;
        double topReinforcementEffectiveDepth = height - a2;

        double concreteCharCompressiveStrength = concreteClassOfElement.getF_ck();
        double concreteDesignCompressiveStrength = concreteCharCompressiveStrength / Concrete.CONCRETE_PARTIAL_FACTOR;

        double bottomReinforcementSteelStrengthChar = bottomReinforcementSteelType.getF_yk();
        double bottomReinforcementSteelStrengthCalc = bottomReinforcementSteelStrengthChar / Steel.STEEL_PARTIAL_FACTOR;

        double topReinforcementSteelStrengthChar = topReinforcementSteelType.getF_yk();
        double topReinforcementSteelStrengthCalc = topReinforcementSteelStrengthChar / Steel.STEEL_PARTIAL_FACTOR;

        double eta = concreteClassOfElement.getEta();
        double dzeta_ef_lim = concreteClassOfElement.getDzeta_ef_lim();


        System.out.println("Eta: " + eta);
        System.out.println("Dzeta_ef_lim: " + dzeta_ef_lim);        //TEST
        System.out.println("M_Ed: " + initialBendingMoment);
        System.out.println("h: " + height);
        System.out.println("b: " + width);
        System.out.println("d_bottom: " + bottomReinforcementEffectiveDepth);
        System.out.println("d_top: " + topReinforcementEffectiveDepth);
        System.out.println("concreteCharCompressiveStrength: " + concreteCharCompressiveStrength);
        System.out.println("concreteDesignCompressiveStrength: " + concreteDesignCompressiveStrength);
        System.out.println("bottomReinforcementSteelStrengthChar: " + bottomReinforcementSteelStrengthChar);
        System.out.println("bottomReinforcementSteelStrengthCalc: " + bottomReinforcementSteelStrengthCalc);
        System.out.println("topReinforcementSteelStrengthChar: " + topReinforcementSteelStrengthChar);
        System.out.println("topReinforcementSteelStrengthCalc: " + topReinforcementSteelStrengthCalc);

        //TODO: Think about implementation of methods for checking maximum and minimum reinforcement
        double maxBottomReinforcement = calculateMaximumReinforcementPureBending(width, bottomReinforcementEffectiveDepth);
        double maxTopReinforcement = calculateMaximumReinforcementPureBending(width, topReinforcementEffectiveDepth);

        if (initialBendingMoment > 0) {
            double mi = calculateMi(width, bendingMoment, bottomReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta);
            //TEST
            System.out.println("Mi: " + mi);

            double dzeta_ef = calculateDzeta_ef(mi);
            //TEST
            System.out.println("Dzeta_ef: " + dzeta_ef);

            if (dzeta_ef <= dzeta_ef_lim) {
                return calculateOnlyExtendedReinforcementBending(bottomReinforcementEffectiveDepth, bottomReinforcementSteelStrengthCalc, bendingMoment, dzeta_ef);
            } else {
                return calculateExtendedAndCompressedReinforcementBending(width, topReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta, dzeta_ef_lim, bendingMoment, a2, bottomReinforcementSteelStrengthCalc);
            }

        } else {
            double mi = calculateMi(width, bendingMoment, topReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta);
            //TEST
            System.out.println("Mi: " + mi);

            double dzeta_ef = calculateDzeta_ef(mi);
            //TEST
            System.out.println("Dzeta_ef: " + dzeta_ef);

            if (dzeta_ef <= dzeta_ef_lim) {
                double[] calculatedReinforcement = calculateOnlyExtendedReinforcementBending(topReinforcementEffectiveDepth, topReinforcementSteelStrengthCalc, bendingMoment, dzeta_ef);
                //Positions of values in array had to be swapped because we need to always have bottom reinforcement in position[0] and top reinforcement in position[1]
                double[] swappedArray = swapValuesInArray(calculatedReinforcement);
                return swappedArray;
            } else {
                double[] calculatedReinforcement = calculateExtendedAndCompressedReinforcementBending(width, bottomReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta, dzeta_ef_lim, bendingMoment, a1, bottomReinforcementSteelStrengthCalc);
                //Positions of values in array had to be swapped because we need to always have bottom reinforcement in position[0] and top reinforcement in position[1]
                double[] swappedArray = swapValuesInArray(calculatedReinforcement);
                return swappedArray;
            }
        }
    }

    public double[] calculateReinforcementPureBendingTSection(CalculationModel bendingCalculationModel) {
        double height = bendingCalculationModel.getElementGeometry().getSection().getHeight();
        double width = bendingCalculationModel.getElementGeometry().getSection().getWidth();
        double flangeWidth = bendingCalculationModel.getElementGeometry().getSection().getFlangeWidth();
        double flangeHeight = bendingCalculationModel.getElementGeometry().getSection().getFlangeHeight();

        double initialBendingMoment = bendingCalculationModel.getForcesSet().getBendingMoment();
        //For Calculation we need absolute value of BendingMoment
        double bendingMoment = Math.abs(initialBendingMoment);

        double bottomReinforcementDiameter = bendingCalculationModel.getReinforcementProperties().getBottomReinforcement().getBarDiameter();
        double topReinforcementDiameter = bendingCalculationModel.getReinforcementProperties().getTopReinforcement().getBarDiameter();
        double stirrupsDiameter = bendingCalculationModel.getReinforcementProperties().getStirrup().getBarDiameter();
        Steel bottomReinforcementSteelType = bendingCalculationModel.getReinforcementProperties().getBottomReinforcement().getSteelType();
        Steel topReinforcementSteelType = bendingCalculationModel.getReinforcementProperties().getTopReinforcement().getSteelType();
        Steel stirrupsSteelType = bendingCalculationModel.getReinforcementProperties().getStirrup().getSteelType();
        double bottomReinforcementCoverage = bendingCalculationModel.getReinforcementProperties().getBottomCoverage();
        double topReinforcementCoverage = bendingCalculationModel.getReinforcementProperties().getTopCoverage();

        Concrete concreteClassOfElement = bendingCalculationModel.getConcreteType();

        //a1: distance between bottom edge of beam and center of bottom reinforcement; a2: distance between top edge of beam and center of top reinforcement;
        double a1 = bottomReinforcementCoverage + 0.5 * bottomReinforcementDiameter + stirrupsDiameter;
        double a2 = topReinforcementCoverage + 0.5 * topReinforcementDiameter + stirrupsDiameter;

        double bottomReinforcementEffectiveDepth = height - a1;
        double topReinforcementEffectiveDepth = height - a2;

        double concreteCharCompressiveStrength = concreteClassOfElement.getF_ck();
        double concreteDesignCompressiveStrength = concreteCharCompressiveStrength / Concrete.CONCRETE_PARTIAL_FACTOR;

        double bottomReinforcementSteelStrengthChar = bottomReinforcementSteelType.getF_yk();
        double bottomReinforcementSteelStrengthCalc = bottomReinforcementSteelStrengthChar / Steel.STEEL_PARTIAL_FACTOR;

        double topReinforcementSteelStrengthChar = topReinforcementSteelType.getF_yk();
        double topReinforcementSteelStrengthCalc = topReinforcementSteelStrengthChar / Steel.STEEL_PARTIAL_FACTOR;

        double eta = concreteClassOfElement.getEta();
        double dzeta_ef_lim = concreteClassOfElement.getDzeta_ef_lim();

        //TEST
        System.out.println("M_Ed: " + initialBendingMoment);
        System.out.println("h: " + height);
        System.out.println("b: " + width);
        System.out.println("d_bottom: " + bottomReinforcementEffectiveDepth);
        System.out.println("d_top: " + topReinforcementEffectiveDepth);
        System.out.println("concreteCharCompressiveStrength: " + concreteCharCompressiveStrength);
        System.out.println("concreteDesignCompressiveStrength: " + concreteDesignCompressiveStrength);
        System.out.println("bottomReinforcementSteelStrengthChar: " + bottomReinforcementSteelStrengthChar);
        System.out.println("bottomReinforcementSteelStrengthCalc: " + bottomReinforcementSteelStrengthCalc);
        System.out.println("topReinforcementSteelStrengthChar: " + topReinforcementSteelStrengthChar);
        System.out.println("topReinforcementSteelStrengthCalc: " + topReinforcementSteelStrengthCalc);
        System.out.println("Eta: " + eta);
        System.out.println("Dzeta_ef_lim: " + dzeta_ef_lim);

        if (initialBendingMoment > 0) {
            double mi = calculateMi(flangeWidth, bendingMoment, bottomReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta);
            //TEST
            System.out.println("Mi: " + mi);

            double dzeta_ef = calculateDzeta_ef(mi);
            //TEST
            System.out.println("Dzeta_ef: " + dzeta_ef);

            double tSectionFactor = checkIfSectionIsRealTSection(flangeHeight, bottomReinforcementEffectiveDepth);

            if (tSectionFactor >= dzeta_ef) {
                System.out.println("Section is not real T-Section");
                System.out.println("Compressed reinforcement is not needed");
                return calculateOnlyExtendedReinforcementBending(bottomReinforcementEffectiveDepth, bottomReinforcementSteelStrengthCalc, bendingMoment, dzeta_ef);

            } else {
                System.out.println("Section is real T-Section");
                if (dzeta_ef <= dzeta_ef_lim) {
                    System.out.println("Compressed reinforcement is not needed");
                    return calculateExtendedReinforcementTSection(width, flangeWidth, flangeHeight, bendingMoment, bottomReinforcementEffectiveDepth, concreteDesignCompressiveStrength, bottomReinforcementSteelStrengthCalc, eta);
                } else {
                    System.out.println("Compressed reinforcement is needed");
                    double[] extendedReinforcement = calculateExtendedReinforcementTSection(width, flangeWidth, flangeHeight, bendingMoment, bottomReinforcementEffectiveDepth, concreteDesignCompressiveStrength, bottomReinforcementSteelStrengthCalc, eta);
                    //TODO: Make sure that value of width is correct!!!
                    double[] compressedReinforcement = calculateExtendedAndCompressedReinforcementBending(width, bottomReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta, dzeta_ef_lim, bendingMoment, a2, bottomReinforcementSteelStrengthCalc);
                    double[] extendedAndCompressedReinforcement = {extendedReinforcement[0], compressedReinforcement[1]};
                    return extendedAndCompressedReinforcement;
                }
            }
        } else {
            double mi = calculateMi(width, bendingMoment, topReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta);
            //TEST
            System.out.println("Mi: " + mi);

            double dzeta_ef = calculateDzeta_ef(mi);
            //TEST
            System.out.println("Dzeta_ef: " + dzeta_ef);

            if (dzeta_ef <= dzeta_ef_lim) {
                double[] calculatedReinforcement = calculateOnlyExtendedReinforcementBending(topReinforcementEffectiveDepth, topReinforcementSteelStrengthCalc, bendingMoment, dzeta_ef);
                //Positions of values in array had to be swapped because we need to always have bottom reinforcement in position[0] and top reinforcement in position[1]
                double[] swappedArray = swapValuesInArray(calculatedReinforcement);
                return swappedArray;
            } else {
                double[] calculatedReinforcement = calculateExtendedAndCompressedReinforcementBending(width, bottomReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta, dzeta_ef_lim, bendingMoment, a1, topReinforcementSteelStrengthCalc);
                //Positions of values in array had to be swapped because we need to always have bottom reinforcement in position[0] and top reinforcement in position[1]
                double[] swappedArray = swapValuesInArray(calculatedReinforcement);
                return swappedArray;
            }
        }
    }

    public double calculateReinforcementShearRectangularSection(CalculationModel shearCalculationModel) {

        double height = shearCalculationModel.getElementGeometry().getSection().getHeight();
        double width = shearCalculationModel.getElementGeometry().getSection().getWidth();

        double shearForce = shearCalculationModel.getForcesSet().getShearForce();
        double axialForce = shearCalculationModel.getForcesSet().getAxialForce();

        double bottomReinforcementDiameter = shearCalculationModel.getReinforcementProperties().getBottomReinforcement().getBarDiameter();
        double topReinforcementDiameter = shearCalculationModel.getReinforcementProperties().getTopReinforcement().getBarDiameter();
        double stirrupsDiameter = shearCalculationModel.getReinforcementProperties().getStirrup().getBarDiameter();

        Steel bottomReinforcementSteelType = shearCalculationModel.getReinforcementProperties().getBottomReinforcement().getSteelType();
        Steel topReinforcementSteelType = shearCalculationModel.getReinforcementProperties().getTopReinforcement().getSteelType();
        Steel stirrupsSteelType = shearCalculationModel.getReinforcementProperties().getStirrup().getSteelType();
        double bottomReinforcementCoverage = shearCalculationModel.getReinforcementProperties().getBottomCoverage();
        double topReinforcementCoverage = shearCalculationModel.getReinforcementProperties().getTopCoverage();

        //a1: distance between bottom edge of beam and center of bottom reinforcement; a2: distance between top edge of beam and center of top reinforcement;
        double a1 = bottomReinforcementCoverage + 0.5 * bottomReinforcementDiameter + stirrupsDiameter;
        double a2 = topReinforcementCoverage + 0.5 * topReinforcementDiameter + stirrupsDiameter;

        double bottomReinforcementEffectiveDepth = height - a1;
        double topReinforcementEffectiveDepth = height - a2;

        double stirrupsSteelStrengthChar = stirrupsSteelType.getF_yk();
        double stirrupsSteelStrengthCalc = stirrupsSteelStrengthChar / Steel.STEEL_PARTIAL_FACTOR;

        double bottomReinforcementCrossSection = shearCalculationModel.getReinforcementProperties().getBottomReinforcementCrossSection();
        //TODO: Method for calculation of stirrups Cross Section
        double stirrupsCrossSection = shearCalculationModel.getReinforcementProperties().getStirrupsCrossSection();

        Concrete concreteClassOfElement = shearCalculationModel.getConcreteType();
        double concreteCharCompressiveStrength = concreteClassOfElement.getF_ck();
        double concreteDesignCompressiveStrength = concreteCharCompressiveStrength / Concrete.CONCRETE_PARTIAL_FACTOR;

        double thetaAngle = shearCalculationModel.getReinforcementProperties().getThetaAngleShear();

        //TEST
        System.out.println("V_Ed: " + shearForce);
        System.out.println("h: " + height);
        System.out.println("b: " + width);
        System.out.println("d_bottom: " + bottomReinforcementEffectiveDepth);
        System.out.println("d_top: " + topReinforcementEffectiveDepth);
        System.out.println("concreteCharCompressiveStrength: " + concreteCharCompressiveStrength);
        System.out.println("concreteDesignCompressiveStrength: " + concreteDesignCompressiveStrength);

        double shearResistanceWithoutReinforcement = calculateShearResistance(bottomReinforcementEffectiveDepth,
                bottomReinforcementCrossSection, width, height, axialForce, concreteCharCompressiveStrength, concreteDesignCompressiveStrength);

        if (shearForce >= shearResistanceWithoutReinforcement) {
            //TODO: Calculate shear reinforcement
            double spacing = calculateStirrupsSpacing(shearForce, thetaAngle, stirrupsSteelStrengthCalc, bottomReinforcementEffectiveDepth, stirrupsCrossSection);
            int roundedSpacing = roundUpToNearestSelectedValue(spacing, 10);
            return roundedSpacing;
        } else {
            //TODO: Calculate reqiurement span of reinforcement
            double spacing = 0.75 * bottomReinforcementEffectiveDepth;
            int roundedSpacing = roundUpToNearestSelectedValue(spacing, 10);
            return roundedSpacing;
        }
    }


    //BENDING CALCULATION HELPER METHODS
    private double[] calculateExtendedReinforcementTSection(double width, double flangeWidth, double flangeHeight, double bendingMoment, double bottomReinforcementEffectiveDepth, double concreteDesignCompressiveStrength, double bottomReinforcementSteelStrengthCalc, double eta) {
        //TODO: Nośność skrzydeł płyty
        double M_Rd_f = flangeHeight * (flangeWidth - width) * eta * concreteDesignCompressiveStrength * (bottomReinforcementEffectiveDepth - 0.5 * flangeHeight);
        double bendingMomentsDifference = bendingMoment - M_Rd_f;
        double newMi = calculateMi(width, bendingMomentsDifference, bottomReinforcementEffectiveDepth, concreteDesignCompressiveStrength, eta);
        double newDzeta_ef = calculateDzeta_ef(newMi);
        double z_c = calculateLeverArmOfInternalForces(bottomReinforcementEffectiveDepth, newDzeta_ef);
        double reinforcement_As_1 = flangeHeight * (flangeWidth - width) * ((eta * concreteDesignCompressiveStrength) / (bottomReinforcementSteelStrengthCalc)) + ((bendingMoment - M_Rd_f) / (z_c * bottomReinforcementSteelStrengthCalc));
        double reinforcement_As_2 = 0;
        double[] reinforcement = {reinforcement_As_1, reinforcement_As_2};
        return reinforcement;
    }

    private double[] calculateOnlyExtendedReinforcementBending(double extendedReinforcementEffectiveDepth,
                                                               double extendedReinforcementSteelStrengthCalc,
                                                               double bendingMoment, double dzeta_ef) {
        System.out.println("Compressed reinforcement is not needed");

        //Lever arm of internal forces
        double z_c = calculateLeverArmOfInternalForces(extendedReinforcementEffectiveDepth, dzeta_ef);
        //TEST
        System.out.println("z_c: " + z_c);

        //As_1: extended reinforcement; As_2: compressed reinforcement;
        double reinforcement_As_1 = (bendingMoment * Constants.KILONEWTON_TO_NEWTON * Constants.METER_TO_MILIMETER) / (z_c * extendedReinforcementSteelStrengthCalc);
        double reinforcement_As_2 = 0;
        double[] reinforcement = {reinforcement_As_1, reinforcement_As_2};
        return reinforcement;
    }

    private double[] calculateExtendedAndCompressedReinforcementBending(double width,
                                                                        double extendedReinforcementEffectiveDepth,
                                                                        double concreteDesignCompressiveStrength,
                                                                        double eta, double dzeta_ef_lim, double bendingMoment, double a2,
                                                                        double extendedReinforcementSteelStrengthCalc) {
        System.out.println("Compressed reinforcement is needed");

        //  TODO: maksymalna nosnosc przekroju pojedynczo zbrojonego
        double M_rd_pz = dzeta_ef_lim * (1 - 0.5 * dzeta_ef_lim) * width * extendedReinforcementEffectiveDepth * extendedReinforcementEffectiveDepth * eta * concreteDesignCompressiveStrength * Constants.NEWTON_TO_KILONEWTON * Constants.MILIMETER_TO_METER;
        //TEST
        System.out.println("M_rd_pz: " + M_rd_pz);

        //As_1: extended reinforcement; As_2: compressed reinforcement
        //TODO: Add value of f_yd
        double reinforcement_As_2 = ((bendingMoment - M_rd_pz) * Constants.KILONEWTON_TO_NEWTON * Constants.METER_TO_MILIMETER) / ((extendedReinforcementEffectiveDepth - a2) * extendedReinforcementSteelStrengthCalc);
        double reinforcement_As_1 = (((M_rd_pz) / ((1 - 0.5 * dzeta_ef_lim) * extendedReinforcementEffectiveDepth * extendedReinforcementSteelStrengthCalc)) * Constants.KILONEWTON_TO_NEWTON * Constants.METER_TO_MILIMETER) + reinforcement_As_2;
        double[] reinforcement = {reinforcement_As_1, reinforcement_As_2};
        return reinforcement;
    }

    private double calculateMi(double width, double bendingMoment, double extendedReinforcementEffectiveDepth, double concreteDesignCompressiveStrength, double eta) {
        return (bendingMoment * Constants.KILONEWTON_TO_NEWTON * Constants.METER_TO_MILIMETER) / (width * extendedReinforcementEffectiveDepth * extendedReinforcementEffectiveDepth * eta * concreteDesignCompressiveStrength);
    }

    private double calculateLeverArmOfInternalForces(double extendedReinforcementEffectiveDepth, double dzeta_ef) {
        return (1 - 0.5 * dzeta_ef) * extendedReinforcementEffectiveDepth;
    }

    private double checkIfSectionIsRealTSection(double flangeHeight, double bottomReinforcementEffectiveDepth) {
        double tSectionFactor = flangeHeight / bottomReinforcementEffectiveDepth;
        return tSectionFactor;
    }

    public double calculateMaximumReinforcementPureBending(double width, double effectiveDepth) {
        double maximumReinforcementPureBending = 0.04 * width * effectiveDepth;
        return maximumReinforcementPureBending;
    }

    public static int calculateNumberOfBars(double reinforcementCrossSectionArea, int barDiameter) {

        //TEST
        System.out.println("reinforcementCrossSectionArea: " + reinforcementCrossSectionArea);
        System.out.println("barDiameter: " + barDiameter);

        double crossSectionAreaOfBar = (Math.PI * Math.pow(barDiameter, 2)) / (4);
        double firstIterationNumberOfBars = reinforcementCrossSectionArea / crossSectionAreaOfBar;
        int secondIterationNumberOfBars;

        //TEST
        System.out.println("crossSectionAreaOfBar: " + crossSectionAreaOfBar);
        System.out.println("firstIterationNumberOfBars: " + firstIterationNumberOfBars);

        if (Math.round(firstIterationNumberOfBars) < firstIterationNumberOfBars) {
            secondIterationNumberOfBars = (int) (Math.round(firstIterationNumberOfBars)) + 1;
            //TEST
            System.out.println("secondIterationNumberOfBars: " + secondIterationNumberOfBars);

            return secondIterationNumberOfBars;
        } else {
            secondIterationNumberOfBars = (int) (Math.round(firstIterationNumberOfBars));
            //TEST
            System.out.println("secondIterationNumberOfBars: " + secondIterationNumberOfBars);

            return secondIterationNumberOfBars;
        }
    }

    //SHEAR CALCULATION HELPER METHODS
    public double calculateDistanceOnWhichShearReinforcementIsNeeded(double designShearForce, double designShearResistanceWithoutReinforcement, double linearLoad) {
        double distance = (designShearForce - designShearResistanceWithoutReinforcement) / linearLoad;
        return distance;
    }

    //TODO: Refactor to accept less arguments
    public double calculateShearResistance(double effectiveDepth,
                                           double extendedReinforcementCrossSectionArea,
                                           double width, double height,
                                           double axialForce,
                                           double concreteCharCompressiveStrength,
                                           double concreteDesignCompressiveStrength) {

        double C_Rd_c = Constants.SHEAR_FACTOR_C_Rd_c;
        double k_Factor = calculateShear_k_Factor(effectiveDepth);
        //TODO: Add getter of area
        double extendedSectionArea = width * effectiveDepth;
        double sectionCrossArea = width * height;
        double extendedReinforcementRatio = calculateReinforcementRatio(extendedReinforcementCrossSectionArea, extendedSectionArea);
        double compressiveStressFromAxialForce = calculateCompressiveStressFromAxialForce(axialForce, sectionCrossArea, concreteDesignCompressiveStrength);
        double ni_Min = calculateShear_Ni_Min(k_Factor, concreteCharCompressiveStrength);
        double k_l_Factor = Constants.SHEAR_FACTOR_k_l_;

        //Main calculation
        double shearResistance1 = (C_Rd_c * k_Factor * Math.cbrt(100 * extendedReinforcementRatio * concreteCharCompressiveStrength) + k_l_Factor * compressiveStressFromAxialForce) * width * effectiveDepth;
        double shearResistance2 = (ni_Min + k_l_Factor * compressiveStressFromAxialForce) * width * effectiveDepth;

        //TODO: Check is this is correct
        if (shearResistance1 < shearResistance2) {
            return shearResistance2;
        } else {
            return shearResistance1;
        }
    }

    public double calculateShear_k_Factor(double effectiveDepth) {
        double kFactor = Math.sqrt(200 / effectiveDepth);

        if (kFactor > 2) {
            return 2.0;
        } else {
            return kFactor;
        }
    }

    public double calculateShear_Ni_Min(double kFactor, double concreteStrengthChar) {
        double ni_Min = 0.035 * Math.sqrt(Math.pow(kFactor, 3)) * Math.sqrt(concreteStrengthChar);
        return ni_Min;
    }

    public double calculateCompressiveStressFromAxialForce(double axialForce, double sectionCrossArea, double concreteDesignCompressiveStrength) {
        double compressiveStress = (Constants.KILONEWTON_TO_NEWTON * axialForce) / (sectionCrossArea);

        if (compressiveStress > 0.2 * concreteDesignCompressiveStrength) {
            return 0.2 * concreteDesignCompressiveStrength;
        } else {
            return compressiveStress;
        }
    }

    public double calculateStirrupsSpacing(double shearForce, double thetaAngle, double steelStrengthCalc, double effectiveDepth, double stirrupsCrossSectionArea) {
        double coTanTheta = 1.0 / Math.tan(thetaAngle);
        double stirrupsSpan = (stirrupsCrossSectionArea * steelStrengthCalc * 0.9 * effectiveDepth * coTanTheta) / (shearForce * Constants.KILONEWTON_TO_NEWTON);
        return stirrupsSpan;
    }

    //OTHER HELPERS
    private double calculateDzeta_ef(double mi) {
        double firstStep = 2.0 * mi;
        System.out.println("firstStep: " + firstStep);
        double secondStep = 1.0 - firstStep;
        System.out.println("secondStep: " + secondStep);
        double thirdStep = Math.sqrt(secondStep);
        System.out.println("thirdStep: " + thirdStep);
        double dzeta_ef = 1.0 - thirdStep;
        System.out.println("dzeta_ef: " + dzeta_ef);

        return dzeta_ef;
    }

    public double calculateReinforcementRatio(double reinforcementCrossSectionalArea, double concreteCrossSectionalArea) {
        double reinforcementRatio = reinforcementCrossSectionalArea / concreteCrossSectionalArea;

        if (reinforcementRatio > 0.02) {
            return 0.02;
        } else {
            return reinforcementRatio;
        }
    }

    public double calculateCrossSectionAreaOfStirrups(int stirrupsDiameter, int numberOfStirrupsArm) {
        return (Math.PI * Math.pow(stirrupsDiameter, 2) * numberOfStirrupsArm) / (4);
    }

    public double[] swapValuesInArray(double[] arrayToSwap) {
        double[] swappedArray = {arrayToSwap[1], arrayToSwap[0]};
        return swappedArray;
    }

    //TODO: Refactor naming of variables + check if it is actually working
    public int roundUpToNearestSelectedValue(double numberToRound, int accuracy) {
        return (int) (Math.ceil(numberToRound / accuracy) * accuracy);
    }
}
