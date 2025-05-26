package com.rolesspringsecurity.demo.repositories;

import com.rolesspringsecurity.demo.entities.RolesEntity;
import com.rolesspringsecurity.demo.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends CrudRepository<RolesEntity, Long> {

}