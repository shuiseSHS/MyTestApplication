package dementor.model;

import java.util.List;

import dementor.model.bean.Approvals;
import dementor.model.bean.Account;
import dementor.model.bean.Change;
import dementor.model.bean.ChangeKey;
import dementor.model.bean.PatchSet;

/**
 * Created by shisong on 2019/10/17
 */
public class GerritCommentAdded extends GerritBase {
    private Account author;
    private List<Approvals> approvals;
    private String comment;
    private PatchSet patchSet;
    private Change change;
    private String project;
    private String refName;
    private ChangeKey changeKey;

    public Account getAuthor() {
        return author;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public List<Approvals> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<Approvals> approvals) {
        this.approvals = approvals;
    }

}
