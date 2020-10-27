package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users,String> {
    Users findUserByEmail(String email);
}
