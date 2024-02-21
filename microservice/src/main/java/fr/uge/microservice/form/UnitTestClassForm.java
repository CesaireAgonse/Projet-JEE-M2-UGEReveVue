package fr.uge.microservice.form;

import javax.validation.constraints.NotNull;

public class UnitTestClassForm {

    @NotNull
    private byte[] javaCode;

    @NotNull
    private byte[] unitCode;

    public byte[] getJavaCode() {
        return javaCode;
    }

    public void setJavaCode(byte[] javaCode) {
        this.javaCode = javaCode;
    }

    public byte[] getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(byte[] unitCode) {
        this.unitCode = unitCode;
    }
}
