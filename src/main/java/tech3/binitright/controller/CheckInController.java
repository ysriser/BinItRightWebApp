package tech3.binitright.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

	public void setcheckInService(final CheckInImplementation checkInserviceImp) {
		this.checkInService = checkInserviceImp;
	}

    @Autowired
    private UserInterface userService;

    @Autowired
    public void setUserService(final UserImplementation userImplementation) {
        this.userService = userImplementation;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CheckInDataResponse> submitRecycleCheckIn(
            @RequestBody final CheckInDataReq data, final Authentication authentication) throws IOException{
        Long userId;
        try {
            userId = Long.valueOf(authentication.getName());
        } catch (final NumberFormatException e) {
            // Fallback logic if token has username instead of ID
            final User u = userService.findByUsername(authentication.getName()).get(0);
            userId = u.getId();
        }

        final CheckIn saved = checkInService.processCheckIn(data, userId);

        String msg = "";

        if(data.getQuantity()>10) {
			msg = "Check-in submitted successfully and pending validation";
		} else {
			msg = "Check-in submitted successfully";
		}

        final CheckInDataResponse res = new CheckInDataResponse(
                saved.getCheckInId(),
                "SUCCESS",
                msg
        );
		return ResponseEntity.ok(res);

	}
}
