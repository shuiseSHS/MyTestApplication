package dementor.model;

import dementor.model.bean.Account;
import dementor.model.bean.Change;

/**
 * Created by shisong on 2019/10/17
 */
public class GerritChangeDeleted extends GerritBase {

    private Change change;
    private Account deleter;

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public Account getDeleter() {
        return deleter;
    }

    public void setDeleter(Account deleter) {
        this.deleter = deleter;
    }
}
