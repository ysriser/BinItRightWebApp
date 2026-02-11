package techthree.binitright.controller;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import techthree.binitright.interfacemethods.CheckInInterface;
import techthree.binitright.interfacemethods.UserInterface;
import techthree.binitright.model.CheckIn;
import techthree.binitright.model.User;
import techthree.binitright.request.CheckInDataReq;
import techthree.binitright.response.CheckInDataResponse;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {

    private final CheckInInterface checkInService;
    private final UserInterface userService;

    public CheckInController(CheckInInterface checkInService,
                             UserInterface userService) {
        this.checkInService = checkInService;
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
