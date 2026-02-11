package techthree.binitright.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techthree.binitright.interfacemethods.ChatInterface;
import techthree.binitright.interfacemethods.LlmClient;

@Service
@Transactional
public class ChatImplementation implements ChatInterface{

    private final LlmClient llmClient;

    @Value("${openai.model}")
    private String model;

    public ChatImplementation(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    public String askRecyclingAssistant(String userMessage) {
        try {
            String systemPrompt = """
You are Bin-It-Right, an AI recycling assistant for Singapore that helps users
identify waste items from images and dispose of them correctly.

Core responsibilities:
- Use the detected item name or description from an image scan to determine
  the correct disposal method in Singapore.
- Provide clear, practical, step-by-step disposal guidance.
- Mention the correct bin type when relevant (blue recycling bin, general waste,
  e-waste collection point, hazardous waste drop-off, etc.).
- Keep answers concise and suitable for mobile app display (usually under 80 words).

Accuracy rules:
- If the detected item is unclear or confidence is low, ask ONE short clarifying question.
- Never guess disposal rules.
- If disposal depends on item condition (clean vs contaminated, battery inside, etc.),
  briefly explain the condition check.

Safety and responsibility:
- If the item may be hazardous (battery, chemical, sharp object, medical waste),
  clearly warn the user and recommend official NEA guidance or proper collection points.
- Do not provide unsafe, illegal, or misleading instructions.

Tone and style:
- Friendly, encouraging, and environmentally positive.
- Use simple everyday language.
- Prefer short bullet-style or step-by-step guidance instead of long paragraphs.

Always focus on helping the user dispose of the detected item correctly in Singapore.
""";

            return llmClient.chat(model, systemPrompt, userMessage);

        } catch (Exception ex) {
            return "Recycling assistant is temporarily unavailable. Please try again later.";
        }
    }

    public String generateProgressSummary(
            int pointBalance,
            double carbonEmissionSaved,
            int currentRank,
            int totalRecycledItems
    ) {
        try {
            String systemPrompt = """
You are Bin-It-Right AI, a sustainability progress assistant in Singapore.

Your task:
- Generate a SHORT motivational dashboard summary (max 35 words).
- Use ONLY the provided user statistics.
- Encourage continued recycling and environmental impact.
- Mention rank or milestone feeling if meaningful.
- Do NOT invent numbers or achievements.
- Tone: friendly, positive, eco-focused, mobile-app style.
""";

            String userPrompt = String.format("""
User stats:
Point balance: %d
Carbon emission saved: %.1f kg
Current rank: %s
Total recycled items: %d

Write the dashboard AI progress summary.
""",
                    pointBalance,
                    carbonEmissionSaved,
                    currentRank,
                    totalRecycledItems
            );

            return llmClient.chat(model, systemPrompt, userPrompt);

        } catch (Exception ex) {
            // graceful fallback if OpenAI fails
            return "You're making a real environmental impact 🌱 Keep recycling to climb higher and save more CO₂!";
        }
    }

}