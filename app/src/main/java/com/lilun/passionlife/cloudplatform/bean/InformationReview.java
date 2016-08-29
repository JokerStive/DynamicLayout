package com.lilun.passionlife.cloudplatform.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/19.
 */
public class InformationReview implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * title : string
     * visibility : public
     * isDraft : true
     * context : string
     * id : 0
     * informationId : 0
     * createdAt : $now
     * updatedAt : $now
     */

    private String title;
    private String visibility;
    private boolean isDraft;
    private String context;
    private int id;
    private int informationId;
    private String createdAt;
    private String updatedAt;

    public String getCreatorId() {
        return creatorId;
    }

    public InformationReview setCreatorId(String creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public String getUpdatorId() {
        return updatorId;
    }

    public InformationReview setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
        return this;
    }

    private String creatorId;
    private String updatorId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean isIsDraft() {
        return isDraft;
    }

    public void setIsDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInformationId() {
        return informationId;
    }

    public void setInformationId(int informationId) {
        this.informationId = informationId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
