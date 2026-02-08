package tech3.binitright.interfacemethods;


import java.util.List;

import tech3.binitright.model.User;

public interface UserInterface {


    User saveUser(User user);
    List<User> findByUsername(String username);
    User findById(Long userId);

    boolean existsByUsername(String username);


}
