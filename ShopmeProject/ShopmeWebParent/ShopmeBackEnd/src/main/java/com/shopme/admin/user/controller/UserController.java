package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {
	
	private String defaultRedirectURL = "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	@Autowired
	private UserService service;
	
	
	@GetMapping("/users")
//	public String list(Model model) {
//		List<User> listUsers = service.listAll();
//		
//		model.addAttribute("listUsers",listUsers);
//		return "users";
//	}
	public String listFirstPage(Model model) {
//		return listByPage(1, model, "firstName", "asc", null);
		return defaultRedirectURL;
	}

//	@GetMapping("/users/page/{pageNum}")
//	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
//			@Param("sortField") String sortField, 
//			@Param("sortDir") String sortDir,
//			@Param("keyWord") String keyWord) {
//		
//		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyWord);
//		List<User> listUsers = page.getContent();
//		
////		System.out.println("pageNum = "+pageNum);
////		System.out.println("Total element = "+page.getTotalElements());
////		System.out.println("Total pages = "+ page.getTotalPages());
//		
//		Long startCount = (long) ((pageNum -1) * UserService.USERS_PER_PAGE+1);
//		Long endCount = startCount + UserService.USERS_PER_PAGE -1;
//		if(endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//		
//		String reverseSortDir = sortDir.equals("asc")?"desc":"asc";
//		model.addAttribute("currentPage",pageNum);
//		model.addAttribute("startCount",startCount);
//		model.addAttribute("endCount",endCount);
//		model.addAttribute("totalPages",page.getTotalPages());
//		model.addAttribute("totalItems",page.getNumberOfElements());
//		model.addAttribute("listUsers",listUsers);
//		model.addAttribute("sortField",sortField);
//		model.addAttribute("sortDir",sortDir);
//		model.addAttribute("reverseSortDir",reverseSortDir);
//		model.addAttribute("keyWord",keyWord);
//		return "users/users";
//	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {
		service.listByPage(pageNum, helper);		
		
		return "users/users";		
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
		user.setEnabled(true);
		
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		model.addAttribute("pageTitle","Create New User");
		return "users/user_form";
	}
	@PostMapping("/users/save")
	public String saveUser(User user,RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser,
			@RequestParam("image") MultipartFile multipartFile ) throws IOException {
		System.out.println(user);
		System.out.println(multipartFile.getOriginalFilename());
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/"+savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}

		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		
		return getRedirectURLptoAffectedUser(user);
	}

	private String getRedirectURLptoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		
		return "redirect:/users/page/1?sortField=Id&sortDir=asc&keyWord="+firstPartOfEmail;
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {

		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("listRoles",listRoles);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle","Edit User (ID: "+id + ")");
			return "users/user_form";
		}catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return defaultRedirectURL;
		}
		
	}
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {

		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The user ID" + id + "has been deleted successfully");
		}catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
		}
		return defaultRedirectURL;
	}
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		service.updateUserEnableStatus(id, enabled);
		
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been "+status;
		
		redirectAttributes.addFlashAttribute("message",message);

		return defaultRedirectURL;
	}
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}
}
