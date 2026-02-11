package techthree.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techthree.binitright.model.Admin;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByUsername(String username);

}
