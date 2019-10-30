package dementor.model.bean;

import java.util.List;

/**
 * Created by shisong on 2019/10/17
 */
public class Change {
    private String project;
    private String branch;
    private String topic;
    private String id;
    private int number;
    private String subject;
    private Account owner;
    private String url;
    private String commitMessage;
    private int createdOn;
    private String status;
    private List<Message> comments;
    private List<TrackingId> trackingIds;
    private PatchSet currentPatchSet;
    private List<Dependency> dependsOn;
    private List<Dependency> neededBy;
    private List<Account> allReviewers;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public int getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(int createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<Message> getComments() {
        return comments;
    }

    public void setComments(List<Message> comments) {
        this.comments = comments;
    }

    public List<TrackingId> getTrackingIds() {
        return trackingIds;
    }

    public void setTrackingIds(List<TrackingId> trackingIds) {
        this.trackingIds = trackingIds;
    }

    public PatchSet getCurrentPatchSet() {
        return currentPatchSet;
    }

    public void setCurrentPatchSet(PatchSet currentPatchSet) {
        this.currentPatchSet = currentPatchSet;
    }

    public List<Dependency> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(List<Dependency> dependsOn) {
        this.dependsOn = dependsOn;
    }

    public List<Dependency> getNeededBy() {
        return neededBy;
    }

    public void setNeededBy(List<Dependency> neededBy) {
        this.neededBy = neededBy;
    }

    public List<Account> getAllReviewers() {
        return allReviewers;
    }

    public void setAllReviewers(List<Account> allReviewers) {
        this.allReviewers = allReviewers;
    }
}
