package dementor.model;

import dementor.model.bean.Account;
import dementor.model.bean.Change;
import dementor.model.bean.ChangeKey;
import dementor.model.bean.PatchSet;

/**
 * Created by shisong on 2019/10/17
 */
public class GerritPatchSetCreated extends GerritBase {
    private Account uploader;
    private PatchSet patchSet;
    private Change change;
    private String project;
    private String refName;
    private ChangeKey changeKey;

    public Account getUploader() {
        return uploader;
    }

    public void setUploader(Account uploader) {
        this.uploader = uploader;
    }

    public PatchSet getPatchSet() {
        return patchSet;
    }

    public void setPatchSet(PatchSet patchSet) {
        this.patchSet = patchSet;
    }

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public ChangeKey getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(ChangeKey changeKey) {
        this.changeKey = changeKey;
    }

}
