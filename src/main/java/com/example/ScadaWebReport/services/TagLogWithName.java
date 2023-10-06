package com.example.ScadaWebReport.services;

import org.springframework.lang.Nullable;

import com.example.ScadaWebReport.Entity.Taglog.Taglog;

public class TagLogWithName extends Taglog {

    private String tagName;
    private String tagText;
    private String tagLevel;
    private String tagLevelId;
    private String tagTotalId;
    private String tagStatus;
    private String tagReason;
    private String tagCamera;
    private String tagRegion;

    public TagLogWithName(@Nullable Taglog taglog, String tagName, String tagText ) {
        super(taglog.getTaglog_id(), taglog.getTag_id(), taglog.getData_value(), (int) taglog.getLogtime(),
                taglog.getLogdate(), taglog.getTimesource(), taglog.getQualifier());
        this.tagName = tagName;
        this.tagText = tagText;
        
    }
    
    public TagLogWithName(Taglog taglog,
    					String tagName, String tagText,
    					String tagLevel, String tagLevelId,
    					String tagTotalId, String tagStatus,
    					String tagReason, String tagCamera,
    					String tagRegion) {
        super(taglog.getTaglog_id(), taglog.getTag_id(), taglog.getData_value(), (int) taglog.getLogtime(),
                taglog.getLogdate(), taglog.getTimesource(), taglog.getQualifier());
        this.tagName = tagName;
        this.tagText = tagText;
        this.tagLevel = tagLevel;
        this.tagLevelId = tagLevelId;
        this.tagTotalId = tagTotalId;
        this.tagStatus = tagStatus;
        this.tagReason = tagReason;
        this.tagCamera = tagCamera;
        this.tagRegion = tagRegion;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	public String getTagLevel() {
		return tagLevel;
	}

	public void setTagLevel(String tagLevel) {
		this.tagLevel = tagLevel;
	}

	public String getTagStatus() {
		return tagStatus;
	}

	public void setTagStatus(String tagStatus) {
		this.tagStatus = tagStatus;
	}

	public String getTagReason() {
		return tagReason;
	}

	public void setTagReason(String tagReason) {
		this.tagReason = tagReason;
	}

	public String getTagCamera() {
		return tagCamera;
	}

	public void setTagCamera(String tagCamera) {
		this.tagCamera = tagCamera;
	}

	public String getTagLevelId() {
		return tagLevelId;
	}

	public void setTagLevelId(String tagLevelId) {
		this.tagLevelId = tagLevelId;
	}

	public String getTagTotalId() {
		return tagTotalId;
	}

	public void setTagTotalId(String tagTotalId) {
		this.tagTotalId = tagTotalId;
	}

	public String getTagRegion() {
		return tagRegion;
	}

	public void setTagRegion(String tagRegion) {
		this.tagRegion = tagRegion;
	}


}

