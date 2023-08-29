package com.example.ScadaWebReport.services;

import com.example.ScadaWebReport.domain.Taglog;

public class TagLogWithName extends Taglog {

    private String tagName;
    private String tagText;
    private String tagLevel;
    private String tagStatus;
    private String tagReason;
    private String tagCamera;

    public TagLogWithName(Taglog taglog, String tagName, String tagText ) {
        super(taglog.getTaglog_id(), taglog.getTag_id(), taglog.getData_value(), (int) taglog.getLogtime(),
                taglog.getLogdate(), taglog.getTimesource(), taglog.getQualifier());
        this.tagName = tagName;
        this.tagText = tagText;
        
    }
    
    public TagLogWithName(Taglog taglog, String tagName, String tagText, String tagLevel, String tagStatus, String tagReason, String tagCamera) {
        super(taglog.getTaglog_id(), taglog.getTag_id(), taglog.getData_value(), (int) taglog.getLogtime(),
                taglog.getLogdate(), taglog.getTimesource(), taglog.getQualifier());
        this.tagName = tagName;
        this.tagText = tagText;
        this.tagLevel = tagLevel;
        this.tagStatus = tagStatus;
        this.tagReason = tagReason;
        this.tagCamera = tagCamera;
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


}

