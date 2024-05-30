package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	@Autowired
	private BrandRepository repo;
	
//	@Test
//	public void testCreateBrand1() {
//		Category laptops = new Category(3);
//		Brand acer = new Brand("Acer");
//		acer.getCategories().add(laptops);
//		
//		Brand saveBrand = repo.save(acer);
//		
//		assertThat(saveBrand).isNotNull();
//		assertThat(saveBrand.getId()).isGreaterThan(0);
//		
//		
//	}
//	@Test
//	public void testCreateBrand2() {
//		Category laptops = new Category(3);
//		Category smartphones = new Category(7);
//		Brand apple = new Brand("apple");
//		apple.getCategories().add(laptops);
//		apple.getCategories().add(smartphones);
//		
//		Brand saveBrand = repo.save(apple);
//		
//		assertThat(saveBrand).isNotNull();
//		assertThat(saveBrand.getId()).isGreaterThan(0);
//		
//		
//	}
	
//	@Test
//	public void testCreateBrand3() {
//		Brand samsung = new Brand("Samsung");
//		
//		samsung.getCategories().add(new Category(2));	// category memory
//		samsung.getCategories().add(new Category(8));	// category internal hard drive
//		
//		Brand savedBrand = repo.save(samsung);
//		
//		assertThat(savedBrand).isNotNull();
//		assertThat(savedBrand.getId()).isGreaterThan(0);
//	}
	
//	@Test
//	public void testFindAll() {
//		Iterable<Brand> brands = repo.findAll();
//		brands.forEach(System.out::println);
//		
//		assertThat(brands).isNotEmpty();
//	}
	
//	@Test
//	public void testGetById() {
//		Brand brand = repo.findById(1).get();
//		
//		assertThat(brand.getName()).isEqualTo("Acer");
//	}
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = repo.findById(3).get();
		samsung.setName(newName);
		
		Brand savedBrand = repo.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	} 
	
	@Test
	public void testDelete() {
		Integer id = 2;
		repo.deleteById(id);
		
		Optional<Brand> result = repo.findById(id);
		
		assertThat(result.isEmpty());
	}
}
