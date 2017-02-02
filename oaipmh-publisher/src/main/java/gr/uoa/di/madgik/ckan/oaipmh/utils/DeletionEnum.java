package gr.uoa.di.madgik.ckan.oaipmh.utils;

/**
 *
 * @author  
 *
 */
public enum DeletionEnum {
    NO("no"), PERSISTENT("persistent"), TRANSIENT("transient");
    private final String name;

    private DeletionEnum(String name) {
        this.name = name;
    }

    /**
     * get the {@link DeletionEnum} value as string
     */
    @Override
    public String toString() {
        return name;
    }
}
