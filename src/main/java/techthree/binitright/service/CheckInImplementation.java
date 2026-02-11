package techthree.binitright.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import techthree.binitright.dto.LeaderboardDTO;
import techthree.binitright.interfacemethods.CheckInInterface;
import techthree.binitright.model.CheckIn;
import techthree.binitright.model.DropOffLocation;
import techthree.binitright.model.User;
import techthree.binitright.model.WasteCategories;
import techthree.binitright.repository.CheckInRepository;
import techthree.binitright.repository.LocationRepository;
import techthree.binitright.repository.UserRepository;
import techthree.binitright.repository.WasteCategoryRepository;
import techthree.binitright.request.CheckInDataReq;

@Service
@Transactional
public class CheckInImplementation implements CheckInInterface {

    Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
	private CheckInRepository checkInRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private WasteCategoryRepository wasteCatRepository;

	@Autowired
	private AchievementImplementation achievementImplementation;

    @Override
    @Transactional
    public List<CheckIn> getAllCheckIns() {
        return checkInRepository.findAllWithDetails();
    }

	@Override
	public CheckIn processCheckIn(final CheckInDataReq data, final Long userId) throws IOException {
		final CheckIn checkIn = new CheckIn();
		final User user = userRepository.findById(userId)
		        .orElseThrow(() -> new RuntimeException("User not found"));
		    
		final DropOffLocation location = locationRepository.findById(data.getBinId())
		            .orElseThrow(() -> new RuntimeException("Location not found"));

        final WasteCategories category = wasteCatRepository
                .findByNameIgnoreCase(data.getWasteCategory())
                .orElseThrow(() -> new RuntimeException(
                        "Waste category not found: " + data.getWasteCategory()
                ));

        if(data.getQuantity() <= 10) {
            checkIn.setRewardPoints(data.getQuantity() * 10);
            checkIn.setStatus(CheckIn.Status.APPROVED);
        } else {
            checkIn.setStatus(CheckIn.Status.PROCESSING);
        }
		checkIn.setUser(user);
		checkIn.setDropOffLocation(location);
		checkIn.setWasteCategories(category);
		checkIn.setDuration(data.getDuration());
		checkIn.setQuantity(data.getQuantity());
		checkIn.setCheckInTime(data.getCheckInTime());
        checkIn.setFileName(data.getVideoKey());
		
		final CheckIn savedCheckIn = checkInRepository.save(checkIn);

        if (checkIn.getRewardPoints() != null && checkIn.getRewardPoints() > 0) {
            final int currentBalance = (user.getPointBalance() == null) ? 0 : user.getPointBalance();
            user.setPointBalance(currentBalance + checkIn.getRewardPoints());
            userRepository.save(user);
        }

		checkAndUnlockAchievements(user, savedCheckIn);
        achievementImplementation.checkProfileAchievements(user);
		
		return savedCheckIn;
	}

	private void checkAndUnlockAchievements(final User user, final CheckIn currentCheckIn) {
		try {
			final long totalCheckIns = checkInRepository.countByUser(user);
			if (totalCheckIns >= 1) {
				achievementImplementation.unlockAchievement(user.getId(), 1L);
			}
			if (totalCheckIns >= 10) {
				achievementImplementation.unlockAchievement(user.getId(), 2L);
			}
			if (totalCheckIns >= 50) {
				achievementImplementation.unlockAchievement(user.getId(), 3L);
			}
			if (totalCheckIns >= 100) {
				achievementImplementation.unlockAchievement(user.getId(), 4L);
			}

            final LocalDateTime time = currentCheckIn.getCheckInTime();
            final int hour = time.getHour();
            if (hour >= 6 && hour < 8) {
				achievementImplementation.unlockAchievement(user.getId(), 7L);
			}
            if (hour >= 22 || hour < 4) {
				achievementImplementation.unlockAchievement(user.getId(), 8L);
			}
		} catch (final Exception e) {
			logger.info("Error unlocking achievements: " + e.getMessage());
		}
	}

	@Override
	public List<CheckIn> getPendingCheckIns() {
		return checkInRepository.findPendingWithDetails();
	}

    @Override
    public Integer getUserTotalRecycled(final Long userId) {
        return checkInRepository.getTotalRecycledByUser(userId);
    }

    @Override
    public List<LeaderboardDTO> getMonthlyLeaderboard() {
        final LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        return checkInRepository.findTopRecyclers(startOfMonth, PageRequest.of(0, 5));
    }
}