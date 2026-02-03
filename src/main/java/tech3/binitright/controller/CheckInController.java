package tech3.binitright.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CheckInDataResponse> submitRecycleCheckIn(
            @RequestBody CheckInDataReq data) throws IOException{
        System.out.println("Inside checkIn controller:  "+data.getUserId());
		
        CheckIn saved = checkInService.processCheckIn(data);

        CheckInDataResponse res = new CheckInDataResponse(
                saved.getCheckInId(),
                "SUCCESS",
                "Check-in submitted successfully and pending validation"
        );
		return ResponseEntity.ok(res);

	}
}
