package gr.uoa.di.madgik.publicfood.facades;

import org.json.JSONException;

import java.util.ArrayList;

public abstract class CollectionFacade<E> extends ArrayList<E> {

    public abstract Object Serialize();
    public abstract Object Deserialize(Object payload) throws JSONException;

}
