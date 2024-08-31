package com.tsd.dist.registration.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.tsd.sdk.exception.DistributorException;
import org.tsd.sdk.model.Address;
import org.tsd.sdk.model.Distributor;
import org.tsd.sdk.response.JsonSuccessResponse;
import org.tsd.sdk.response.ResponseObject;
import org.tsd.sdk.resquest.AddressReq;
import org.tsd.sdk.resquest.DistributorReq;

import com.tsd.dist.registration.repo.AddressRepo;
import com.tsd.dist.registration.repo.DistributorRepo;

import lombok.SneakyThrows;

@Service
public class DistributorService {

	@Autowired(required = true)
	private DistributorRepo distributorRepo;
	
	@Autowired
	private AddressRepo addressRepo;

	@Transactional
	@SneakyThrows
	public Distributor saveDistributor(DistributorReq distributorReq) {
		Distributor distributor = distributorRepo.findByMobile(distributorReq.getMobileNo());
		if (null != distributor) {
			throw new DistributorException("Distributor already exists with " + distributorReq.getMobileNo());
		}
		distributor = Distributor.builder()
				.first_name(distributorReq.getFirst_name())
				.last_name(distributorReq.getLast_name())
				.username(distributorReq.getUserName())
				.password(distributorReq.getPassword())
				.security_question(distributorReq.getSecurityQuestion())
				.security_answer(distributorReq.getSecurityAnswer())
				.company_code(distributorReq.getCompanyCode())
				.company_name(distributorReq.getCompanyName())
				.contact_person_name(distributorReq.getContactPersonName())
				.mobile(distributorReq.getMobileNo())
				.email(distributorReq.getEmail())
				.pannum(distributorReq.getPannum())
				.gstin(distributorReq.getGstin())
				.active(false)
				.enabled(false)
				.created_by("Admin")
				.created_on(Timestamp.valueOf(LocalDateTime.now()))
				.last_updated_by("Admin")
				.last_updated_on(Timestamp.valueOf(LocalDateTime.now())).build();
		
		distributorRepo.save(distributor);
		
		return saveDistributorAddress(distributor, distributorReq.getAddressReq());
		
	}
	
	@Transactional
	@SneakyThrows
	private Distributor saveDistributorAddress(Distributor distributor, AddressReq addressReq) {
		Address address = Address.builder()
				.short_name(addressReq.getShort_name())
				.line1(addressReq.getLine1())
				.line2(addressReq.getLine2())
				.line3(addressReq.getLine3())
				.address(addressReq.getAddress())
				.country(addressReq.getCountry())
				.state_name(addressReq.getState_name())
				.city(addressReq.getCity())
				.pin_code(addressReq.getPin_code())
				.defaultAddress(addressReq.is_default())
				.geo_tag(addressReq.getGeo_tag())
				.verifiedAddress(addressReq.is_verified())
				.mobile(Long.parseLong(distributor.getMobile()))
				.build();
		
		addressRepo.save(address);
		
		List<Address> list = new ArrayList<Address>();
		list.add(address);
		distributor.setAddresses(list);
		
		return distributor;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	@SneakyThrows
	public ResponseEntity<?> fetchAllDistributor(String filter, String value) {
		if (filter.equals("All")) {
			List<Distributor> distributors = distributorRepo.findAll();
			List<Address> addresses = null;
			for(Distributor distributor : distributors) {
				addresses = addressRepo.findByMobile(Long.parseLong(distributor.getMobile()));
				distributor.setAddresses(addresses);
			}
			return ResponseEntity.ok(JsonSuccessResponse.ok("Success", 200, distributors));
		} else {
			Distributor distributor = null;
			switch (filter) {
			case "mobile":
				distributor = distributorRepo.findByMobile(value);
				distributor.setAddresses(addressRepo.findByMobile(Long.parseLong(distributor.getMobile())));
				return ResponseEntity.ok(JsonSuccessResponse.ok("Success", 200, distributor));
			case "username":
				distributor = distributorRepo.findByUsername(value);
				distributor.setAddresses(addressRepo.findByMobile(Long.parseLong(distributor.getMobile())));
				return ResponseEntity.ok(JsonSuccessResponse.ok("Success", 200, distributor));
			case "email":
				distributor = distributorRepo.findByEmail(value);
				distributor.setAddresses(addressRepo.findByMobile(Long.parseLong(distributor.getMobile())));
				return ResponseEntity.ok(JsonSuccessResponse.ok("Success", 200, distributor));
			case "pannum":
				distributor = distributorRepo.findByPannum(value);
				distributor.setAddresses(addressRepo.findByMobile(Long.parseLong(distributor.getMobile())));
				return ResponseEntity.ok(JsonSuccessResponse.ok("Success", 200, distributor));
			default:
				return ResponseEntity.ok(JsonSuccessResponse.ok("Mo match found", 404, null));
			}
		}
	}

	@Transactional
	@SneakyThrows
	public JsonSuccessResponse<?> deleteDistributor(String mobile) {
		distributorRepo.deleteByMobile(mobile);
		return JsonSuccessResponse.ok("Resource Successfully deleted", 204, null);
	}

	@Transactional
	@SneakyThrows
	public ResponseObject modifyDistributor(String mobilenumber, DistributorReq distributorReq) throws DistributorException {

		Distributor distributor = distributorRepo.findByMobile(mobilenumber);
		if (null == distributor.getId()) {
			throw new DistributorException("Distributor not found with " + mobilenumber);
		}

		distributor.setUsername(distributorReq.getUserName());
		distributor.setPassword(distributorReq.getPassword());
		distributor.setSecurity_question(distributorReq.getSecurityQuestion());
		distributor.setSecurity_answer(distributorReq.getSecurityAnswer());
		distributor.setCompany_code(distributorReq.getCompanyCode());
		distributor.setCompany_name(distributorReq.getCompanyName());
//		distributor.setAddress(distributorReq.getAddress());
//		distributor.setCountry(distributorReq.getCountry());
//		distributor.setState(distributorReq.getState());
//		distributor.setCity(distributorReq.getCity());
//		distributor.setPin_code(distributorReq.getPinCode());
		distributor.setContact_person_name(distributorReq.getContactPersonName());
		distributor.setMobile(distributorReq.getMobileNo());
		distributor.setEmail(distributorReq.getEmail());
		distributor.setPannum(distributorReq.getPannum());
		distributor.setGstin(distributorReq.getGstin());
		distributor.setActive(distributorReq.isActive());
		
		distributor.setLast_updated_by("Admin");
		distributor.setLast_updated_on(Timestamp.valueOf(LocalDateTime.now()));

		distributorRepo.save(distributor);
		return JsonSuccessResponse.ok("Distributor updated successfully", 200, distributor);
	}
}
