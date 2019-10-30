package dementor.model.bean;

import dementor.model.GerritBase;

/**
 * Created by shisong on 2019/10/17
 */
public class TrackingId extends GerritBase {
    private String system;
    private String id;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
