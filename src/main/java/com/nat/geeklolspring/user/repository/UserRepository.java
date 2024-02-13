package com.nat.geeklolspring.user.repository;

import com.nat.geeklolspring.entity.Role;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,String> {
    // 사용자의 권한을 업데이트하는 메서드
    @Modifying
    @Query("UPDATE User u SET u.role = :newAuthority WHERE u.id = :userId")
    void updateAuthority(@Param("userId") String userId, @Param("newAuthority")Role role);
}
