package com.test.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from users",nativeQuery = true)
    List<User> getAll();
    @Query(value = "select u.password from User u where u.name = :name")
    String getHashedPassword(@Param("name") String name);
}
