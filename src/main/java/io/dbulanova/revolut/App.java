package io.dbulanova.revolut;

import io.dbulanova.revolut.controller.AccountController;
import io.dbulanova.revolut.repository.AccountRepository;
import io.dbulanova.revolut.repository.AccountRepositoryImpl;
import io.dbulanova.revolut.service.*;
import org.jooby.Jooby;
import org.jooby.json.Jackson;

public class App extends Jooby {

    {
        use(new Jackson());

        //A module with domain logic
        use((env, conf, binder) -> {
            binder.bind(AccountRepository.class).to(AccountRepositoryImpl.class);
            binder.bind(AccountTransactionService.class).to(AccountTransactionServiceImpl.class);
            binder.bind(TransferService.class).to(TransferServiceImpl.class);
            binder.bind(AccountService.class).to(AccountServiceImpl.class);
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
