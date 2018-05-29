package gr.uoa.di.madgik.publicfood.facades;

import gr.uoa.di.madgik.publicfood.model.ChildrenEntity;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ChildCollectionFacade extends CollectionFacade {

    @Override
    public Object Serialize() throws JSONException {
        return null;
    }

    @Override
    public List<ChildrenEntity> Deserialize(Object payload) throws JSONException {

        JSONArray array = (JSONArray) payload;
        List<ChildrenEntity> childrenEntities = new ArrayList<ChildrenEntity>();

        for (int i = 0; i < array.length(); i++) {
            childrenEntities.add(new ChildrenEntity().Deserialize(array.getJSONObject(i)));
        }

        return childrenEntities;
    }
}
