package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import utils.ApiConfig;

public abstract class BaseApiTest {
    @BeforeAll
    static void setUpBaseUri() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
    }
}