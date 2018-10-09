package com.concrete_calculator;


public enum Concrete {

    // TODO: Correction of units for concrete properties
    // TODO: Add all of concrete types
    C12_15(12, 15, 20, 1.6, 1.1, 2.0, 27, 3.5, 1.0, 0.8, 0.493),
    C16_20(16, 20, 24, 1.9, 1.3, 2.5, 29, 3.5, 1.0, 0.8, 0.493),
    C20_25(20, 25, 28, 2.2, 1.5, 2.9, 30, 3.5, 1.0, 0.8, 0.493),
    C25_30(25, 30, 33, 2.6, 1.8, 3.3, 31, 3.5, 1.0, 0.8, 0.493),
    C30_37(30, 37, 38, 2.9, 2.0, 3.8, 33, 3.5, 1.0, 0.8, 0.493),
    C35_45(35, 45, 43, 3.2, 2.2, 4.2, 34, 3.5, 1.0, 0.8, 0.493),
    C40_50(40, 50, 48, 3.5, 2.5, 4.6, 35, 3.5, 1.0, 0.8, 0.493),
    C45_55(45, 55, 53, 3.8, 2.7, 4.9, 36, 3.5, 1.0, 0.8, 0.493),
    C50_60(50, 60, 58, 4.1, 2.9, 5.3, 37, 3.5, 1.0, 0.8, 0.493);

    private double elasticModulus;
    public static final double CONCRETE_PARTIAL_FACTOR = 1.4;

    // TODO: Think if naming of concrete properties is correct
    private double f_ck;
    private double f_ck_cube;
    private double f_cm;
    private double f_ctm;
    private double f_ctk_005;
    private double f_ctk_095;
    private double e_cm;

    private double epsylon_cu3;
    private double eta;
    private double lambda;
    private double dzeta_ef_lim;


    Concrete(double f_ck, double f_ck_cube, double f_cm, double f_ctm, double f_ctk_005, double f_ctk_095, double e_cm, double epsylon_cu3, double eta, double lambda, double dzeta_ef_lim) {
        this.f_ck = f_ck;
        this.f_ck_cube = f_ck_cube;
        this.f_cm = f_cm;
        this.f_ctm = f_ctm;
        this.f_ctk_005 = f_ctk_005;
        this.f_ctk_095 = f_ctk_095;
        this.e_cm = e_cm;
        this.epsylon_cu3 = epsylon_cu3;
        this.eta = eta;
        this.lambda = lambda;
        this.dzeta_ef_lim = dzeta_ef_lim;
    }

    public double getElasticModulus() {
        return elasticModulus;
    }

    public double getF_ck() {
        return f_ck;
    }

    public double getF_ck_cube() {
        return f_ck_cube;
    }

    public double getF_cm() {
        return f_cm;
    }

    public double getF_ctm() {
        return f_ctm;
    }

    public double getF_ctk_005() {
        return f_ctk_005;
    }

    public double getF_ctk_095() {
        return f_ctk_095;
    }

    public double getE_cm() {
        return e_cm;
    }

    public double getEpsylon_cu3() {
        return epsylon_cu3;
    }

    public double getEta() {
        return eta;
    }

    public double getLambda() {
        return lambda;
    }

    public double getDzeta_ef_lim() {
        return dzeta_ef_lim;
    }
}
