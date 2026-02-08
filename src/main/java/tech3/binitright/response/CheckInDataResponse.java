package tech3.binitright.response;

public final class CheckInDataResponse {
    private Long checkInId;
    private String responseCode;
    private String responseDesc;

    public CheckInDataResponse() {
    }

    public CheckInDataResponse(final Long checkInId, final String responseCode, final String responseDesc) {
        this.checkInId = checkInId;
        this.responseCode = responseCode;
        this.responseDesc = responseDesc;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(final String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(final String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public Long getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(final Long checkInId) {
        this.checkInId = checkInId;
    }

    @Override
    public String toString() {
        return "CheckInDataResponse [responseCode=" + responseCode + ", responseDesc=" + responseDesc + "]";
    }
}