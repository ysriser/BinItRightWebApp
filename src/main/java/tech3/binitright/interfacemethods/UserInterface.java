package tech3.binitright.interfacemethods;


import tech3.binitright.model.Admin;

import java.util.List;
import java.util.Map;

public interface UserInterface {
    void saveAdmin(Admin adminToSave);

    public List<Admin> findAdminByUsername(String username);
}
