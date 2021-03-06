/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fsc.cicerone.model;

import com.fsc.cicerone.app_connector.ConnectorConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricTestRunner.class)
public class ItineraryTest {

    @Test
    public void loadFromJSONObject() throws JSONException {
        final String string = "{\"itinerary_code\":9,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]},\"title\":\"Pausa con Pizzutillo\",\"description\":\"Facciamo una pausa\",\"beginning_date\":\"2019-10-03\",\"ending_date\":\"2019-10-31\",\"end_reservations_date\":\"2019-10-31\",\"maximum_participants_number\":15,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"}";

        final JSONObject jsonObject = new JSONObject(string);
        final Itinerary itinerary = new Itinerary(jsonObject);

        final SimpleDateFormat output = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US);

        final String TAG = "Fields not setted properly";
        assertEquals(TAG, itinerary.getTitle(), jsonObject.getString("title"));
        assertEquals(TAG, itinerary.getCicerone(), new User(jsonObject.getJSONObject("username")));
        assertEquals(TAG, itinerary.getCode(), jsonObject.getInt("itinerary_code"));
        assertEquals(TAG, itinerary.getMaxParticipants(), jsonObject.getInt("maximum_participants_number"));
        assertEquals(TAG, itinerary.getMinParticipants(), jsonObject.getInt("minimum_participants_number"));
        assertEquals(TAG, itinerary.getDescription(), jsonObject.getString("description"));
        assertEquals(itinerary.getFullPrice(), jsonObject.getDouble("full_price"), 0f);
        assertEquals(itinerary.getReducedPrice(), jsonObject.getDouble("reduced_price"), 0f);
        assertEquals(TAG, itinerary.getImageUrl(), jsonObject.getString("image_url"));
        assertEquals(TAG, itinerary.getLocation(), jsonObject.getString("location"));
        assertEquals(TAG, itinerary.getRepetitions(), jsonObject.getInt("repetitions_per_day"));
        assertEquals(TAG, itinerary.getDuration(), jsonObject.getString("duration"));
        assertEquals(TAG, output.format(itinerary.getBeginningDate()), jsonObject.getString("beginning_date"));
        assertEquals(TAG, output.format(itinerary.getEndingDate()), jsonObject.getString("ending_date"));
        assertEquals(TAG, output.format(itinerary.getReservationDate()), jsonObject.getString("end_reservations_date"));
    }

    @Test
    public void getCode() throws NoSuchFieldException, IllegalAccessException {

        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("code");
        field.setAccessible(true);
        field.set(itinerary, 5);

        //when
        final int result = itinerary.getCode();

        //then
        assertEquals("field wasn't retrieved properly", result, 5);
    }

    @Test
    public void getCicerone() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]}";
        final User cicerone = new User(string);
        final Field field = itinerary.getClass().getDeclaredField("cicerone");
        field.setAccessible(true);
        field.set(itinerary, cicerone);

        //when
        final User result = itinerary.getCicerone();

        //then
        assertEquals("field wasn't retrieved properly", result, cicerone);
    }

    @Test
    public void setCicerone() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]}";
        final User cicerone = new User(string);

        //when
        itinerary.setCicerone(cicerone);

        //then
        final Field field = itinerary.getClass().getDeclaredField("cicerone");
        field.setAccessible(true);
        final User itineraryCicerone = (User) field.get(itinerary);
        assertEquals("field wasn't retrieved properly", itineraryCicerone, cicerone);
    }

    @Test
    public void setCode() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setCode(5);

        //then
        final Field field = itinerary.getClass().getDeclaredField("code");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), 5);
    }

    @Test
    public void getTitle() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("title");
        field.setAccessible(true);
        field.set(itinerary, "title_test");

        //when
        final String result = itinerary.getTitle();

        //then
        assertEquals("field wasn't retrieved properly", result, "title_test");
    }

    @Test
    public void setTitle() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setTitle("title_test");

        //then
        final Field field = itinerary.getClass().getDeclaredField("title");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), "title_test");
    }

    @Test
    public void getDescription() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("description");
        field.setAccessible(true);
        field.set(itinerary, "description_test");

        //when
        final String result = itinerary.getDescription();

        //then
        assertEquals("field wasn't retrieved properly", result, "description_test");
    }

    @Test
    public void setDescription() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setDescription("description_test");

        //then
        final Field field = itinerary.getClass().getDeclaredField("description");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), "description_test");
    }

    @Test
    public void getBeginningDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("beginningDate");
        field.setAccessible(true);
        field.set(itinerary, theDate);

        //when
        final Date result = itinerary.getBeginningDate();

        //then
        assertEquals("field wasn't retrieved properly", result, theDate);
    }

    @Test
    public void setBeginningDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setBeginningDate(theDate);

        //then
        final Field field = itinerary.getClass().getDeclaredField("beginningDate");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(itinerary), theDate);
    }

    @Test
    public void getEndingDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("endingDate");
        field.setAccessible(true);
        field.set(itinerary, theDate);

        //when
        final Date result = itinerary.getEndingDate();

        //then
        assertEquals("field wasn't retrieved properly", result, theDate);
    }

    @Test
    public void setEndingDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setEndingDate(theDate);

        //then
        final Field field = itinerary.getClass().getDeclaredField("endingDate");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(itinerary), theDate);
    }

    @Test
    public void getReservationDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("reservationDate");
        field.setAccessible(true);
        field.set(itinerary, theDate);

        //when
        final Date result = itinerary.getReservationDate();

        //then
        assertEquals("field wasn't retrieved properly", result, theDate);
    }

    @Test
    public void setReservationDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setReservationDate(theDate);

        //then
        final Field field = itinerary.getClass().getDeclaredField("reservationDate");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(itinerary), theDate);
    }

    @Test
    public void getLocation() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("location");
        field.setAccessible(true);
        field.set(itinerary, "location_test");

        //when
        final String result = itinerary.getLocation();

        //then
        assertEquals("field wasn't retrieved properly", result, "location_test");
    }

    @Test
    public void setLocation() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setLocation("location_test");

        //then
        final Field field = itinerary.getClass().getDeclaredField("location");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), "location_test");
    }

    @Test
    public void getMinParticipants() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("minParticipants");
        field.setAccessible(true);
        field.set(itinerary, 1);

        //when
        final int result = itinerary.getMinParticipants();

        //then
        assertEquals("field wasn't retrieved properly", result, 1);
    }

    @Test
    public void setMinParticipants() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setMinParticipants(1);

        //then
        final Field field = itinerary.getClass().getDeclaredField("minParticipants");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), 1);
    }

    @Test
    public void getMaxParticipants() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("maxParticipants");
        field.setAccessible(true);
        field.set(itinerary, 1);

        //when
        final int result = itinerary.getMaxParticipants();

        //then
        assertEquals("field wasn't retrieved properly", result, 1);
    }

    @Test
    public void setMaxParticipants() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setMaxParticipants(10);

        //then
        final Field field = itinerary.getClass().getDeclaredField("maxParticipants");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), 10);
    }

    @Test
    public void getRepetitions() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("repetitions");
        field.setAccessible(true);
        field.set(itinerary, 1);

        //when
        final int result = itinerary.getRepetitions();

        //then
        assertEquals("field wasn't retrieved properly", result, 1);
    }

    @Test
    public void setRepetitions()  throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setRepetitions(1);

        //then
        final Field field = itinerary.getClass().getDeclaredField("repetitions");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), 1);
    }

    @Test
    public void getDuration() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("duration");
        field.setAccessible(true);
        field.set(itinerary, "duration_test");

        //when
        final String result = itinerary.getDuration();

        //then
        assertEquals("field wasn't retrieved properly", result, "duration_test");
    }

    @Test
    public void setDuration()  throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setDuration("duration_test");

        //then
        final Field field = itinerary.getClass().getDeclaredField("duration");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), "duration_test");
    }

    @Test
    public void getFullPrice() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("fullPrice");
        field.setAccessible(true);
        field.set(itinerary, 1.5f);

        //when
        final float result = itinerary.getFullPrice();

        //then
        assertEquals("field wasn't retrieved properly", result, 1.5f,0);
    }

    @Test
    public void setFullPrice()  throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setFullPrice(1.5f);

        //then
        final Field field = itinerary.getClass().getDeclaredField("fullPrice");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), 1.5f);
    }

    @Test
    public void getReducedPrice() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("reducedPrice");
        field.setAccessible(true);
        field.set(itinerary, 1.5f);

        //when
        final float result = itinerary.getReducedPrice();

        //then
        assertEquals("field wasn't retrieved properly", result, 1.5f,0);
    }

    @Test
    public void setReducedPrice() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setReducedPrice(1.5f);

        //then
        final Field field = itinerary.getClass().getDeclaredField("reducedPrice");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), 1.5f);
    }

    @Test
    public void getImageUrl() throws NoSuchFieldException, IllegalAccessException {
        final Itinerary itinerary = new Itinerary();
        final Field field = itinerary.getClass().getDeclaredField("imageUrl");
        field.setAccessible(true);
        field.set(itinerary, "https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg");

        //when
        final String result = itinerary.getImageUrl();

        //then
        assertEquals("field wasn't retrieved properly", result, "https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg");
    }

    @Test
    public void setImageUrl()  throws NoSuchFieldException, IllegalAccessException {
        //given
        final Itinerary itinerary = new Itinerary();

        //when
        itinerary.setImageUrl("https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg");

        //then
        final Field field = itinerary.getClass().getDeclaredField("imageUrl");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(itinerary), "https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg");
    }

    @Test
    public void toJSONObject() throws JSONException, ParseException {

        JSONObject jsonObject = new JSONObject("{\"itinerary_code\":9,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]},\"title\":\"Pausa con Pizzutillo\",\"description\":\"Facciamo una pausa\",\"beginning_date\":\"2019-10-03\",\"ending_date\":\"2019-10-31\",\"end_reservations_date\":\"2019-10-31\",\"maximum_participants_number\":15,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"}");

        Itinerary itinerary = new Itinerary(jsonObject);

        JSONObject obj = itinerary.toJSONObject();

        assertEquals("Fields didn't match", obj.getInt("itinerary_code"), jsonObject.getInt("itinerary_code"));
        assertEquals("Fields didn't match", obj.getString("title"), jsonObject.getString("title"));
        assertEquals("Fields didn't match", new User(obj.getJSONObject("username")), new User(jsonObject.getJSONObject("username")));
        assertEquals("Fields didn't match", obj.getString("description"), jsonObject.getString("description"));
        assertEquals("Fields didn't match", new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("beginning_date")), new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("beginning_date")));
        assertEquals("Fields didn't match",  new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("ending_date")), new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("ending_date")));
        assertEquals("Fields didn't match", new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("end_reservations_date")) , new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("end_reservations_date")));
        assertEquals("Fields didn't match", obj.getString("duration"), jsonObject.getString("duration"));
        assertEquals("Fields didn't match", obj.getString("location"), jsonObject.getString("location"));
        assertEquals("Fields didn't match", obj.getInt("minimum_participants_number"), jsonObject.getInt("minimum_participants_number"));
        assertEquals("Fields didn't match", obj.getInt("maximum_participants_number"), jsonObject.getInt("maximum_participants_number"));
        assertEquals("Fields didn't match", obj.getInt("repetitions_per_day"), jsonObject.getInt("repetitions_per_day"));
        assertEquals("Fields didn't match", Float.parseFloat(obj.getString("full_price")),Float.parseFloat(jsonObject.getString("full_price")),0);
        assertEquals("Fields didn't match", Float.parseFloat(obj.getString("reduced_price")), Float.parseFloat(jsonObject.getString("reduced_price")),0);
        assertEquals("Fields didn't match", obj.getString("image_url"), jsonObject.getString("image_url"));
    }

    @Test
    public void equals1() {
        final String string = "{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"}";
        final String string2 = "{\"itinerary_code\":6,\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"title\":\"dfefef\",\"description\":\"feffef\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-21\",\"maximum_participants_number\":1,\"minimum_participants_number\":1,\"location\":\"Lizzano\",\"repetitions_per_day\":1,\"duration\":\"00:04:00\",\"image_url\":\"https:\\/\\/media.internazionale.it\\/images\\/2019\\/02\\/27\\/148022-hd.jpg\",\"full_price\":\"1.00\",\"reduced_price\":\"1.00\"}";

        Itinerary i1 = new Itinerary(string);
        Itinerary i2 = new Itinerary(string);
        Itinerary i3 = new Itinerary(string2);

        assertEquals("field wasn't retrieved properly", i1, i2);
        assertNotEquals("field wasn't retrieved properly", i1, i3);
        assertEquals("field wasn't retrieved properly", i1, i1);
    }

    @Test
    public void hashCode1() {
        final String string = "{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"}";

        Itinerary i1 = new Itinerary(string);
        Itinerary i2 = new Itinerary(string);

        assertEquals("field wasn't retrieved properly", i1.hashCode(), i2.hashCode());
        assertEquals("field wasn't retrieved properly", i1.hashCode(), i1.hashCode());
    }
}