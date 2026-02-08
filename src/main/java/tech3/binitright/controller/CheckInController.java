package tech3.binitright.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.User;
import tech3.binitright.request.CheckInDataReq;
import tech3.binitright.response.CheckInDataResponse;
import tech3.binitright.service.CheckInImplementation;
import tech3.binitright.service.UserImplementation;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {

	@Autowired
	private CheckInInterface checkInService;
	
	public void setcheckInService(CheckInImplementation checkInserviceImp) {
		this.checkInService = checkInserviceImp;
	}

    @Autowired
    private UserInterface userService;

    @Autowired
    public void setUserService(UserImplementation userImplementation) {
        this.userService = userImplementation;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CheckInDataResponse> submitRecycleCheckIn(
            @RequestBody CheckInDataReq data, Authentication authentication) throws IOException{
        Long userId;
        try {
            userId = Long.valueOf(authentication.getName());
        } catch (NumberFormatException e) {
            // Fallback logic if token has username instead of ID
            User u = userService.findByUsername(authentication.getName()).get(0);
            userId = u.getId();
        }
		
        CheckIn saved = checkInService.processCheckIn(data, userId);

        String msg = "";

        if(data.getQuantity()>10) msg = "Check-in submitted successfully and pending validation";
        else msg = "Check-in submitted successfully";

        CheckInDataResponse res = new CheckInDataResponse(
                saved.getCheckInId(),
                "SUCCESS",
                msg
        );
		return ResponseEntity.ok(res);

	}
}
