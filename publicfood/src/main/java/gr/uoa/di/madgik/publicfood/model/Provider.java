package gr.uoa.di.madgik.publicfood.model;

public enum Provider {
    None(0), Google(1), Facebook(2), LinkedIn(3);

    private final int id;

    Provider(int id) {
        this.id = id;
    }

    public static Provider valueOf(int x) {
        switch (x) {
            case 1:
                return Google;
            case 2:
                return Facebook;
            case 3:
                return LinkedIn;
        }
        return None;
    }

    public String toString() {
        switch (this) {
            case Google:
                return "Google";
            case Facebook:
                return "Facebook";
            case LinkedIn:
                return "LinkedIn";
        }

        return "";
    }

    public int getValue() {
        return id;
    }
}