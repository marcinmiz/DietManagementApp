package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.Note;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.repos.NoteRepo;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class NoteController
{
	@Autowired
	private NoteRepo noteRepo;

	@Autowired
	private UserService userService;

	@GetMapping("/notes")
	public String notes(Principal principal, Model model)
	{
		User user = (User) userService.loadUserByUsername(principal.getName());

		List<Note> notes = noteRepo.findByUserId(user.getId());
		model.addAttribute("notes", notes);
		model.addAttribute("user", user);

		return "notes";
	}

	@PostMapping("/addnote")
	public String addNote(Principal principal, String title, String product, String note)
	{
		User user = (User) userService.loadUserByUsername(principal.getName());

		Note newNote = new Note();
		newNote.setTitle(title);
		newNote.setProduct(product);
		newNote.setNote(note);
		newNote.setUserId(user.getId());

		noteRepo.save(newNote);

		return "redirect:/notes";
	}
}
