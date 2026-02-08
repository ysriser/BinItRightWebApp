package tech3.binitright.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.User;
import tech3.binitright.repository.AdminRepository;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.repository.UserRepository;

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
    public void saveAdmin(final Admin admin) {
        adminrepo.save(admin);
    }

    @Override
    public List<Admin> findAdminByUsername(final String username) {
        final List<Admin> admins = new ArrayList<>();
        admins.addAll(adminrepo.findByUsername(username));
        return admins;
    }

    @Override
    public Admin getSingleAdminByUsername(final String username) {
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
	public void updateCheckInStatus(final Long id, final CheckIn.Status status, final String remarks) {
        Integer rewards;
	    final CheckIn checkIn = checkInRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("CheckIn not found"));

	    if (status == CheckIn.Status.APPROVED) {
            checkIn.setStatus(status);
            rewards = checkIn.getQuantity()*10;
            checkIn.setRewardPoints(rewards);

            final User user = checkIn.getUser();

            final int currentBalance = user.getPointBalance() == null ? 0 : user.getPointBalance();
            user.setPointBalance(currentBalance + rewards);

            System.out.println(currentBalance + "  " + user.getPointBalance());

            userRepository.save(user);
        } else {
            checkIn.setStatus(status);
            checkIn.setRewardPoints(0);
        }

        //checkIn.setAdminRemarks(remarks);
        checkInRepository.save(checkIn);
	}

    @Override
    public Optional<Admin> findById(final long l) {
        return adminrepo.findById(l);
    }

    @Override
	@Transactional
	public CheckIn reviewCheckIn(final Long checkInId) {
		return checkInRepository.findByIdWithDetails(checkInId)
                .orElseThrow(() ->
                        new EntityNotFoundException("CheckIn not found: " + checkInId));
	}
}
