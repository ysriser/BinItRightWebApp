package tech3.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Admin;

import java.util.List;

@Service
public class AdminSecurityService implements UserDetailsService {

    @Autowired
    private AdminInterface adminInterface;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Admin> admins = adminInterface.findAdminByUsername(username);

        if (admins == null || admins.isEmpty()) {
            throw new UsernameNotFoundException("Admin not found with username: " + username);
        }

        Admin admin = admins.get(0);

        // Map to Spring Security User
        return org.springframework.security.core.userdetails.User.builder()
                .username(admin.getUsername())
                .password(admin.getPasswordUhash()) // The encrypted hash from DB
                .roles("admin")
                .build();
    }
}