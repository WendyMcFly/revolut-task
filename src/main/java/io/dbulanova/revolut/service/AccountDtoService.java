package io.dbulanova.revolut.service;

import io.dbulanova.revolut.dto.AccountDto;

import java.util.List;

public interface AccountDtoService {

    List<AccountDto> getAll();

    AccountDto getOne(String id);
}
