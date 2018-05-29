package gr.uoa.di.madgik.publicfood.model.enums;

public enum BMIClasses {

    Undefined(-1), Underweight(0), Healthy(1), Overweight(2), Obese(3);

    private final int id;

    BMIClasses(int id) {
        this.id = id;
    }

    public static BMIClasses valueOf(int x) {
        switch (x) {
            case -1:
                return Undefined;
            case 0:
                return Underweight;
            case 1:
                return Healthy;
            case 2:
                return Overweight;
            case 3:
                return Obese;

        }
        return null;
    }

    public int getValue() {
        return id;
    }
}
