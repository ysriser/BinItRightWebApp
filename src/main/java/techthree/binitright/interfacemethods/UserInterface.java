package techthree.binitright.interfacemethods;


import techthree.binitright.model.User;

import java.util.List;

public interface UserInterface {


    User saveUser(User user);
    List<User> findByUsername(String username);
    User findById(Long userId);

    boolean existsByUsername(String username);


}
