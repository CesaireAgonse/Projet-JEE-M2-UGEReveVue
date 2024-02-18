package fr.uge.revevue.form;

public class UnitTestClassForm {

    private byte[] javaCode;

    private byte[] unitCode;

    public UnitTestClassForm(byte[] javaCode, byte[] unitCode) {
        this.javaCode = javaCode;
        this.unitCode = unitCode;
    }

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
