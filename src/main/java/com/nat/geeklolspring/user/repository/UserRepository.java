package com.nat.geeklolspring.user.repository;

import com.nat.geeklolspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

}
