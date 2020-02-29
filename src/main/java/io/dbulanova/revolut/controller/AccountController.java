package io.dbulanova.revolut.controller;


import io.dbulanova.revolut.dto.AccountDto;
import io.dbulanova.revolut.service.AccountService;
import io.dbulanova.revolut.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.jooby.Results;
import org.jooby.mvc.Body;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.Result;

import javax.inject.Inject;
import java.util.List;

@Path("/accounts")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
/*
  The jooby's default exception handler is used, which maps
  IllegalArgumentException -> 400
  FileNotFound             -> 404
  Exception                -> 500
*/
public class AccountController {

    private final TransferService transferService;
    private final AccountService accountService;

    @GET
    public List<AccountDto> getAll() {
        return accountService.getAll();
    }

    @Path("/{id}")
    @GET
    public AccountDto getOne(String id) {
        return accountService.getOne(id);
    }

    @Path("/transfer")
    @POST
    public Result makeTransfer(@Body TransferRequest transferRequest) {
        transferService.transfer(transferRequest.getAccountFrom(), transferRequest.getAccountTo(), transferRequest.getAmount());
        return Results.ok();
    }
}
