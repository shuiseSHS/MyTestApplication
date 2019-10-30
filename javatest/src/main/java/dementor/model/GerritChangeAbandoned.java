package dementor.model;

import dementor.model.bean.Account;
import dementor.model.bean.Change;
import dementor.model.bean.PatchSet;

/**
 * Created by shisong on 2019/10/17
 */
public class GerritChangeAbandoned extends GerritBase {
    private Change change;
    private PatchSet patchSet;
    private Account abandoner;
    private String reason;

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public PatchSet getPatchSet() {
        return patchSet;
    }

    public void setPatchSet(PatchSet patchSet) {
        this.patchSet = patchSet;
    }

    public Account getAbandoner() {
        return abandoner;
    }

    public void setAbandoner(Account abandoner) {
        this.abandoner = abandoner;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
