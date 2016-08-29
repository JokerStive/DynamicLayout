package com.lilun.passionlife.cloudplatform.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class OrganizationInformation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * slug : string
     * title : string
     * visibility : public
     * replyable : true
     * isCat : true
     * isDraft : true
     * context : string
     * id : 0
     * organizationId : string
     * parentId : 0
     * createdAt : $now
     * updatedAt : $now
     * picture : string
     * pictures : string
     */


    private List<InformationReview> reviews;
    private String slug;
    private String title;
    private String visibility;
    private boolean replyable;
    private boolean isCat;
    private boolean isDraft;
    private String context;
    private Object id;
    private String organizationId;
    private int parentId;
    private String createdAt;
    private String updatedAt;
    private String picture;
    private String pictures;
    private String creatorId;
    private String updatorId;

    public String getCreatorId() {
        return creatorId;
    }

    public OrganizationInformation setCreatorId(String creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public String getUpdatorId() {
        return updatorId;
    }

    public OrganizationInformation setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
        return this;
    }

    public List<InformationReview> getReviews() {
        return reviews;
    }

    public OrganizationInformation setReviews(List<InformationReview> reviews) {
        this.reviews = reviews;
        return this;
    }



    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

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

    public boolean isReplyable() {
        return replyable;
    }

    public void setReplyable(boolean replyable) {
        this.replyable = replyable;
    }

    public boolean isIsCat() {
        return isCat;
    }

    public void setIsCat(boolean isCat) {
        this.isCat = isCat;
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

    public Object getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }
}
