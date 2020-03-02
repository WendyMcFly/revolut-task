package io.dbulanova.revolut;

import io.dbulanova.revolut.controller.AccountController;
import io.dbulanova.revolut.repository.AccountRepository;
import io.dbulanova.revolut.repository.AccountRepositoryImpl;
import io.dbulanova.revolut.service.*;
import io.dbulanova.revolut.service.exception.AccountNotFoundException;
import io.dbulanova.revolut.service.exception.InvalidTransferException;
import io.dbulanova.revolut.service.exception.NotEnoughMoneyException;
import lombok.extern.slf4j.Slf4j;
import org.jooby.Err;
import org.jooby.Jooby;
import org.jooby.json.Jackson;

@Slf4j
public class App extends Jooby {

    {
        use(new Jackson());

        //A module with domain logic
        use((env, conf, binder) -> {
            binder.bind(AccountRepository.class).to(AccountRepositoryImpl.class);
            binder.bind(AccountTransactionService.class).to(AccountTransactionServiceImpl.class);
            binder.bind(TransferService.class).to(TransferServiceImpl.class);
            binder.bind(AccountDtoService.class).to(AccountDtoServiceImpl.class);
            binder.bind(CurrencyRatesProvider.class).to(CurrencyRatesProviderImpl.class);
        });

        //Actual routes
        use(AccountController.class);

        //Populate the repository with test accounts!
        onStart(reg -> TestAccountsPopulation.populateAccounts(reg.require(AccountRepository.class)));
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }
}
