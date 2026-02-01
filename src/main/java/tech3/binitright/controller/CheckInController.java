package tech3.binitright.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.model.CheckIn;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.request.CheckInDataReq;
import tech3.binitright.response.CheckInDataResponse;
import tech3.binitright.service.CheckInImplementation;

@Controller
@RequestMapping("/api/checkin")
public class CheckInController {

	@Autowired
	private CheckInInterface checkInService;
	
	public void setcheckInService(CheckInImplementation checkInserviceImp) {
		this.checkInService = checkInserviceImp;
	}
	
	@Autowired
	CheckInRepository checkInRepository;
	
	@Value("${app.upload.dir}")
	private String uploadDir;
	
	@PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<CheckInDataResponse> submitRecycleCheckIn(
			@RequestPart("video") MultipartFile video,
			@RequestPart("metadata") CheckInDataReq data) throws IOException {
		System.out.println("Inside checkIn controller:  "+data);
		
		if(video.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		
        CheckIn saved = checkInService.processCheckIn(video, data);
        
        CheckInDataResponse res = new CheckInDataResponse();
        
        res = checkInService.validateCheckIn(saved.getCheckInId(), saved.getQuantity());
		
		return ResponseEntity.ok(res);

	}
	
	
}
