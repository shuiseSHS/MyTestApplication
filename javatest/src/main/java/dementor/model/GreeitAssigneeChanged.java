package dementor.model;

import dementor.model.bean.Account;
import dementor.model.bean.Change;

/**
 * Created by shisong on 2019/10/17
 */
public class GreeitAssigneeChanged extends GerritBase {
    private Change change;
    private Account changer;
    private String oldAssignee;

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public Account getChanger() {
        return changer;
    }

    public void setChanger(Account changer) {
        this.changer = changer;
    }

    public String getOldAssignee() {
        return oldAssignee;
    }

    public void setOldAssignee(String oldAssignee) {
        this.oldAssignee = oldAssignee;
    }
}
