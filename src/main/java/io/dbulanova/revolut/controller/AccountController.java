package io.dbulanova.revolut.controller;


import io.dbulanova.revolut.dto.AccountDto;
import io.dbulanova.revolut.service.AccountDtoService;
import io.dbulanova.revolut.service.IdempotencyKeyService;
import io.dbulanova.revolut.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.*;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Path("/accounts")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountController {

    private final TransferService transferService;
    private final AccountDtoService accountDtoService;
    private final IdempotencyKeyService idempotencyKeyService;

    @GET
    public List<AccountDto> getAll() {
        return accountDtoService.getAll();
    }

    @Path("/{id}")
    @GET
    public AccountDto getOne(String id) {
        return accountDtoService.getOne(id);
    }

    @Path("/transfer")
    @POST
    public Result makeTransfer(@Body TransferRequest transferRequest, @Header UUID key) {
        if (idempotencyKeyService.isDuplicate(key)) {
            return Results.with(Status.CONFLICT);
        }
        transferService.transfer(transferRequest.getAccountFrom(), transferRequest.getAccountTo(), transferRequest.getAmount());
        idempotencyKeyService.add(key);
        return Results.ok();
    }
}
