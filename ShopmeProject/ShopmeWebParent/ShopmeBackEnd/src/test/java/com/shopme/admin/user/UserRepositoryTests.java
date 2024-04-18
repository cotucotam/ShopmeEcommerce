package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql =false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
//	@Test
//	public void testCreateUser() {
//		Role roleAdmin = entityManager.find(Role.class, 1);
//		User userCoTam = new User("tam@culi.net","nam2020","Tam","Tran Co");
//		userCoTam.addRole(roleAdmin);
//		
//		User savedUser = repo.save(userCoTam);
//		assertThat(savedUser.getId()).isGreaterThan(0);
//	}
//	
//	@Test
//	public void testCreateNewUserwithTwoRoles() {
//		User userRavi = new User("ravi@gmail.com","ravi2020","Ravi","kumar");
//		Role roleEditor = new Role(3);
//		Role roleAssistant = new Role(5);
//		userRavi.addRole(roleEditor);
//		userRavi.addRole(roleAssistant);
//		
//		User savedUser = repo.save(userRavi);
//		assertThat(savedUser.getId()).isGreaterThan(0);
//		
//	}
//	@Test
//	public void testListAllUsers() {
//		Iterable<User> listUsers = repo.findAll();
//		listUsers.forEach(user -> System.out.println(user));
//	}
//	@Test
//	public void testGetUserById() {
//		User UserTam = repo.findById(1).get();
//		System.out.println(UserTam);
//		assertThat(UserTam).isNotNull();
//	}
//	@Test
//	public void testUpdateUserDetails() {
//		User userTam = repo.findById(1).get();
//		userTam.setEnabled(true);
//		userTam.setEmail("trancotam@gmail.com");
//		repo.save(userTam);
//	}
//	@Test
//	public void testUpdateUserRoles() {
//		User userTam = repo.findById(1).get();
//		Role roleEditor = new Role(3);
//		Role roleSalePerson = new Role(2);
//		
//		userTam.getRoles().remove(roleEditor);
//		userTam.addRole(roleSalePerson);
//
//		repo.save(userTam);
//	}
//	@Test
//	public void testDeleteUser() {
//		Integer userId = 9;
//		repo.deleteById(userId);
//	}
//	@Test 
//	public void testGetUserByEmail() {
//		String email = "trancotam@gmail.com";
//		User user = repo.getUserByEmail(email);
//		assertThat(user).isNotNull();
//	}
//	@Test 
//	public void testCountById() {
//		Integer id = 100;
//		Long countById = repo.countById(id);
//
//		assertThat(countById).isNotNull().isGreaterThan(0);
//	}
//	
//	@Test
//	public void testDisableUser() {
//		Integer id = 2;
//		repo.updateEnableStatus(id, false);
//	}
//	@Test
//	public void testEnableUser() {
//		Integer id = 2;
//		repo.updateEnableStatus(id, true);
//	}
//	@Test
//	public void TestListFirstPage() {
//		int pageNumber = 0;
//		int pageSize = 4;
//		
//		Pageable pageable = PageRequest.of(pageNumber, pageSize);
//		Page<User> page = repo.findAll(pageable);
//	
//		List<User> listUsers = page.getContent();
//		listUsers.forEach(user-> System.out.println(user));
//		
//		assertThat(listUsers.size()).isEqualTo(pageSize);
//	}
	
	@Test
	public void testSearchUsers() {
		System.out.println("testSearchUsers");
		String keyWords = "tam";
		int pageNumber = 0;
		int pageSize = 10;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyWords, pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isGreaterThan(0);
		
		
	}
}
