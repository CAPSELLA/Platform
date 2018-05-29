package gr.uoa.di.madgik.publicfood.facades;

import org.json.JSONException;
import org.json.JSONObject;

public class DatasetTopPercentage extends Facade {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object Serialize() throws JSONException {
        return null;
    }

    @Override
    public DatasetTopPercentage Deserialize(Object payload) throws JSONException {

        JSONObject jsonObject = (payload instanceof JSONObject) ? (JSONObject) payload : new JSONObject((String) payload);

        return null;
    }
}
