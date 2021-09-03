package com.cardinity.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cardinity.pojo.Task;
import com.cardinity.pojo.User;

public interface TaskRepository<T> extends PagingAndSortingRepository<Task, Long> {
	List<Task> findByUser(User user);
}
