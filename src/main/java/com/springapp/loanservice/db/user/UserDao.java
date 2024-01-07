package com.springapp.loanservice.db.user;

import java.util.Optional;
import com.springapp.loanservice.db.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

}
