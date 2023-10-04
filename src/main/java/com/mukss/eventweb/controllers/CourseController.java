package com.mukss.eventweb.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mukss.eventweb.config.userdetails.CustomUserDetails;
import com.mukss.eventweb.entities.Attend;
import com.mukss.eventweb.entities.AttendsDTO;
import com.mukss.eventweb.entities.Course;
import com.mukss.eventweb.entities.CourseComment;
import com.mukss.eventweb.entities.CoursesDTO;
import com.mukss.eventweb.entities.Event;
import com.mukss.eventweb.entities.User;
import com.mukss.eventweb.exceptions.EventNotFoundException;
import com.mukss.eventweb.services.CourseService;

@Controller
@RequestMapping(value = "/courses", produces = MediaType.TEXT_HTML_VALUE)
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	// GET courses/index.html
	@GetMapping
	public String getCourses(Model model) {
		ArrayList<Course> courselist = new ArrayList<Course>();

		for (Course course : courseService.findAll()) {
			courselist.add(course);
		}
		
		courselist.sort((a, b) -> a.getCourseCode().compareTo(b.getCourseCode()));

		model.addAttribute("courselist", courselist);
		
		return "courses/index";
	}
	
	// GET courses/{id}
	@GetMapping("/{id}")
	public String getEvent(@PathVariable("id") long id,
			@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
		
		Course course = courseService.findById(id).get();

		
		CoursesDTO commlist = new CoursesDTO();
		CoursesDTO owncomm = new CoursesDTO();
		List<CourseComment> fullcommlist = course.getComments();
		List<CourseComment> sortedcomm = new ArrayList<CourseComment>();
		
		// get logged user
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = null;
		if (principal instanceof CustomUserDetails) {
			user = ((CustomUserDetails)principal).getUser();
		}
		// handle NoAuth
//		if (user == null) {
			for (int i=0; i < fullcommlist.size(); i++) {
				sortedcomm.add(fullcommlist.get(i));
			}
//		} else {
//			for (int i=0; i < fullcommlist.size(); i++) {
//				if (fullcommlist.get(i).getUser().getId() != user.getId()) {
//					sortedcomm.add(fullcommlist.get(i));
//				}
//			}
//		}
		// non-user owned comment
		sortedcomm.sort((a, b) -> Long.valueOf(a.getId()).compareTo(Long.valueOf(b.getId())));
		
		commlist.setCommentList(sortedcomm);

		// course 추가		
		model.addAttribute("course", course);
		
		// owncomment list
		List<CourseComment> clist = new ArrayList<CourseComment>();
		if (user != null) {
			for (int i=0; i < fullcommlist.size(); i++) {
				if (fullcommlist.get(i).getUser().getId() == user.getId()) {
					clist.add(fullcommlist.get(i));
				}
			}
		}
		commlist.setCommentList(sortedcomm);
		owncomm.setCommentList(clist);
		model.addAttribute("ownlist", clist);
		// other ppl's comments
		model.addAttribute("sortedcomm", commlist);
		// own comments
		model.addAttribute("owncomm", owncomm);
		
		// 'eattend' 객체 추가
		if (!model.containsAttribute("ecomment")) {
			model.addAttribute("ecomment", new CourseComment());
		}
		return "courses/view";	
	}
	
	// Adding new event
	@GetMapping("/new")
	public String newCourse(Model model) {
		// if model does not have event, initialize a new event
		if (!model.containsAttribute("course")) {
			model.addAttribute("course", new Course());
		}
		return "courses/new";
	}
	
	// POST courses/new
	@PostMapping(value = "/new", consumes = MediaType.ALL_VALUE)
	public String createCourse(@RequestBody @Valid @ModelAttribute Course course, BindingResult errors,
			Model model, RedirectAttributes redirectAttrs) throws IOException {
		
		if (errors.hasErrors()) {
			model.addAttribute("course", course);
			return "courses/new";
		}

		
		// save post after automatically adding relevant meta info
		courseService.save(course);
		redirectAttrs.addFlashAttribute("ok_message", "New course added.");

		return "redirect:/courses";
	}
	
	// GET courses/delete/id
	@GetMapping("delete/{id}")
	public String deleteCourse(@PathVariable("id") long id, RedirectAttributes redirectAttrs) {
		courseService.deleteById(id);
		redirectAttrs.addFlashAttribute("ok_message", "Course deleted.");
		return "redirect:/courses";
	}
}
