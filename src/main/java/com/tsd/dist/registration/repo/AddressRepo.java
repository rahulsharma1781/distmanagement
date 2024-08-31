package com.tsd.dist.registration.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tsd.sdk.model.Address;



public interface AddressRepo extends JpaRepository<Address, Long>{
	List<Address> findByMobile(Long mobile);
}