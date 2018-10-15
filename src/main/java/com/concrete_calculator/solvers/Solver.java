package com.concrete_calculator.solvers;

import com.concrete_calculator.models.CalculationModel;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.materials.Steel;


public class Solver {

    public double[] calculateReinforcement(CalculationModel bendingCalculationModel) {

        double height = bendingCalculationModel.getElementGeometry().getSection().getHeight();
        double width = bendingCalculationModel.getElementGeometry().getSection().getWidth();

        double bendingMoment = bendingCalculationModel.getForcesSet().getBendingMoment();

        double bottomReinforcementDiameter = bendingCalculationModel.getReinforcementProperties().getBottomReinforcement().getBarDiameter();
        double topReinforcementDiameter = bendingCalculationModel.getReinforcementProperties().getTopReinforcement().getBarDiameter();

        Steel bottomReinforcementSteelType = bendingCalculationModel.getReinforcementProperties().getBottomReinforcement().getSteelType();
        Steel topReinforcementSteelType = bendingCalculationModel.getReinforcementProperties().getTopReinforcement().getSteelType();

        double bottomReinforcementCoverage = bendingCalculationModel.getReinforcementProperties().getBottomCoverage();
        double topReinforcementCoverage = bendingCalculationModel.getReinforcementProperties().getTopCoverage();

        Concrete concreteClassOfElement = bendingCalculationModel.getConcreteType();

        //TEST
        System.out.println("M_Ed: " + bendingMoment);
        System.out.println("h: " + height);
        System.out.println("b: " + width);

        double bottomReinforcementEffectiveDepth = height - bottomReinforcementCoverage - 0.5 * bottomReinforcementDiameter;

        //TEST
        System.out.println("d: " + bottomReinforcementEffectiveDepth);

        double concreteCharCompressiveStrength = concreteClassOfElement.getF_ck();
        double concreteDesignCompressiveStrength = concreteCharCompressiveStrength / Concrete.CONCRETE_PARTIAL_FACTOR;

        //TEST
        System.out.println(concreteCharCompressiveStrength);
        System.out.println(concreteDesignCompressiveStrength);

        double bottomReinforcementSteelStrengthChar = bottomReinforcementSteelType.getF_yk();
        double bottomReinforcementSteelStrengthCalc = bottomReinforcementSteelStrengthChar / Steel.STEEL_PARTIAL_FACTOR;

        double eta = concreteClassOfElement.getEta();

        //TEST
        System.out.println("Eta: " + eta);

        double dzeta_ef_lim = concreteClassOfElement.getDzeta_ef_lim();

        //TEST
        System.out.println("Dzeta_ef_lim: " + dzeta_ef_lim);

        double mi = (bendingMoment) / (width * bottomReinforcementEffectiveDepth * bottomReinforcementEffectiveDepth * eta * concreteDesignCompressiveStrength * 1000);

        //TEST
        System.out.println("Mi: " + mi);

        double dzeta_ef = 1.0 - Math.sqrt(1.0 - 2.0 * mi);

        //TEST
        System.out.println("Dzeta_ef: " + dzeta_ef);


        if (dzeta_ef <= dzeta_ef_lim) {
            System.out.println("Compressed reinforcement is not needed");

            double z_c = (1 - 0.5 * dzeta_ef) * bottomReinforcementEffectiveDepth;

            //TEST
            System.out.println("z_c: " + z_c);

            double reinforcement_As_1 = (bendingMoment * 10000) / (z_c * bottomReinforcementSteelStrengthCalc * 1000);
            double reinforcement_As_2 = 0;

            double[] reinforcement = {reinforcement_As_1, reinforcement_As_2};

            return reinforcement;
        } else {
            System.out.println("Compressed reinforcement is needed");

            //  TODO: maksymalna nosnosc przekroju pojedynczo zbrojonego
            double M_rd_pz = dzeta_ef_lim * (1 - 0.5 * dzeta_ef_lim) * width * bottomReinforcementEffectiveDepth * bottomReinforcementEffectiveDepth * eta * concreteDesignCompressiveStrength;

            //TEST
            System.out.println(M_rd_pz);

            //  TODO: Add concrete coverage + steel strength
            double reinforcement_As_2 = (bendingMoment - M_rd_pz) / ((bottomReinforcementEffectiveDepth - 1) * 1);

            //  TODO: Add steel strength
            double reinforcement_As_1 = ((M_rd_pz) / ((1 - 0.5 * dzeta_ef_lim) * bottomReinforcementEffectiveDepth * 1)) + reinforcement_As_2;

            double[] reinforcement = {reinforcement_As_1, reinforcement_As_2};

            return reinforcement;
        }


    }


}
