package com.cardinity.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;

import com.cardinity.pojo.Project;
import com.cardinity.pojo.User;
import com.cardinity.repositories.ProjectRepository;
import com.cardinity.repositories.UserRepository;

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
		Optional<Project> projectOptional = projectRepository.findById(id);
		projectRepository.deleteById(id);
		return projectOptional.get();
	}

	public PagedListHolder<Project> getPagedProjects(String username, Integer pageNumber, Integer pageSize, Optional<String> search,
			Optional<String> sortBy) {
		List<Project> projects = null;
		User user = null;
		if(username!=null) user = userRepository.findByUsername(username); 
		PagedListHolder<Project> page = new PagedListHolder<Project>();
		page.setPageSize(pageSize);
		if(user!=null) projects = (List<Project>) projectRepository.findByUser(user);
		else projects = (List<Project>) projectRepository.findAll();
		if (search.isPresent() && !search.get().isEmpty()) {
			projects = projects.stream().filter((project) -> project.getName().contains(search.get()))
					.collect(Collectors.toList());
		}
		if (pageNumber * pageSize > projects.size() + pageSize) {
			pageNumber = 0;
		}
		page.setPage(pageNumber);
		if (sortBy.isPresent()) {
			PropertyComparator.sort(projects, new MutableSortDefinition(sortBy.get(), true, true));
		}
		page.setSource(projects);
		return page;
	}

}
