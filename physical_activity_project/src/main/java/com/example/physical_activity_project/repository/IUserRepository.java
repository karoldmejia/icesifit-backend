package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.physical_activity_project.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByInstitutionalEmail(String institutionalEmail);
}

