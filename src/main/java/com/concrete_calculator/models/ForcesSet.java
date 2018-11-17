package com.concrete_calculator.models;

public class ForcesSet {

    double bendingMoment;
    double shearForce;
    double axialForce;
    double torqMoment;

    public double getBendingMoment() {
        return bendingMoment;
    }

    public double getShearForce() {
        return shearForce;
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

    public void setShearForce(double shearForce) {
        this.shearForce = shearForce;
    }

    public void setAxialForce(double axialForce) {
        this.axialForce = axialForce;
    }

    public void setTorqMoment(double torqMoment) {
        this.torqMoment = torqMoment;
    }
}
