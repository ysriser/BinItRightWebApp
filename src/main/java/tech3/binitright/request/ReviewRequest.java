package tech3.binitright.request;

public final class ReviewRequest {
    private String status;   // APPROVED or REJECTED
    private String remarks;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "AdminValidationRequest [status=" + status + ", remarks=" + remarks + "]";
    }
}