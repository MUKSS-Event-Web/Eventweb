package com.mukss.eventweb.controllers;

import java.time.LocalDateTime;
import java.util.Base64;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mukss.eventweb.services.CourseService;
import com.mukss.eventweb.config.userdetails.CustomUserDetails;
import com.mukss.eventweb.entities.Attend;
import com.mukss.eventweb.entities.Course;
import com.mukss.eventweb.entities.CourseComment;
import com.mukss.eventweb.entities.Event;
import com.mukss.eventweb.entities.User;
import com.mukss.eventweb.exceptions.EventNotFoundException;
import com.mukss.eventweb.services.CourseCommentService;

@Controller
@RequestMapping(value = "/comments", produces = MediaType.TEXT_HTML_VALUE)
public class CourseCommentController {
	@Autowired
    private CourseService courseService;

    @Autowired
    private CourseCommentService courseCommentService;
    
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createComment(@RequestBody @Valid @ModelAttribute CourseComment ecomment, BindingResult errors,
    		@RequestParam(value = "courseId", required = true) String courseId, 
    		Model model, RedirectAttributes redirectAttrs) {
    	
    	long parsedCourseId = Long.parseLong(courseId);
    	Course course = courseService.findById(parsedCourseId).get();
        // return to original url if error
        if (errors.hasErrors()) {
        	// attend 추가		
    		model.addAttribute("course", course);
            
    		model.addAttribute("ecomment", ecomment);
            
    		return "redirect:/courses/" + courseId;
        }

        // set author info and time info here
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
        }
        

        // TODO: Handle if there is no user logged in.
        if (!model.containsAttribute("eattend")) {
            model.addAttribute("eattend", new Attend());
        }

        ecomment.setUser(user);
        ecomment.setLastEdited(LocalDateTime.now());
        ecomment.setCourse(course);
        courseCommentService.save(ecomment);
        redirectAttrs.addFlashAttribute("ok_message", "New review added.");

        // return to original url upon saving
        return "redirect:/courses/" + courseId;
    }
}
