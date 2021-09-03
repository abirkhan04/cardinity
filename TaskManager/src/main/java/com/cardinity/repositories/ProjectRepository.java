package com.cardinity.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cardinity.pojo.Project;
import com.cardinity.pojo.User;

public interface ProjectRepository<T> extends PagingAndSortingRepository<Project, Long> {
	List<Project> findByUser(User user);

	Optional<Project> findByUserAndId(User user, Long id);
}
