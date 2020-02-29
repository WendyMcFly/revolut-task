package io.dbulanova.revolut;

import io.dbulanova.revolut.domain.Account;
import io.dbulanova.revolut.repository.AccountRepository;
import io.dbulanova.revolut.repository.AccountRepositoryImpl;
import io.restassured.RestAssured;
import org.jooby.test.JoobyRule;
import org.junit.*;

import java.math.BigDecimal;

import static io.restassured.RestAssured.when;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.*;

public class AppTest {

    private static final BigDecimal FIRST_ACCOUNT_BALANCE = BigDecimal.ZERO;
    private static final BigDecimal SECOND_ACCOUNT_BALANCE = BigDecimal.ONE;
    private static final BigDecimal THIRD_ACCOUNT_BALANCE = BigDecimal.valueOf(100500L);
    private static final String FIRST_ACCOUNT_NUMBER = "A1";
    private static final String SECOND_ACCOUNT_NUMBER = "A2";
    private static final String THIRD_ACCOUNT_NUMBER = "A3";

    private static App app = new App();

    @ClassRule
    public static JoobyRule bootstrap = new JoobyRule(app);

    @BeforeClass
    public static void config() {
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
    }

    @Before
    public void clearAccRepo() {
        app.require(AccountRepositoryImpl.class).clear();
    }

    @Test
    public void testGetAccountsSize() {
        givenAccountsPopulated();

        when()
                .get("/accounts")
                .then()
                .body("$", hasSize(3));
    }

    @Test
    @Ignore
    public void testGetAccount() {
        givenAccountsPopulated();

        when()
                .get("/accounts/A1")
                .then()
                .body("accountNumber", is(FIRST_ACCOUNT_NUMBER))
                .body("accountBalance", comparesEqualTo(FIRST_ACCOUNT_BALANCE));
    }

    @Test
    public void testGetMissingAccount() {
        givenAccountsPopulated();

        when()
                .get("/accounts/A4")
                .then()
                .statusCode(404);
    }


    @Test
    public void testInvalidUrl() {
        when()
                .get("/something")
                .then()
                .statusCode(404);
    }


    private void givenAccountsPopulated() {
        createMockAccount(FIRST_ACCOUNT_NUMBER, FIRST_ACCOUNT_BALANCE);
        createMockAccount(SECOND_ACCOUNT_NUMBER, SECOND_ACCOUNT_BALANCE);
        createMockAccount(THIRD_ACCOUNT_NUMBER, THIRD_ACCOUNT_BALANCE);
    }


    private void createMockAccount(String accountNumber, BigDecimal balance) {
        app.require(AccountRepository.class).save(new Account(accountNumber, balance));
    }
}