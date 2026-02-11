package techthree.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import techthree.binitright.interfacemethods.AdminInterface;
import techthree.binitright.model.Admin;
import techthree.binitright.model.CheckIn;
import techthree.binitright.model.User;
import techthree.binitright.repository.AdminRepository;
import techthree.binitright.repository.CheckInRepository;
import techthree.binitright.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminImplementation implements AdminInterface{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

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
    public Admin getSingleAdminByUsername(String username) {
        return adminrepo.findByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Admin not found: " + username)
                );
    }


    @Override
	@Transactional
	public List<CheckIn> getPendingCheckIns() {
		return checkInRepository.findByStatusWithDetails(CheckIn.Status.PROCESSING);
	}

	@Override
	@Transactional
	public void updateCheckInStatus(Long id, CheckIn.Status status, String remarks) {
        Integer rewards;
	    CheckIn checkIn = checkInRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("CheckIn not found"));

	    if (status == CheckIn.Status.APPROVED) {
            checkIn.setStatus(status);
            rewards = checkIn.getQuantity()*10;
            checkIn.setRewardPoints(rewards);

            User user = checkIn.getUser();

            int currentBalance = user.getPointBalance() == null ? 0 : user.getPointBalance();
            user.setPointBalance(currentBalance + rewards);

            userRepository.save(user);
        } else {
            checkIn.setStatus(status);
            checkIn.setRewardPoints(0);
        }

        checkInRepository.save(checkIn);
	}

    @Override
    public Optional<Admin> findById(long l) {
        return adminrepo.findById(l);
    }

    @Override
	@Transactional
	public CheckIn reviewCheckIn(Long checkInId) {
		return checkInRepository.findByIdWithDetails(checkInId)
                .orElseThrow(() ->
                        new EntityNotFoundException("CheckIn not found: " + checkInId));
	}
}
