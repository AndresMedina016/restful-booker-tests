package tests;

import model.Booking;
import model.BookingDates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class BookingCreateTests extends BaseApiTest {

    @Test
    @DisplayName("AUTO-RB-03 - Crear booking válido (POST)")
    void crearBookingValido() {
        Booking booking = new Booking(
                "Maria", "Lopez", 180, false,
                new BookingDates("2026-10-05", "2026-10-08"),
                "Late checkout"
        );

        given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo("Maria"))
                .body("booking.lastname", equalTo("Lopez"))
                .body("booking.totalprice", equalTo(180))
                .body("booking.depositpaid", equalTo(false));
    }

    @Test
    @DisplayName("AUTO-RB-04 - Crear booking con firstname vacío (POST)")
    void crearBookingConFirstnameVacio() {
        String requestBody = """
            {
              "firstname": "",
              "lastname": "Gomez",
              "totalprice": 150,
              "depositpaid": true,
              "bookingdates": { "checkin": "2026-11-01", "checkout": "2026-11-05" },
              "additionalneeds": "None"
            }
            """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("AUTO-RB-05 - Crear booking con totalprice no numérico (POST)")
    void crearBookingConTotalpriceNoNumerico() {
        String requestBody = """
            {
              "firstname": "Pedro",
              "lastname": "Diaz",
              "totalprice": "no-es-un-numero",
              "depositpaid": true,
              "bookingdates": { "checkin": "2026-12-01", "checkout": "2026-12-05" },
              "additionalneeds": "None"
            }
            """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("AUTO-RB-06 - Crear booking con campo adicional 'discount' (POST)")
    void crearBookingConCampoAdicionalDiscount() {
        String requestBody = """
            {
              "firstname": "Lucia",
              "lastname": "Fernandez",
              "totalprice": 300,
              "depositpaid": true,
              "bookingdates": { "checkin": "2027-01-10", "checkout": "2027-01-15" },
              "additionalneeds": "None",
              "discount": 15
            }
            """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo("Lucia"))
                .body("booking.totalprice", equalTo(300));
    }
}