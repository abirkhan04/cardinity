package com.cardinity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cardinity.pojo.Project;
import com.cardinity.pojo.User;
import com.cardinity.repositories.ProjectRepository;
import com.cardinity.repositories.UserRepository;
import com.cardinity.util.AppConstants;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository<Project> projectRepository;

	@Autowired
	private UserRepository<User> userRepository;

	public Project save(Project project) {
		return projectRepository.save(project);
	}

	public Project delete(Long id) {
		Optional<Project> project = projectRepository.findByUserAndId(getUser(), id);
		if (project.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.NOT_FOUND);
		projectRepository.deleteById(id);
		return project.get();
	}

	public List<Project> getProjectsForUser(String username) {
		return projectRepository.findByUser(userRepository.findByUsername(username));
	}

	public List<Project> getProjects() {
		return projectRepository.findByUser(getUser());
	}

	private User getUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByUsername(username);
	}

	public Project addUser(Project project) {
		if (project.getUser() == null) {
			project.setUser(getUser());
		}
		return project;
	}

}
