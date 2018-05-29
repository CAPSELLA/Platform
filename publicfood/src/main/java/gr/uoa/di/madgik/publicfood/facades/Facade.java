package gr.uoa.di.madgik.publicfood.facades;

import org.json.JSONException;

public abstract class Facade {

    //region Serialization

    public abstract Object Serialize() throws JSONException;
    public abstract Object Deserialize(Object payload) throws JSONException;

    //region
}