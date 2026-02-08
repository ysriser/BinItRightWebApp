package tech3.binitright.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedbackUid")
    private Long feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catUid", nullable = false)
    private WasteCategories wasteCategory;

    @Column(name = "imageUurl")
    private String imageUrl;

    @Column(name = "feedbackUcontent")
    private String feedbackContent;
    
    
    public Feedback() {}
    public Feedback(Long feedbackId, User user, WasteCategories wasteCategory, String imageUrl,
			String feedbackContent) {
		
		this.feedbackId = feedbackId;
		this.user = user;
		this.wasteCategory = wasteCategory;
		this.imageUrl = imageUrl;
		this.feedbackContent = feedbackContent;
	}
	public Long getFeedbackId() {
        return feedbackId;
    }
    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public WasteCategories getWasteCategories() {
        return wasteCategory;
    }
    public void setWasteCategories(WasteCategories wasteCategory) {
        this.wasteCategory = wasteCategory;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }
    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
	
}