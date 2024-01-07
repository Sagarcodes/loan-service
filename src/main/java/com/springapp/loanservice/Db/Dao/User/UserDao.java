package com.springapp.loanservice.Db.Dao.User;

import java.util.Optional;
import com.springapp.loanservice.Db.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

}
