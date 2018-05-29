package gr.uoa.di.madgik.publicfood.model.enums;

public enum ChartsResponse  {

    Total(0), Male(1), Female(2);

    private final int id;

    ChartsResponse(int id) {
        this.id = id;
    }

    public static ChartsResponse valueOf(int x) {
        switch (x) {
            case 0:
                return Total;
            case 1:
                return Male;
            case 2:
                return Female;
        }
        return null;
    }

    public int getValue() {
        return id;
    }
}
