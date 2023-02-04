package com.merry.meal.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merry.meal.data.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long>{
	public Optional<Account> findByEmail(String email);
}
