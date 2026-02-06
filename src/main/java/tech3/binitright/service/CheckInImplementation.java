package tech3.binitright.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.DropOffLocation;
import tech3.binitright.model.User;
import tech3.binitright.model.WasteCategories;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.repository.LocationRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.repository.WasteCategoryRepository;
import tech3.binitright.request.CheckInDataReq;
import tech3.binitright.request.RecycledItemReq;
import tech3.binitright.response.CheckInDataResponse;

@Service
@Transactional
public class CheckInImplementation implements CheckInInterface{
	
	private static final int MIN_DURATION_SECONDS = 5;
    
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
	public CheckIn processCheckIn(CheckInDataReq data) throws IOException{
		
		//Save request to DB
		CheckIn checkIn = new CheckIn();
		User user = userRepository.findById(data.getUserId())
		        .orElseThrow(() -> new RuntimeException("User not found"));
		    
		DropOffLocation location = locationRepository.findById(data.getBinId())
		            .orElseThrow(() -> new RuntimeException("Location not found"));

        WasteCategories category = wasteCatRepository
                .findByNameIgnoreCase(data.getWasteCategory())
                .orElseThrow(() -> new RuntimeException(
                        "Waste category not found: " + data.getWasteCategory()
                ));

        if(data.getQuantity()<10){
            checkIn.setRewardPoints(data.getQuantity()*10);
            checkIn.setStatus(CheckIn.Status.APPROVED);
        }
        else if(data.getQuantity()>10){
            checkIn.setStatus(CheckIn.Status.PROCESSING);
        }
		checkIn.setUser(user);
		checkIn.setDropOffLocation(location);
		checkIn.setWasteCategories(category);
		checkIn.setDuration(data.getDuration());
		checkIn.setQuantity(data.getQuantity());
		checkIn.setCheckInTime(data.getCheckInTime());
        checkIn.setFileName(data.getVideoKey());
		
		CheckIn savedCheckIn = checkInRepository.save(checkIn);

        if (checkIn.getRewardPoints() > 0) {
            user.setPointBalance(user.getPointBalance() + checkIn.getRewardPoints());
            userRepository.save(user);
        }

		checkAndUnlockAchievements(user, savedCheckIn);
        achievementImplementation.checkProfileAchievements(user);
		
		return savedCheckIn;
		
	}

	private void checkAndUnlockAchievements(User user, CheckIn currentCheckIn) {
		try {
			long totalCheckIns = checkInRepository.countByUser(user);
			
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

            LocalDateTime time = currentCheckIn.getCheckInTime();
            int hour = time.getHour();

            if (hour >= 6 && hour < 8) {
                achievementImplementation.unlockAchievement(user.getId(), 7L);
            }

            if (hour >= 22 || hour < 4) {
                achievementImplementation.unlockAchievement(user.getId(), 8L);
            }

		} catch (Exception e) {
			System.err.println("Error unlocking achievements: " + e.getMessage());
		}
	}

	@Override
	public List<CheckIn> getPendingCheckIns() {
		return checkInRepository.findByStatusWithDetails(CheckIn.Status.PROCESSING);
	}

}