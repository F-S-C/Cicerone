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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.Objects;

import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ItineraryReviewTest {

    @Test
    public void loadFromJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"reviewed_itinerary\": { \"itinerary_code\": 19, \"username\": { \"username\": \"utente2\", \"tax_code\": \"ut200000\", \"name\": \"Utente2\", \"surname\": \"Utente2\", \"password\": \"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\", \"email\": \"a.esposito39@studenti.uniba.it\", \"user_type\": \"1\", \"cellphone\": \"1245637897\", \"birth_date\": \"1998-02-06\", \"sex\": \"female\", \"document\": { \"document_number\": \"ut0009\", \"document_type\": \"identity card\", \"expiry_date\": \"2019-09-27\" }, \"languages\": [] }, \"title\": \"Prova notifiche\", \"description\": \"Proviamo le notifiche.\", \"beginning_date\": \"2019-09-27\", \"ending_date\": \"2019-10-31\", \"end_reservations_date\": \"2019-10-29\", \"maximum_participants_number\": 100, \"minimum_participants_number\": 1, \"location\": \"ovunque\", \"repetitions_per_day\": 5, \"duration\": \"01:00:00\", \"image_url\": \"https:\\/\\/fscgroup.ddns.net\\/images\\/1569368557-5d8aa9ed1ca0c.jpg\", \"full_price\": \"10.00\", \"reduced_price\": \"0.00\" }, \"username\": { \"username\": \"test\", \"tax_code\": \"IT0000000\", \"name\": \"test\", \"surname\": \"testsurname\", \"password\": \"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\", \"email\": \"graziano.montanaro98@gmail.com\", \"user_type\": \"1\", \"cellphone\": \"0999561111\", \"birth_date\": \"1998-09-28\", \"sex\": \"male\", \"document\": { \"document_number\": \"test0000\", \"document_type\": \"Identity Card\", \"expiry_date\": \"2022-02-25\" }, \"languages\": [] }, \"feedback\": 4, \"description\": \"hole\"}");

        final String user = jsonObject.getJSONObject("username").getString("username");
        final int feedback = jsonObject.getInt("feedback");
        final String description = jsonObject.getString("description");
        final int itinerary = jsonObject.getJSONObject("reviewed_itinerary").getInt("itinerary_code");

        ItineraryReview itineraryReview = new ItineraryReview(jsonObject);

        assertEquals("field wasn't retrieved properly", user, itineraryReview.getAuthor().getUsername());
        assertEquals("field wasn't retrieved properly", feedback, itineraryReview.getFeedback());
        assertEquals("field wasn't retrieved properly", description, itineraryReview.getDescription());
        assertEquals("field wasn't retrieved properly", itinerary, itineraryReview.getReviewedItinerary().getCode());


    }

    @Test
    public void getReviewedItinerary() {
        final User user = new User();
        final Itinerary itinerary = new Itinerary();
        final ItineraryReview.Builder itineraryReview = new ItineraryReview.Builder(user, itinerary);

        //when
        final Itinerary resultItinerary = itineraryReview.build().getReviewedItinerary();

        //then
        assertEquals("field wasn't retrieved properly", resultItinerary, itinerary);
    }

    @Test
    public void toJSONObject() throws JSONException {

        JSONObject jsonObject = new JSONObject("{ \"reviewed_itinerary\": { \"itinerary_code\": 19, \"username\": { \"username\": \"utente2\", \"tax_code\": \"ut200000\", \"name\": \"Utente2\", \"surname\": \"Utente2\", \"password\": \"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\", \"email\": \"a.esposito39@studenti.uniba.it\", \"user_type\": \"1\", \"cellphone\": \"1245637897\", \"birth_date\": \"1998-02-06\", \"sex\": \"female\", \"document\": { \"document_number\": \"ut0009\", \"document_type\": \"identity card\", \"expiry_date\": \"2019-09-27\" }, \"languages\": [] }, \"title\": \"Prova notifiche\", \"description\": \"Proviamo le notifiche.\", \"beginning_date\": \"2019-09-27\", \"ending_date\": \"2019-10-31\", \"end_reservations_date\": \"2019-10-29\", \"maximum_participants_number\": 100, \"minimum_participants_number\": 1, \"location\": \"ovunque\", \"repetitions_per_day\": 5, \"duration\": \"01:00:00\", \"image_url\": \"https:\\/\\/fscgroup.ddns.net\\/images\\/1569368557-5d8aa9ed1ca0c.jpg\", \"full_price\": \"10.00\", \"reduced_price\": \"0.00\" }, \"username\": { \"username\": \"test\", \"tax_code\": \"IT0000000\", \"name\": \"test\", \"surname\": \"testsurname\", \"password\": \"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\", \"email\": \"graziano.montanaro98@gmail.com\", \"user_type\": \"1\", \"cellphone\": \"0999561111\", \"birth_date\": \"1998-09-28\", \"sex\": \"male\", \"document\": { \"document_number\": \"test0000\", \"document_type\": \"Identity Card\", \"expiry_date\": \"2022-02-25\" }, \"languages\": [] }, \"feedback\": 4, \"description\": \"hole\" }");

        ItineraryReview itineraryReview = new ItineraryReview(jsonObject);
        JSONObject obj = itineraryReview.toJSONObject();

        assertEquals("Fields didn't match", new Itinerary(obj.getJSONObject("reviewed_itinerary")), new Itinerary(jsonObject.getJSONObject("reviewed_itinerary")));
        assertEquals("Fields didn't match",  new User(obj.getJSONObject("username")), new User(jsonObject.getJSONObject("username")));
        assertEquals("Fields didn't match", obj.getInt("feedback"), jsonObject.getInt("feedback"));
        assertEquals("Fields didn't match", obj.getString("description"), jsonObject.getString("description"));
    }

    @Test
    public void setFeedback() throws NoSuchFieldException, IllegalAccessException, JSONException {
        JSONObject jsonObject = new JSONObject("{\"reviewed_itinerary\": { \"itinerary_code\": 19, \"username\": { \"username\": \"utente2\", \"tax_code\": \"ut200000\", \"name\": \"Utente2\", \"surname\": \"Utente2\", \"password\": \"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\", \"email\": \"a.esposito39@studenti.uniba.it\", \"user_type\": \"1\", \"cellphone\": \"1245637897\", \"birth_date\": \"1998-02-06\", \"sex\": \"female\", \"document\": { \"document_number\": \"ut0009\", \"document_type\": \"identity card\", \"expiry_date\": \"2019-09-27\" }, \"languages\": [] }, \"title\": \"Prova notifiche\", \"description\": \"Proviamo le notifiche.\", \"beginning_date\": \"2019-09-27\", \"ending_date\": \"2019-10-31\", \"end_reservations_date\": \"2019-10-29\", \"maximum_participants_number\": 100, \"minimum_participants_number\": 1, \"location\": \"ovunque\", \"repetitions_per_day\": 5, \"duration\": \"01:00:00\", \"image_url\": \"https:\\/\\/fscgroup.ddns.net\\/images\\/1569368557-5d8aa9ed1ca0c.jpg\", \"full_price\": \"10.00\", \"reduced_price\": \"0.00\" }, \"username\": { \"username\": \"test\", \"tax_code\": \"IT0000000\", \"name\": \"test\", \"surname\": \"testsurname\", \"password\": \"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\", \"email\": \"graziano.montanaro98@gmail.com\", \"user_type\": \"1\", \"cellphone\": \"0999561111\", \"birth_date\": \"1998-09-28\", \"sex\": \"male\", \"document\": { \"document_number\": \"test0000\", \"document_type\": \"Identity Card\", \"expiry_date\": \"2022-02-25\" }, \"languages\": [] }, \"feedback\": 4, \"description\": \"hole\"}");
        //given
        final ItineraryReview itineraryReview = new ItineraryReview(jsonObject);

        //when
        itineraryReview.setFeedback(4);

        //then
        final Field field = Objects.requireNonNull(itineraryReview.getClass().getSuperclass()).getDeclaredField("feedback");
        field.setAccessible(true);
        Assert.assertEquals("field wasn't retrieved properly", field.get(itineraryReview), 4);
    }

    @Test
    public void setDescription() throws JSONException, NoSuchFieldException, IllegalAccessException {
        JSONObject jsonObject = new JSONObject("{\"reviewed_itinerary\": { \"itinerary_code\": 19, \"username\": { \"username\": \"utente2\", \"tax_code\": \"ut200000\", \"name\": \"Utente2\", \"surname\": \"Utente2\", \"password\": \"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\", \"email\": \"a.esposito39@studenti.uniba.it\", \"user_type\": \"1\", \"cellphone\": \"1245637897\", \"birth_date\": \"1998-02-06\", \"sex\": \"female\", \"document\": { \"document_number\": \"ut0009\", \"document_type\": \"identity card\", \"expiry_date\": \"2019-09-27\" }, \"languages\": [] }, \"title\": \"Prova notifiche\", \"description\": \"Proviamo le notifiche.\", \"beginning_date\": \"2019-09-27\", \"ending_date\": \"2019-10-31\", \"end_reservations_date\": \"2019-10-29\", \"maximum_participants_number\": 100, \"minimum_participants_number\": 1, \"location\": \"ovunque\", \"repetitions_per_day\": 5, \"duration\": \"01:00:00\", \"image_url\": \"https:\\/\\/fscgroup.ddns.net\\/images\\/1569368557-5d8aa9ed1ca0c.jpg\", \"full_price\": \"10.00\", \"reduced_price\": \"0.00\" }, \"username\": { \"username\": \"test\", \"tax_code\": \"IT0000000\", \"name\": \"test\", \"surname\": \"testsurname\", \"password\": \"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\", \"email\": \"graziano.montanaro98@gmail.com\", \"user_type\": \"1\", \"cellphone\": \"0999561111\", \"birth_date\": \"1998-09-28\", \"sex\": \"male\", \"document\": { \"document_number\": \"test0000\", \"document_type\": \"Identity Card\", \"expiry_date\": \"2022-02-25\" }, \"languages\": [] }, \"feedback\": 4, \"description\": \"hole\"}");
        //given
        final ItineraryReview itineraryReview = new ItineraryReview(jsonObject);

        //when
        itineraryReview.setDescription("test_description");

        //then
        final Field field = Objects.requireNonNull(itineraryReview.getClass().getSuperclass()).getDeclaredField("description");
        field.setAccessible(true);
        Assert.assertEquals("field wasn't retrieved properly", field.get(itineraryReview), "test_description");
    }
}