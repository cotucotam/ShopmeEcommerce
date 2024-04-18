package com.shopme.admin.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Role;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity", "com.shopme.admin.user"})

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

}
