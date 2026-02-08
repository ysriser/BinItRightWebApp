package tech3.binitright.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "news")
public final class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsUid")
    private Long newsId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "imageUurl")
    private String imageUrl;

    public enum Status {
        Completed, Upcoming
    }

    @Column(name = "publishedUdate")
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public News() {
    }

    // 修复了第 55 行过长的问题
    public News(final Long newsId, final String name, final String description,
                final String imageUrl, final LocalDateTime publishedDate) {
        this.newsId = newsId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.publishedDate = publishedDate;
    }

    public News(final Long newsId, final String name, final String description, 
                final String imageUrl, final Status status) {
        super();
        this.newsId = newsId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(final Long newsId) {
        this.newsId = newsId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(final LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }
}