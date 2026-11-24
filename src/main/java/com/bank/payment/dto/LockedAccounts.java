package com.bank.payment.dto;

import com.bank.payment.entity.Account;

public record LockedAccounts(Account source, Account target) {
}
