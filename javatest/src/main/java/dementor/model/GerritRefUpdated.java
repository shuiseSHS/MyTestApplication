package dementor.model;

import dementor.model.bean.Account;
import dementor.model.bean.RefUpdate;

/**
 * Created by shisong on 2019/10/17
 */
public class GerritRefUpdated extends GerritBase {
    private Account submitter;
    private RefUpdate refUpdate;

    public Account getSubmitter() {
        return submitter;
    }

    public void setSubmitter(Account submitter) {
        this.submitter = submitter;
    }

    public RefUpdate getRefUpdate() {
        return refUpdate;
    }

    public void setRefUpdate(RefUpdate refUpdate) {
        this.refUpdate = refUpdate;
    }

}
