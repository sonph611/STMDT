package com.API.service.saler;

import com.API.model.Account;

public class UserFilter {
	private Account account;
	
	public UserFilter(Account a) {
		account=a;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
