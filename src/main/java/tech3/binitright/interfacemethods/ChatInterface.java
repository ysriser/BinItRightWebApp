package tech3.binitright.interfacemethods;

public interface ChatInterface {

    public String askRecyclingAssistant(String userMessage);
    public String generateProgressSummary(
            int pointBalance,
            double carbonEmissionSaved,
            int currentRank,
            int totalRecycledItems
    );


}
