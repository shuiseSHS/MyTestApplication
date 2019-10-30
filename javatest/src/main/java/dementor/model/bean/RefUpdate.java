package dementor.model.bean;

/**
 * Created by shisong on 2019/10/17
 */
public class RefUpdate {
    private String oldRev;
    private String newRev;
    private String refName;
    private String project;

    public String getOldRev() {
        return oldRev;
    }

    public void setOldRev(String oldRev) {
        this.oldRev = oldRev;
    }

    public String getNewRev() {
        return newRev;
    }

    public void setNewRev(String newRev) {
        this.newRev = newRev;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
