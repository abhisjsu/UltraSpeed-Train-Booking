package com.cmpe275.cusr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmpe275.cusr.model.UserBean;

@Repository
public interface UserRepository extends JpaRepository<UserBean, String>{
	UserBean findByEmail(String email);
}
