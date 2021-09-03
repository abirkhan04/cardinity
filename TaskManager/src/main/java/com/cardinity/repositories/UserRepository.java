package com.cardinity.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cardinity.pojo.User;

public interface UserRepository<T> extends PagingAndSortingRepository<User, Long>{
	User findByUsername(String username);
}
