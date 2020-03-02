package io.dbulanova.revolut;

import io.dbulanova.revolut.controller.TransferRequest;
import io.dbulanova.revolut.domain.Account;
import io.dbulanova.revolut.repository.AccountRepository;
import io.dbulanova.revolut.repository.AccountRepositoryImpl;
import io.restassured.RestAssured;
import org.jooby.test.JoobyRule;
import org.junit.*;

import java.math.BigDecimal;
import java.util.Currency;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.*;

public class AppTest {

    private static final BigDecimal FIRST_ACCOUNT_BALANCE = new BigDecimal("100000.50");
    private static final BigDecimal SECOND_ACCOUNT_BALANCE = new BigDecimal("200000.50");
    private static final BigDecimal AMOUNT = new BigDecimal("400.50");
    private static final String FIRST_ACCOUNT_NUMBER = "A1";
    private static final String SECOND_ACCOUNT_NUMBER = "A2";

    private static App app = new App();

    @ClassRule
    public static JoobyRule bootstrap = new JoobyRule(app);

    @BeforeClass
    public static void config() {
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
    }

    @Before
    public void clearAccountRepository() {
        app.require(AccountRepositoryImpl.class).clear();
    }

    @Test
    public void testGetAccountsSize() {
        givenAccountsPopulated();

        when()
                .get("/accounts")
                .then()
                .body("accounts", hasSize(2));
    }

    @Test
    public void testGetAccount() {
        givenAccountsPopulated();

        when()
                .get("/accounts/A1")
                .then()
                .body("number", is(FIRST_ACCOUNT_NUMBER))
                .body("balance", comparesEqualTo(FIRST_ACCOUNT_BALANCE));
    }

    @Test
    public void testValidTransfer() {
        givenAccountsPopulated();

        given().
                when()
                .body(new TransferRequest(FIRST_ACCOUNT_NUMBER, SECOND_ACCOUNT_NUMBER, AMOUNT))
                .contentType("application/json")
                .post("/accounts/transfer")
                .then().statusCode(200);

        when()
                .get("/accounts/A1")
                .then()
                .body("balance", comparesEqualTo(new BigDecimal("99600.00")));

        when()
                .get("/accounts/A2")
                .then()
                .body("balance", comparesEqualTo(new BigDecimal("200401.00")));
    }

    @Test
    public void testInvalidTransferNoAccount() {
        givenAccountsPopulated();

        given().
                when()
                .body(new TransferRequest("A3", SECOND_ACCOUNT_NUMBER, AMOUNT))
                .contentType("application/json")
                .post("/accounts/transfer")
                .then()
                .statusCode(400)
                .body(containsString("Account A3 is not found"));
    }

    @Test
    public void testInvalidTransferInsufficientFunds() {
        givenAccountsPopulated();

        String s = given().
                when()
                .body(new TransferRequest(FIRST_ACCOUNT_NUMBER, SECOND_ACCOUNT_NUMBER, AMOUNT.pow(10)))
                .contentType("application/json")
                .post("/accounts/transfer").asString();


        given().
                when()
                .body(new TransferRequest(FIRST_ACCOUNT_NUMBER, SECOND_ACCOUNT_NUMBER, AMOUNT.pow(10)))
                .contentType("application/json")
                .post("/accounts/transfer")
                .then()
                .statusCode(400)
                .body(containsString("insufficient funds"));

        // make sure no money was transferred
        when()
                .get("/accounts/A1")
                .then()
                .body("balance", comparesEqualTo(FIRST_ACCOUNT_BALANCE));

        when()
                .get("/accounts/A2")
                .then()
                .body("balance", comparesEqualTo(SECOND_ACCOUNT_BALANCE));

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
    }


    private void createMockAccount(String accountNumber, BigDecimal balance) {
        app.require(AccountRepository.class).save(new Account(accountNumber, balance, Currency.getInstance("USD")));
    }
}