package com.tsd.dist.registration.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tsd.sdk.model.Distributor;



public interface DistributorRepo extends JpaRepository<Distributor, Long> {
	void deleteByMobile(String mobile);
	Distributor findByEmail(String email);
	Distributor findByUsername(String username);
	Distributor findByPannum(String pannum);
	Distributor findByMobile(String mobile);
}