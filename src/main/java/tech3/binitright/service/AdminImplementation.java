package tech3.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.CheckIn;
import tech3.binitright.repository.AdminRepository;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.request.ReviewRequest;

@Service
@Transactional
public class AdminImplementation implements AdminInterface{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
	private CheckInRepository checkInRepository;

    @Autowired
    AdminRepository adminrepo;


    @Override
    public void saveAdmin(Admin admin) {
        adminrepo.save(admin);
    }

    @Override
    public List<Admin> findAdminByUsername(String username) {
        List<Admin> admins = new ArrayList<>();
        admins.addAll(adminrepo.findByUsername(username));
        return admins;
    }

	@Override
	@Transactional
	public List<CheckIn> getPendingCheckIns() {
		return checkInRepository.findByStatusWithDetails(CheckIn.Status.PROCESSING);
	}

	@Override
	@Transactional
	public void updateCheckInStatus(Long id, CheckIn.Status status, String remarks) {
	    CheckIn checkIn = checkInRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("CheckIn not found"));

	    if (status == CheckIn.Status.APPROVED) {
            checkIn.setStatus(status);
            // calculate reward points here
        } else {
            checkIn.setStatus(status);
            //checkIn.setRewardPoints(0);
        }

        //checkIn.setAdminRemarks(remarks); 
        checkInRepository.save(checkIn);
	}

	@Override
	@Transactional
	public CheckIn reviewCheckIn(Long checkInId) {
		return checkInRepository.findByIdWithDetails(checkInId)
                .orElseThrow(() ->
                        new EntityNotFoundException("CheckIn not found: " + checkInId));
	}

	

	
}
