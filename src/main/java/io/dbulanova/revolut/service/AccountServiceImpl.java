package io.dbulanova.revolut.service;

import io.dbulanova.revolut.dto.AccountDto;
import io.dbulanova.revolut.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<AccountDto> getAll() {
        return accountRepository.getAll().stream()
                .map(AccountDto::fromDomain)
                .sorted(Comparator.comparing(AccountDto::getAccountNumber))
                .collect(toList());
    }

    @Override
    public AccountDto getOne(String id) {
        return accountRepository.findByAccountNumber(id)
                .map(AccountDto::fromDomain)
                .orElseThrow(IllegalArgumentException::new);
    }
}
