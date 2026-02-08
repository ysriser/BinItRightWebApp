package tech3.binitright.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech3.binitright.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByUsername(String username);

}
