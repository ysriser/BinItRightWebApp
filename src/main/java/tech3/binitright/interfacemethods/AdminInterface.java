package tech3.binitright.interfacemethods;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import tech3.binitright.model.CheckIn;
import tech3.binitright.request.ReviewRequest;;

public interface AdminInterface {
	public CheckIn reviewCheckIn(Long checkInId);
	public void updateCheckInStatus(Long id, CheckIn.Status status, String remarks);
}
