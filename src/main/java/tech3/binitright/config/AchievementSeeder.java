package tech3.binitright.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tech3.binitright.model.Achievement;
import tech3.binitright.repository.AchievementRepository;

@Configuration
public final class AchievementSeeder {

    private static final AtomicBoolean SEEDING_COMPLETE = new AtomicBoolean(false);

    @Bean
    @Profile({"test", "default", "prod"})
    public CommandLineRunner seedAchievements(final AchievementRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                SEEDING_COMPLETE.set(true);
                return;
            }

            final List<Achievement> list = List.of(
                new Achievement("First Submission", "Submit your first recycling item.",
                        "Recycle 1 item", "https://img.icons8.com/color/96/seed.png"),
                new Achievement("Recycling Master", "Complete 10 recycling submissions.",
                        "Recycle 10 times", "https://img.icons8.com/color/96/recycle-sign.png")
            );

            repo.saveAll(list);
            SEEDING_COMPLETE.set(true);
        };
    }

    public final boolean isSeedingComplete() {
        return SEEDING_COMPLETE.get();
    }
}