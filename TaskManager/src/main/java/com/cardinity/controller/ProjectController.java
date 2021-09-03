package com.cardinity.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cardinity.pojo.Project;
import com.cardinity.service.ProjectService;

@RestController
@Validated
@RequestMapping(value = "projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@GetMapping(value = "projects")
	public List<Project> getProjects() {
		return projectService.getProjects();
	}

	@GetMapping(value = "user-projects/{username}")
	public List<Project> getprojectsForUser(@PathVariable String username) {
		return projectService.getProjectsForUser(username);
	}

	@PostMapping(value = "project")
	public @ResponseBody Project postProject(@Valid @RequestBody Project project) {
		return projectService.save(projectService.addUser(project));
	}

	@PutMapping(value = "project")
	public @ResponseBody Project putproject(@Valid @RequestBody Project project) throws Exception {
		return projectService.save(project);
	}

	@DeleteMapping(value = "project/{id}")
	public @ResponseBody Project deleteproject(@PathVariable String id) throws Exception {
		return projectService.delete(Long.valueOf(id));
	}

}
