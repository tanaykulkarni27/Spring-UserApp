package com.test.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
// Long : Data type of your @Id field [primary key]
// User : ClassName of the Entity
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u.password from User u where u.name = :name")
    String getHashedPassword(@Param("name") String name);
}
