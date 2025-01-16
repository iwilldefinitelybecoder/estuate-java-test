package com.estuate.project1.repository;

import com.estuate.project1.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users,Long> {
}
