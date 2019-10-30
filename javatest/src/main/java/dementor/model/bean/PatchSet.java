package dementor.model.bean;

import java.util.List;

/**
 * Created by shisong on 2019/10/17
 */
public class PatchSet {

    private int number;
    private String revision;
    private List<String> parents;
    private String ref;
    private Account uploader;
    private Account author;

    /* Kind of change uploaded.

    REWORK
    Nontrivial content changes.

    TRIVIAL_REBASE
    Conflict-free merge between the new parent and the prior patch set.

    MERGE_FIRST_PARENT_UPDATE
    Conflict-free change of first (left) parent of a merge commit.

    NO_CODE_CHANGE
    No code changed; same tree and same parent tree.

    NO_CHANGE
    No changes; same commit message, same tree and same parent tree.*/
    private String kind;

    private int createdOn;
    private int sizeInsertions;
    private int sizeDeletions;
    private List<PatchsetComment> comments;
    private List<String> files;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
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

    public Account getUploader() {
        return uploader;
    }

    public void setUploader(Account uploader) {
        this.uploader = uploader;
    }

    public int getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(int createdOn) {
        this.createdOn = createdOn;
    }

    public Account getAuthor() {
        return author;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getSizeInsertions() {
        return sizeInsertions;
    }

    public void setSizeInsertions(int sizeInsertions) {
        this.sizeInsertions = sizeInsertions;
    }

    public int getSizeDeletions() {
        return sizeDeletions;
    }

    public void setSizeDeletions(int sizeDeletions) {
        this.sizeDeletions = sizeDeletions;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public List<PatchsetComment> getComments() {
        return comments;
    }

    public void setComments(List<PatchsetComment> comments) {
        this.comments = comments;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
