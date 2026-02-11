package tech3.binitright.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tech3.binitright.model.Achievement;
import tech3.binitright.repository.AchievementRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class AchievementSeeder {

    private static final AtomicBoolean seedingComplete = new AtomicBoolean(false);
    private static final Logger log = LoggerFactory.getLogger(AchievementSeeder.class);

    @Bean
    @Profile({"test", "default", "prod"})
    public CommandLineRunner seedAchievements(AchievementRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                seedingComplete.set(true);
                log.info("Achievements already seeded, skipping.");
                return;
            }

            List<Achievement> list = List.of(
                new Achievement("First Submission", "Submit your first recycling item.", "Recycle 1 item", "https://img.icons8.com/color/96/seed.png"),
                new Achievement("Recycling Master", "Complete 10 recycling submissions.", "Recycle 10 times", "https://img.icons8.com/color/96/recycle-sign.png"),
                new Achievement("Eco Enthusiast", "Maintain your dedication with 50 submissions.", "Recycle 50 times", "https://img.icons8.com/color/96/medal.png"),
                new Achievement("Green Legend", "A monumental 100 recycling submissions!", "Recycle 100 times", "https://img.icons8.com/color/96/trophy.png"),
                new Achievement("The Collector", "Save up your rewards points.", "Hold 5000 points", "https://img.icons8.com/color/96/hamster.png"),
                new Achievement("Rising Star", "Advance your environmental impact rank.", "Reach Rank 2", "https://img.icons8.com/color/96/upgrade.png"),
                new Achievement("Early Bird", "Complete a check-in early in the morning.", "Check-in 06:00-08:00", "https://img.icons8.com/color/96/sun.png"),
                new Achievement("Night Owl", "Contribute to recycling late at night.", "Recycle after 22:00", "https://img.icons8.com/color/96/owl.png"),
                new Achievement("Eagle Eye", "Help maintain the community's bins.", "Report 1 Issue", "https://img.icons8.com/color/96/visible.png"),
                new Achievement("First Pot of Gold", "Redeem your points for a reward.", "Redeem 1 Reward", "https://img.icons8.com/color/96/coins.png")
            );

            repo.saveAll(list);
            log.info(">>> 10 Achievements seeded successfully.");

            seedingComplete.set(true);
            log.info(">>> ALL SEEDING OPERATIONS COMPLETE.");
        };
    }

    public boolean isSeedingComplete() {
        return seedingComplete.get();
    }
}