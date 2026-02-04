package tech3.binitright.interfacemethods;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import tech3.binitright.model.Admin;
import tech3.binitright.model.CheckIn;
import tech3.binitright.request.ReviewRequest;;

public interface AdminInterface {

    void saveAdmin(Admin adminToSave);
    public List<Admin> findAdminByUsername(String username);
    public Admin getSingleAdminByUsername(String username);
	public List<CheckIn> getPendingCheckIns();
	public CheckIn reviewCheckIn(Long checkInId);
	public void updateCheckInStatus(Long id, CheckIn.Status status, String remarks);
}
