package tech3.binitright.interfacemethods;


import tech3.binitright.model.Admin;
import tech3.binitright.model.User;

import java.util.List;
import java.util.Map;

public interface UserInterface {


    void saveUser(User user);
    List<User> findByUsername(String username);
}
