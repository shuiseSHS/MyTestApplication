package dementor.model.bean;

/**
 * Created by shisong on 2019/10/17
 */

public class Approvals {
    private String type;
    private String description;
    private String value;
    private String oldValue;
    private long grantedOn;
    private Account by;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public long getGrantedOn() {
        return grantedOn;
    }

    public void setGrantedOn(long grantedOn) {
        this.grantedOn = grantedOn;
    }

    public Account getBy() {
        return by;
    }

    public void setBy(Account by) {
        this.by = by;
    }
}