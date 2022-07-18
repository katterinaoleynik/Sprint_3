package qa.scooter_praktikum_services_tests;

import api.CourierClient;
import api.Courier;
import api.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidationOfCourierLoginTest {

    private static CourierClient courierClient = new CourierClient();
    private static Courier courier = Courier.getRandomCourier();
    private int courierId;
    private int expectedStatus;
    private String expectedErrorMessage;
    private CourierCredentials courierCredentials;


    public ValidationOfCourierLoginTest(CourierCredentials courierCredentials, int expectedStatus, String expectedErrorMessage) {
        this.courierCredentials = courierCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getStatusCodeCourierLogin() {
        return new Object[][]{
                {CourierCredentials.loginCourierWithLoginOnly(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.loginCourierWithPasswordOnly(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.getCourierWithNotValidCredentials(), 404, "Учетная запись не найдена"}
        };
    }

    @Test
    @DisplayName("Checking field validation for login courier")
    public void checkFieldsValidationOfCourierLogin() {

        courierClient.createCourier(courier);
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = response.extract().path("id");
        ValidatableResponse errorResponse = new CourierClient().loginCourier(courierCredentials);
        int statusCode = errorResponse.extract().statusCode();
        assertEquals("Некорректный код статуса", expectedStatus, statusCode);
        String errorMessage = errorResponse.extract().path("message");
        assertEquals("Некорректное сообщение об ошибке", expectedErrorMessage, errorMessage);
    }

}
