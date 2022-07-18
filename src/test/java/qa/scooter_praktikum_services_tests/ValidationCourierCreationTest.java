package qa.scooter_praktikum_services_tests;

import api.CourierClient;
import api.Courier;
import api.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidationCourierCreationTest {

    private CourierClient courierClient;
    private int courierId;

    private Courier courier;
    private int expectedStatusCode;
    private String expectedErrorMessage;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }
    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    public ValidationCourierCreationTest(Courier courier, int expectedStatusCode, String expectedErrorMessage) {
        this.courier = courier;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getStatusCodeCourierCreation() {
        return new Object[][]{
                {Courier.getCourierWithLoginOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getCourierWithPasswordOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getCourierWithFirstNameOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getCourierWithoutFirstNameOnly(), 201, null}
        };
    }

    @Test
    @DisplayName("Checking field validation for courier creation")
    public void checkFieldsValidationOfCourierCreation() {
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        if (statusCode == 201) {
            courierId = courierClient.loginCourier(new CourierCredentials(courier.login, courier.password)).extract().path("id");
            assertThat("Неверный ID курьера", courierId, notNullValue());
        }

        assertEquals("Некорректный код статуса", expectedStatusCode, statusCode);
        assertEquals("Некорректное сообщение об ошибке", expectedErrorMessage, errorMessage);
    }

}
