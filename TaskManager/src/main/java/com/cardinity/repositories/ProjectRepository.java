package com.cardinity.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cardinity.pojo.Project;
import com.cardinity.pojo.User;

public interface ProjectRepository<T> extends PagingAndSortingRepository<Project, Long> {
	List<Project> findByUser(User user);
}
