package com.concrete_calculator;

public class ForcesSet {

    double bendingMoment;
    double sheerForce;
    double axialForce;
    double torqMoment;

    public double getBendingMoment() {
        return bendingMoment;
    }

    public double getSheerForce() {
        return sheerForce;
    }

    public double getAxialForce() {
        return axialForce;
    }

    public double getTorqMoment() {
        return torqMoment;
    }

    public void setBendingMoment(double bendingMoment) {
        this.bendingMoment = bendingMoment;
    }

    public void setSheerForce(double sheerForce) {
        this.sheerForce = sheerForce;
    }

    public void setAxialForce(double axialForce) {
        this.axialForce = axialForce;
    }

    public void setTorqMoment(double torqMoment) {
        this.torqMoment = torqMoment;
    }
}
