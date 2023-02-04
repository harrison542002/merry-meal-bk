package com.merry.meal.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merry.meal.data.Account;
import com.merry.meal.exceptions.ResourceNotFoundException;
import com.merry.meal.payload.AccountDto;
import com.merry.meal.repo.AccountRepo;
import com.merry.meal.services.AccountService;
import com.merry.meal.utils.JwtUtils;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public List<AccountDto> getAllAccountDto() {

		List<Account> allAccounts = this.accountRepo.findAll();

		List<AccountDto> allAccountDtos = allAccounts.stream()
				.map((account) -> this.modelMapper.map(account, AccountDto.class)).collect(Collectors.toList());

		return allAccountDtos;
	}

	@Override
	public AccountDto getAccountDto(String token) {

		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("account", "credentials", email));

		return this.modelMapper.map(account, AccountDto.class);
	}

}
