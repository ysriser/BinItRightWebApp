package tech3.binitright.response;

public final class RedeemResponse {
    private int newTotalPoints;
    private String message;

    public RedeemResponse() {}
    public RedeemResponse(final int newTotalPoints, final String message) {
        this.newTotalPoints = newTotalPoints;
        this.message = message;
    }

    public int getNewTotalPoints() { return newTotalPoints; }
    public String getMessage() { return message; }
}
