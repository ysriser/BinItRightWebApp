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
    @Transactional
    public List<CheckIn> getAllCheckIns() {
        return checkInRepository.findAllWithDetails();
    }

	@Override
	public CheckIn processCheckIn(CheckInDataReq data) throws IOException{
		// Use configured upload directory
	    Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

	    if (!Files.exists(uploadPath)) {
	        Files.createDirectories(uploadPath);
	    }
		
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
        }
		checkIn.setUser(user);
		checkIn.setDropOffLocation(location);
		checkIn.setWasteCategories(category);
		checkIn.setDuration(data.getDuration());
		checkIn.setQuantity(data.getQuantity());
		checkIn.setCheckInTime(data.getCheckInTime());				
		checkIn.setStatus(CheckIn.Status.PROCESSING);
        checkIn.setFileName(data.getVideoKey());
		
		return checkInRepository.save(checkIn); 	
		
	}

	@Override
	public List<CheckIn> getPendingCheckIns() {
		return checkInRepository.findByStatusWithDetails(CheckIn.Status.PROCESSING);
	}

}
