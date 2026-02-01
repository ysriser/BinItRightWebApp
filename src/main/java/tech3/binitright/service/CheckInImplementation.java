package tech3.binitright.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
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

	    @Value("${app.upload.dir}")
	    private String uploadDir;

	@Override
	public CheckIn processCheckIn(MultipartFile video, CheckInDataReq data) throws IOException{
		// Use configured upload directory
	    Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

	    if (!Files.exists(uploadPath)) {
	        Files.createDirectories(uploadPath);
	    }
	    
		// Save File
		Path target = uploadPath.resolve(video.getOriginalFilename());
		Files.copy(video.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
		
		
		//Save request to DB
		CheckIn checkIn = new CheckIn();
		User user = userRepository.findById(data.getUserId())
		        .orElseThrow(() -> new RuntimeException("User not found"));
		    
		DropOffLocation location = locationRepository.findById(data.getBinId())
		            .orElseThrow(() -> new RuntimeException("Location not found"));
		        
		WasteCategories waste = wasteCatRepository.findByName(data.getWasteCategory())
		                .orElseThrow(() -> new RuntimeException("Item not found"));
		            
		checkIn.setUser(user);
		checkIn.setDropOffLocation(location);
		checkIn.setWasteCategories(waste);
		checkIn.setDuration(data.getDuration());
		checkIn.setQuantity(data.getQuantity());
		checkIn.setCheckInTime(data.getCheckInTime());				
		checkIn.setStatus(CheckIn.Status.PROCESSING);
		
		return checkInRepository.save(checkIn); 	
		
	}

	@Override
	public List<CheckIn> getPendingCheckIns() {
		return checkInRepository.findByStatusWithDetails(CheckIn.Status.PROCESSING);
	}

	@Override
	public CheckInDataResponse validateCheckIn(Long checkInId, int quantity) {
		CheckIn checkIn = checkInRepository.findById(checkInId).get(); 

        if (checkIn == null) {
            throw new EntityNotFoundException("CheckIn not found with id: " + checkInId);
        }
        
        boolean valid = isDurationValid(checkIn);
        
        if(quantity>10) {
        	checkIn.setStatus(CheckIn.Status.PROCESSING);
            checkInRepository.save(checkIn);

            return new CheckInDataResponse(
                    checkInId,
                    "PENDING",
                    "Check-in quantity too high and validation pending"
            );
        }

        if (valid) {
            checkIn.setStatus(CheckIn.Status.APPROVED);
            checkInRepository.save(checkIn);

            return new CheckInDataResponse(
                    checkInId,
                    "APPROVED",
                    "Check-in validated and approved successfully"
            );
        } else {
            checkIn.setStatus(CheckIn.Status.DENIED);
            checkInRepository.save(checkIn);

            return new CheckInDataResponse(
                    checkInId,
                    "REJECTED",
                    "Check-in validation failed"
            );
        }
    }

	private boolean isDurationValid(CheckIn checkIn) {
        return checkIn.getDuration() >= MIN_DURATION_SECONDS;
    }
	

}
