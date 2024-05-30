package com.shopme.admin.setting.state;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

public interface StateRepository extends CrudRepository<State, Integer>,PagingAndSortingRepository<State, Integer> {
	public List<State> findByCountryOrderByNameAsc(Country country);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM State s WHERE s.country = :country")
	public void deleteStatesByCountry(Country country);
}
