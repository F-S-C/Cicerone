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
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ItineraryTest {

    @Test
    public void loadFromJSONObject() {
      /*  final String string = "{\"itinerary_code\":9,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]},\"title\":\"Pausa con Pizzutillo\",\"description\":\"Facciamo una pausa\",\"beginning_date\":\"2019-10-03\",\"ending_date\":\"2019-10-31\",\"end_reservations_date\":\"2019-10-31\",\"maximum_participants_number\":15,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"}";
        try {
            JSONObject object = new JSONObject(string);
            assertEquals("Field wasn't retrieved properly", object.toString(), new Itinerary(object).toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
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
    public void getCicerone() throws NoSuchFieldException, IllegalAccessException{
        final Itinerary itinerary = new Itinerary();
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]}";
        final User cicerone = new User(string);
        final Field field = itinerary.getClass().getDeclaredField("cicerone");
        field.setAccessible(true);
        field.set(itinerary, string);

        //when
        final User result = itinerary.getCicerone();

        //then
        assertEquals("field wasn't retrieved properly", result, cicerone);
    }

    @Test
    public void setCicerone() {
    }

    @Test
    public void setCode() {
    }

    @Test
    public void getTitle() {
    }

    @Test
    public void setTitle() {
    }

    @Test
    public void getDescription() {
    }

    @Test
    public void setDescription() {
    }

    @Test
    public void getBeginningDate() {
    }

    @Test
    public void setBeginningDate() {
    }

    @Test
    public void getEndingDate() {
    }

    @Test
    public void setEndingDate() {
    }

    @Test
    public void getReservationDate() {
    }

    @Test
    public void setReservationDate() {
    }

    @Test
    public void getLocation() {
    }

    @Test
    public void setLocation() {
    }

    @Test
    public void getMinParticipants() {
    }

    @Test
    public void setMinParticipants() {
    }

    @Test
    public void getMaxParticipants() {
    }

    @Test
    public void setMaxParticipants() {
    }

    @Test
    public void getRepetitions() {
    }

    @Test
    public void setRepetitions() {
    }

    @Test
    public void getDuration() {
    }

    @Test
    public void setDuration() {
    }

    @Test
    public void getFullPrice() {
    }

    @Test
    public void setFullPrice() {
    }

    @Test
    public void getReducedPrice() {
    }

    @Test
    public void setReducedPrice() {
    }

    @Test
    public void getImageUrl() {
    }

    @Test
    public void setImageUrl() {
    }

    @Test
    public void toJSONObject() {
    }
}