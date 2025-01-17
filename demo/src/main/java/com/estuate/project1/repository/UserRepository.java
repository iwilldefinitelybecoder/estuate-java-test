package com.estuate.project1.repository;

import com.estuate.project1.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByEmail(String email);

    Page<Users> findAllByEmailNot(String email, Pageable pageable);
}
