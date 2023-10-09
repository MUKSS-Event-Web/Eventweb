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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import com.mukss.eventweb.config.userdetails.CustomUserDetails;
import com.mukss.eventweb.entities.Event;
import com.mukss.eventweb.entities.User;
import com.mukss.eventweb.exceptions.EventNotFoundException;
import com.mukss.eventweb.services.EventService;
import com.mukss.eventweb.entities.Attend;
import com.mukss.eventweb.entities.AttendsDTO;

@Controller
@RequestMapping(value = "/events", produces = MediaType.TEXT_HTML_VALUE)
public class EventsController {
	
	@Autowired
	private EventService eventService;
	
	// TODO: Add not_found error
	@ExceptionHandler(EventNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String EventNotFoundHandler(EventNotFoundException ex, Model model){
		model.addAttribute("not_found_id", ex.getId());
		
		return "events/not_found";
	}
	
	// Returns all events as a list, under attribute "posts" of model
	@GetMapping
	public String getEvents(Model model) {
		ArrayList<Event> eventsResults = new ArrayList<Event>();
		Iterable<Event> pastEvents; 
		Iterable<Event> futureEvents;

		for (Event event : eventService.findAll()) {
			eventsResults.add(event);
		}
		
		eventsResults.sort((a, b) -> a.getDate().compareTo(b.getDate()) == 0 ? a.getTitle().compareTo(b.getTitle()) : a.getDate().compareTo(b.getDate()));
		
		ArrayList<Event> pastEventsResults = new ArrayList<Event>();
		ArrayList<Event> futureEventsResults = new ArrayList<Event>();
		
		for (Event event : eventsResults) {
			if (event.getDate().compareTo(LocalDate.now()) >= 0) {
				futureEventsResults.add(event);
			} else {
				pastEventsResults.add(event);
			}
		}
		
		pastEvents = pastEventsResults;
		futureEvents = futureEventsResults;

		
		model.addAttribute("pastEvents", pastEvents);
		model.addAttribute("futureEvents", futureEvents);
		model.addAttribute("events", eventService.findAll());
		
		return "events/index";
	}
	
	// Returns event with given event ID, under attribute event
	@GetMapping("/{id}")
	public String getEvent(@PathVariable("id") long id,
			@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
		
		Event event = eventService.findById(id).orElseThrow(() -> new EventNotFoundException(id));

		String imageString = Base64.getEncoder().encodeToString(event.getData());
		
		AttendsDTO attendsForm = new AttendsDTO();
		AttendsDTO ownatt = new AttendsDTO();
		List<Attend> attlist = event.getAttends();
		List<Attend> sortedatt = new ArrayList<Attend>();
		
		for (int i=0; i < attlist.size(); i++) {
			if (attlist.get(i).getStatus().contains("firm") || attlist.get(i).getStatus().contains("ting")) {
				sortedatt.add(attlist.get(i));
			}
		}
		sortedatt.sort((a, b) -> Long.valueOf(a.getId()).compareTo(Long.valueOf(b.getId())));
		
		attendsForm.setAttendList(sortedatt);
		
		// set author info and time info here
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
        }

		// attend 추가		
		model.addAttribute("event", event);
		model.addAttribute("eventImage", imageString);
		model.addAttribute("imageFileType", event.getImageFileType());
		
		List<Attend> flist = new ArrayList<Attend>();
		if (user != null) {
			for (int i=0; i < attlist.size(); i++) {
				if (attlist.get(i).getUser().getId() == user.getId()) {
					flist.add(attlist.get(i));
				}
			}
		}
		ownatt.setAttendList(flist);
		
		model.addAttribute("attendsForm", attendsForm);
		model.addAttribute("ownatt", ownatt);
		
		// 'eattend' 객체 추가
		if (!model.containsAttribute("eattend")) {
			model.addAttribute("eattend", new Attend());
		}
		return "events/view";	
	}
	
	@GetMapping("/attendee/{id}")
	public String getAttendee(@PathVariable("id") long id,
			@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
		
		Event event = eventService.findById(id).orElseThrow(() -> new EventNotFoundException(id));

		String imageString = Base64.getEncoder().encodeToString(event.getData());
		
		AttendsDTO attendsForm = new AttendsDTO();
		AttendsDTO rejForm = new AttendsDTO();
		List<Attend> attlist = event.getAttends();
		List<Attend> flist = new ArrayList<Attend>();
		List<Attend> rlist = new ArrayList<Attend>();
		for (int i=0; i < attlist.size(); i++) {
			if (attlist.get(i).getStatus().contains("firm")) {
				flist.add(attlist.get(i));
			}
			if (attlist.get(i).getStatus().contains("ject")) {
				rlist.add(attlist.get(i));
			}
		}
		// Lname -> Fname -> ID sort, ascending
		flist.sort((a, b) -> Long.valueOf(a.getId()).compareTo(Long.valueOf(b.getId())));
		flist.sort((a, b) -> a.getUser().getFirstName().compareTo(b.getUser().getFirstName()));
		flist.sort((a, b) -> a.getUser().getLastName().compareTo(b.getUser().getLastName()));
		rlist.sort((a, b) -> Long.valueOf(a.getId()).compareTo(Long.valueOf(b.getId())));
		
		attendsForm.setAttendList(flist);
		rejForm.setAttendList(rlist);

		// attend 추가		
		model.addAttribute("event", event);
		model.addAttribute("eventImage", imageString);
		model.addAttribute("imageFileType", event.getImageFileType());
		
		model.addAttribute("attendsForm", attendsForm);
		model.addAttribute("rejForm", rejForm);
		
		// 'eattend' 객체 추가
		if (!model.containsAttribute("eattend")) {
			model.addAttribute("eattend", new Attend());
		}
		return "events/attendee";	
	}
	
	
	// Adding new event
	@GetMapping("/new")
	public String newEvent(Model model) {
		// if model does not have event, initialize a new event
		if (!model.containsAttribute("event")) {
			model.addAttribute("event", new Event());
		}
		return "events/new";
	}
	
	@PostMapping(value = "/new", consumes = MediaType.ALL_VALUE)
	public String createEvent(@RequestParam("imgFile") MultipartFile imgFile, @RequestBody @Valid @ModelAttribute Event event, BindingResult errors,
			Model model, RedirectAttributes redirectAttrs) throws IOException {
		
		if (errors.hasErrors()) {
			model.addAttribute("event", event);
			return "events/new";
		}

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = null;
		if (principal instanceof CustomUserDetails) {
			user = ((CustomUserDetails)principal).getUser();
		} 		
		event.setUser(user);
		event.setTimeUploaded(LocalDateTime.now());
		
		// Saving an image for an event
		String fileName = StringUtils.cleanPath(imgFile.getOriginalFilename());
		event.setImageName(fileName);
		event.setImageFileType(imgFile.getContentType());
		event.setData(imgFile.getBytes());
		
		// save post after automatically adding relevant meta info
		eventService.save(event);
		redirectAttrs.addFlashAttribute("ok_message", "New event added.");

		return "redirect:/events";
	}
		
	// Update new event
	@GetMapping("/update/{id}")
	public String updateEvent(@PathVariable("id") long id, Model model) {
		// if model does not have event, initialize a new event
		if (!model.containsAttribute("event")) {
			model.addAttribute("event", new Event());
		}
		// load event by id
		Event event = eventService.findById(id).orElseThrow(()-> new EventNotFoundException(id));
		model.addAttribute("event", event);
		
		return "events/update";
	}
	
	@PostMapping(value = "/update", consumes = MediaType.ALL_VALUE)
	public String updateEvent(@RequestBody @Valid @ModelAttribute Event event, BindingResult errors,
			Model model, RedirectAttributes redirectAttrs) {
		
		Event retrievedEvent = eventService.findById(event.getId()).orElseThrow(()-> new EventNotFoundException(event.getId()));
		
		if (errors.hasErrors()) {
			model.addAttribute("event", retrievedEvent);
			return "/events/update";
		}
		
		retrievedEvent.setLocation(event.getLocation());
		retrievedEvent.setTitle(event.getTitle());
		retrievedEvent.setContent(event.getContent());	
		retrievedEvent.setTime(event.getTime());
		retrievedEvent.setDate(event.getDate());

		
		// save update event info
		eventService.save(retrievedEvent);
		redirectAttrs.addFlashAttribute("ok_message", "Event " + retrievedEvent.getTitle() + " was updated.");
		return "redirect:/events";
	}
	
	//delete one greeting
	@DeleteMapping("/{id}")
	public String deletePost(@PathVariable("id") long id, RedirectAttributes redirectAttrs) {
		eventService.deleteById(id);
		redirectAttrs.addFlashAttribute("ok_message", "Event deleted.");
		return "redirect:/events";
	}
	
}