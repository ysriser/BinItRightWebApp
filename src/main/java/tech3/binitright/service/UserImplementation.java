package tech3.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.repository.AdminRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserImplementation implements UserInterface {

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
}
