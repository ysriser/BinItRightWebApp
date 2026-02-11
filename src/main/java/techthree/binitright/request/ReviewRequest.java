package techthree.binitright.request;


public class ReviewRequest {
	 private String status;   // APPROVED or REJECTED
	 private String remarks;
	 
	 public String getStatus() {
		 return status;
	 }
	 public void setStatus(String status) {
		 this.status = status;
	 }
	 public String getRemarks() {
		 return remarks;
	 }
	 public void setRemarks(String remarks) {
		 this.remarks = remarks;
	 }
	 @Override
	 public String toString() {
		return "AdminValidationRequest [status=" + status + ", remarks=" + remarks + "]";
	 }
	 
}
