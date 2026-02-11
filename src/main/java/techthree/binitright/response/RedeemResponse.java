package techthree.binitright.response;

public class RedeemResponse {
    private int newTotalPoints;
    private String message;

    public RedeemResponse() {}
    public RedeemResponse(int newTotalPoints, String message) {
        this.newTotalPoints = newTotalPoints;
        this.message = message;
    }

    public int getNewTotalPoints() { return newTotalPoints; }
    public String getMessage() { return message; }
}
