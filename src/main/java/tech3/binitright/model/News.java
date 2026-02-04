package tech3.binitright.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
public class News {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "news_id")
	    private Long newsId;

	    private String name;

        @Column(columnDefinition = "TEXT")
	    private String description;

	    @Column(name = "image_url")
	    private String imageUrl;
        
	    public enum Status {
	        Completed,
	        Upcoming
	    }

        @Column(name = "published_date")
        private LocalDateTime publishedDate;


		public News(Long newsId, String name, String description, String imageUrl, LocalDateTime publishedDate) {
			
			this.newsId = newsId;
			this.name = name;
			this.description = description;
			this.imageUrl = imageUrl;
            this.publishedDate = publishedDate;
		}
	    
		@Enumerated(EnumType.STRING)
	    @Column(name = "status")
		 private Status status;
		 
		public News() {}

		public News(Long newsId, String name, String description, String imageUrl, Status status) {
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

		public void setNewsId(Long newsId) {
			this.newsId = newsId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

        public LocalDateTime getPublishedDate() {
            return publishedDate;
        }

        public void setPublishedDate(LocalDateTime publishedDate) {
            this.publishedDate = publishedDate;
        }

		
		 
	

}
