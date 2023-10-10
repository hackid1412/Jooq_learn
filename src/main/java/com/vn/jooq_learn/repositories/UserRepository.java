package com.vn.jooq_learn.repositories;

import com.vn.jooq_learn.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
