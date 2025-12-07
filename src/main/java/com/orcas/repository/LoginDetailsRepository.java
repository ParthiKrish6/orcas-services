package com.orcas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orcas.entity.LoginDetails;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {
	@Query("select b from LoginDetails b where b.userId = :userId and b.pwd = :pwd")
	LoginDetails getUser(@Param("userId") String user, @Param("pwd") String pwd);
}