package gr.uoa.di.madgik.publicfood.model.enums;

public enum Gender {
    Male(1), Female(2);

    private final int id;

    Gender(int id) {
        this.id = id;
    }

    public static Gender valueOf(int x) {
        switch (x) {
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
