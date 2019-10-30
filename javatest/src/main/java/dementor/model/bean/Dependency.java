package dementor.model.bean;

/**
 * Created by shisong on 2019/10/17
 */
public class Dependency {
    private String id;
    private String number;
    private String revision;
    private String ref;
    private boolean isCurrentPatchSet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public boolean isCurrentPatchSet() {
        return isCurrentPatchSet;
    }

    public void setCurrentPatchSet(boolean currentPatchSet) {
        isCurrentPatchSet = currentPatchSet;
    }
}
