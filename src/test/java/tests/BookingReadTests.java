package tests;

import model.Booking;
import model.BookingDates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class BookingReadTests extends BaseApiTest {

    @Test
    @DisplayName("AUTO-RB-01 - Consultar booking existente (GET)")
    void consultarBookingExistente() {
        // Creamos el booking primero para no depender de datos que ya existan
        // en el sandbox público (que se resetea cada 10 minutos).
        Booking booking = new Booking(
                "Carlos", "Rojas", 250, true,
                new BookingDates("2026-09-01", "2026-09-10"),
                "Breakfast"
        );

        int bookingId = given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("bookingid");

        given()
                .pathParam("id", bookingId)
                .when()
                .get("/booking/{id}")
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Carlos"))
                .body("lastname", equalTo("Rojas"))
                .body("totalprice", equalTo(250));
    }

    @Test
    @DisplayName("AUTO-RB-02 - Consultar booking inexistente (GET)")
    void consultarBookingInexistente() {
        int idInexistente = 999999999;

        given()
                .pathParam("id", idInexistente)
                .when()
                .get("/booking/{id}")
                .then()
                .statusCode(404);
    }
}