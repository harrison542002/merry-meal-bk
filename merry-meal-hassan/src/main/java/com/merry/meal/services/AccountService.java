package com.merry.meal.services;

import java.util.List;

import com.merry.meal.payload.AccountDto;

public interface AccountService {

	// get all accounts
	List<AccountDto> getAllAccountDto();

	// get particular account info
	AccountDto getAccountDto(String token);

}
