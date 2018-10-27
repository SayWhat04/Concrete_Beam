package com.concrete_calculator.solvers;

import com.concrete_calculator.UnitsConverter;
import com.concrete_calculator.models.CalculationModel;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.materials.Steel;

public class Solver {

    public double[] calculateReinforcementPureBending(CalculationModel bendingCalculationModel) {

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

    private double[] calculateOnlyExtendedReinforcementBending(double extendedReinforcementEffectiveDepth,
                                                               double extendedReinforcementSteelStrengthCalc,
                                                               double bendingMoment, double dzeta_ef) {
        System.out.println("Compressed reinforcement is not needed");

        //Lever arm of internal forces
        double z_c = (1 - 0.5 * dzeta_ef) * extendedReinforcementEffectiveDepth;
        //TEST
        System.out.println("z_c: " + z_c);

        //As_1: extended reinforcement; As_2: compressed reinforcement;
        double reinforcement_As_1 = (bendingMoment * UnitsConverter.KILONEWTON_TO_NEWTON * UnitsConverter.METER_TO_MILIMETER) / (z_c * extendedReinforcementSteelStrengthCalc);
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
        double M_rd_pz = dzeta_ef_lim * (1 - 0.5 * dzeta_ef_lim) * width * extendedReinforcementEffectiveDepth * extendedReinforcementEffectiveDepth * eta * concreteDesignCompressiveStrength * UnitsConverter.NEWTON_TO_KILONEWTON * UnitsConverter.MILIMETER_TO_METER;
        //TEST
        System.out.println("M_rd_pz: " + M_rd_pz);

        //As_1: extended reinforcement; As_2: compressed reinforcement
        //TODO: Add value of f_yd
        double reinforcement_As_2 = ((bendingMoment - M_rd_pz) * UnitsConverter.KILONEWTON_TO_NEWTON * UnitsConverter.METER_TO_MILIMETER) / ((extendedReinforcementEffectiveDepth - a2) * extendedReinforcementSteelStrengthCalc);
        double reinforcement_As_1 = (((M_rd_pz) / ((1 - 0.5 * dzeta_ef_lim) * extendedReinforcementEffectiveDepth * extendedReinforcementSteelStrengthCalc)) * UnitsConverter.KILONEWTON_TO_NEWTON * UnitsConverter.METER_TO_MILIMETER) + reinforcement_As_2;
        double[] reinforcement = {reinforcement_As_1, reinforcement_As_2};
        return reinforcement;
    }

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

    private double calculateMi(double width, double bendingMoment, double extendedReinforcementEffectiveDepth, double concreteDesignCompressiveStrength, double eta) {
        return (bendingMoment * UnitsConverter.KILONEWTON_TO_NEWTON * UnitsConverter.METER_TO_MILIMETER) / (width * extendedReinforcementEffectiveDepth * extendedReinforcementEffectiveDepth * eta * concreteDesignCompressiveStrength);
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

    public double calculateMaximumReinforcementPureBending(double width, double effectiveDepth) {
        double maximumReinforcementPureBending = 0.04 * width * effectiveDepth;
        return maximumReinforcementPureBending;
    }

    public double[] swapValuesInArray(double[] arrayToSwap) {
        double[] swappedArray = {arrayToSwap[1], arrayToSwap[0]};
        return swappedArray;
    }


}
