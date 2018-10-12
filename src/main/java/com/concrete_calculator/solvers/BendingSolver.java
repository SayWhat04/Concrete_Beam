package com.concrete_calculator.solvers;


import com.concrete_calculator.geometry.RectangularSection;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.materials.Steel;

public class BendingSolver {

    public double[] calculateReinforcement(RectangularSection section, double bendingMoment) {

        System.out.println("M_Ed: " + bendingMoment);

        double height = section.getHeight();
        double width = section.getWidth();

        System.out.println("h: " + height);
        System.out.println("b: " + width);

        double effectiveDepth = section.getEffectiveDepth();

        System.out.println("d: " + effectiveDepth);

        Concrete calculatedConcrete = section.getConcreteType();
        Steel calculatedSteel = section.getSteelType();

        double concreteCharCompressiveStrength = calculatedConcrete.getF_ck();
        double concreteDesignCompressiveStrength = concreteCharCompressiveStrength / Concrete.CONCRETE_PARTIAL_FACTOR;

        System.out.println(concreteCharCompressiveStrength);
        System.out.println(concreteDesignCompressiveStrength);

        double steelStrengthChar = calculatedSteel.getF_yk();
        double steelStrengthCalc = steelStrengthChar / Steel.STEEL_PARTIAL_FACTOR;

        double eta = calculatedConcrete.getEta();
        System.out.println("Eta: " + eta);

        double dzeta_ef_lim = calculatedConcrete.getDzeta_ef_lim();
        System.out.println("Dzeta_ef_lim: " + dzeta_ef_lim);

        double mi = (bendingMoment) / (width * effectiveDepth * effectiveDepth * eta * concreteDesignCompressiveStrength * 1000);
        System.out.println("Mi: " + mi);


        double dzeta_ef = 1.0 - Math.sqrt(1.0 - 2.0 * mi);
        System.out.println("Dzeta_ef: " + dzeta_ef);


        if (dzeta_ef <= dzeta_ef_lim) {
            System.out.println("Compressed reinforcement is not needed");

            double z_c = (1 - 0.5 * dzeta_ef) * effectiveDepth;
            System.out.println("z_c: " + z_c);

            //  TODO: add steel properties, change of variables naming
            double reinforcement_As_1 = (bendingMoment * 10000) / (z_c * steelStrengthCalc * 1000);
            double reinforcement_As_2 = 0;

            double[] reinforcement = {reinforcement_As_1, reinforcement_As_2};

            return reinforcement;

        } else {
            System.out.println("Compressed reinforcement is needed");

            //  TODO: maksymalna nosnosc przekroju pojedynczo zbrojonego
            double M_rd_pz = dzeta_ef_lim * (1 - 0.5 * dzeta_ef_lim) * width * effectiveDepth * effectiveDepth * eta * concreteDesignCompressiveStrength;
            System.out.println(M_rd_pz);

            //  TODO: Add concrete coverage + steel strength
            double reinforcement_As_2 = (bendingMoment - M_rd_pz) / ((effectiveDepth - 1) * 1);

            //  TODO: Add steel strength
            double reinforcement_As_1 = ((M_rd_pz) / ((1 - 0.5 * dzeta_ef_lim) * effectiveDepth * 1)) + reinforcement_As_2;

            double[] reinforcement = {reinforcement_As_1, reinforcement_As_2};
            return reinforcement;
        }


    }


}