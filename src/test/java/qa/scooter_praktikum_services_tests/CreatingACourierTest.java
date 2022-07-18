package qa.scooter_praktikum_services_tests;


import api.CourierClient;
import api.RestAssuredClient;
import api.Courier;
import api.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class CreatingACourierTest extends RestAssuredClient {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void getSettingForCreatingCourier() {
        courier = Courier.getRandomCourier();
        courierClient = new CourierClient();
    }


    @Test
    @DisplayName("Check successful courier creation")
    public void checkCourierCreation() {
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean isCourierCreated = response.extract().path("ok");
        ValidatableResponse login = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = login.extract().path("id");

        assertTrue("Курьер не создан", isCourierCreated);
        assertThat("Некорректный код статуса", statusCode, equalTo(201));
        assertThat("Неверный ID курьера", courierId, notNullValue());
    }

    @Test
    @DisplayName("Check it's impossible to create two identical couriers")
    public void checkCannotCreateIdenticalCouriers() {
        courierClient.createCourier(courier);
        ValidatableResponse login = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = login.extract().path("id");
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean CourierNotCreated = response.extract().path("message").equals("Этот логин уже используется. Попробуйте другой.");


        assertThat("Некорректный код статуса", statusCode, equalTo(409));
        assertTrue("Создано два одинаковых курьера", CourierNotCreated);
    }

}