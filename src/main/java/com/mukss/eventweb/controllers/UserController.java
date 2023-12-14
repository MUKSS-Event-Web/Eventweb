package com.mukss.eventweb.controllers;
 
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
 
 
import com.mukss.eventweb.config.userdetails.CustomUserDetails;
import com.mukss.eventweb.entities.Event;
import com.mukss.eventweb.entities.MembershipsDTO;
import com.mukss.eventweb.entities.User;
import com.mukss.eventweb.exceptions.UserNotFoundException;
import com.mukss.eventweb.services.EventService;
import com.mukss.eventweb.services.UserService;
import com.mukss.eventweb.entities.Attend;
import com.mukss.eventweb.entities.AttendsDTO;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
 
@Controller
@RequestMapping(value = "/users", produces = MediaType.TEXT_HTML_VALUE)
public class UserController {
 
	@Autowired
	private UserService userService;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
 
	// TODO: Handle not_found properly
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String UserNotFoundHandler(UserNotFoundException ex, Model model){
		model.addAttribute("not_found_id", ex.getMessage());
 
		return "events/not_found";
	}
 
	// Returns all events as a list, under attribute "posts" of model
	@GetMapping("/list")
	public String getUsers(Model model) {
 
		Iterable<User> ulist = userService.findAll();
		List<User> memlist = new ArrayList<User>();
		for (User u : ulist) {
			memlist.add(u);
		}
		
		memlist.sort((a, b) -> Long.valueOf(a.getId()).compareTo(Long.valueOf(b.getId())));
 
		MembershipsDTO membershipsDTO = new MembershipsDTO();
		membershipsDTO.setUsersList(memlist);
 
		model.addAttribute("ulist", membershipsDTO);
 
		return "users/list";
	}
	
	// NAVIGATING TO THIS SUBURL DELETES THE ASSOCIATED USER
	@GetMapping ("deleteUser/{id}")
	public String deleteUser(@PathVariable("id") long id, RedirectAttributes redirectAttrs) {
		userService.deleteById(id);
		redirectAttrs.addFlashAttribute("ok_message", "User deleted.");
		return "redirect:/users/list";
	}
	
	// Have to refactor later. This repo might be dump in the future.
	@GetMapping("{userName}/{password}")
	public ResponseEntity<?> updateUserPassword(@PathVariable("userName") String userName, @PathVariable("password") String password) {
		Optional<User> user = userService.findByName(userName);
		
		if (user.isEmpty()) {
			return new ResponseEntity<>(BAD_REQUEST);
		}
		
		User notNullUser = user.get();
		notNullUser.setPassword(passwordEncoder.encode(password));
		
		userService.save(notNullUser);
		
		return new ResponseEntity<>(NO_CONTENT);
	}

}