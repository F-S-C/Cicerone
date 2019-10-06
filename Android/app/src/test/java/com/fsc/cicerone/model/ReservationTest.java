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

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ReservationTest {

    @Test
    public void getClient() throws NoSuchFieldException, IllegalAccessException {
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]}";
        final User client = new User(string);
        final Field field = reservation.getClass().getDeclaredField("client");
        field.setAccessible(true);
        field.set(reservation, client);

        //when
        final User result = reservation.getClient();

        //then
        assertEquals("field wasn't retrieved properly", result, client);
    }

    @Test
    public void getItinerary() throws NoSuchFieldException, IllegalAccessException {
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        final String string = "{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"}";
        final Itinerary itinerary = new Itinerary(string);
        final Field field = reservation.getClass().getDeclaredField("itinerary");
        field.setAccessible(true);
        field.set(reservation, itinerary);

        //when
        final Itinerary result = reservation.getItinerary();

        //then
        assertEquals("field wasn't retrieved properly", result, itinerary);
    }

    @Test
    public void getNumberOfAdults() throws NoSuchFieldException, IllegalAccessException {
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        final Field field = reservation.getClass().getDeclaredField("numberOfAdults");
        field.setAccessible(true);
        field.set(reservation, 1);

        //when
        final int result = reservation.getNumberOfAdults();

        //then
        assertEquals("field wasn't retrieved properly", result, 1);
    }

    @Test
    public void getNumberOfChildren() throws NoSuchFieldException, IllegalAccessException  {
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        final Field field = reservation.getClass().getDeclaredField("numberOfChildren");
        field.setAccessible(true);
        field.set(reservation, 1);

        //when
        final int result = reservation.getNumberOfChildren();

        //then
        assertEquals("field wasn't retrieved properly", result, 1);
    }

    @Test
    public void getRequestedDate() throws NoSuchFieldException, IllegalAccessException  {
        //given
        final Date theDate = new Date();
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        final Field field = reservation.getClass().getDeclaredField("requestedDate");
        field.setAccessible(true);
        field.set(reservation, theDate);

        //when
        final Date result = reservation.getRequestedDate();

        //then
        assertEquals("field wasn't retrieved properly", result, theDate);
    }

    @Test
    public void getForwardingDate() throws NoSuchFieldException, IllegalAccessException  {
        //given
        final Date theDate = new Date();
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        final Field field = reservation.getClass().getDeclaredField("forwardingDate");
        field.setAccessible(true);
        field.set(reservation, theDate);

        //when
        final Date result = reservation.getForwardingDate();

        //then
        assertEquals("field wasn't retrieved properly", result, theDate);
    }

    @Test
    public void getConfirmationDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        final Field field = reservation.getClass().getDeclaredField("confirmationDate");
        field.setAccessible(true);
        field.set(reservation, theDate);

        //when
        final Date result = reservation.getConfirmationDate();

        //then
        assertEquals("field wasn't retrieved properly", result, theDate);
    }

    @Test
    public void getTotal() throws NoSuchFieldException, IllegalAccessException  {
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        final Field field = reservation.getClass().getDeclaredField("total");
        field.setAccessible(true);
        field.set(reservation, 1.5f);

        //when
        final float result = reservation.getTotal();

        //then
        assertEquals("field wasn't retrieved properly", result, 1.5f,0);
    }

    @Test
    public void isConfirmed() {
        String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservationTrue = new Reservation(stringReservation);
        //then
        assertTrue(reservationTrue.isConfirmed());

        stringReservation = "{\"username\":{\"username\":\"zakkaqueen\",\"tax_code\":\"zccrgn98\",\"name\":\"Regina\",\"surname\":\"Zaccaria\",\"password\":\"$2y$10$mn1VXbS\\/11tsu06mANgsXetJTaC5447ylfnRANMq.WZfQ8AOIxL.G\",\"email\":\"zaccariaregina@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"3455028654\",\"birth_date\":\"1998-03-31\",\"sex\":\"female\",\"document\":{\"document_number\":\"at98\",\"document_type\":\"identit\\u00e0\",\"expiry_date\":\"2019-09-29\"},\"languages\":[{\"username\":\"zakkaqueen\",\"language_code\":\"IT\",\"language_name\":\"Italian\"}]},\"booked_itinerary\":{\"itinerary_code\":6,\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"title\":\"dfefef\",\"description\":\"feffef\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-21\",\"maximum_participants_number\":1,\"minimum_participants_number\":1,\"location\":\"Lizzano\",\"repetitions_per_day\":1,\"duration\":\"00:04:00\",\"image_url\":\"https:\\/\\/media.internazionale.it\\/images\\/2019\\/02\\/27\\/148022-hd.jpg\",\"full_price\":\"1.00\",\"reduced_price\":\"1.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"1.00\",\"requested_date\":\"2019-09-27\",\"forwading_date\":\"2019-09-28\",\"confirm_date\":null}";
        final Reservation reservationFalse = new Reservation(stringReservation);
        assertFalse(reservationFalse.isConfirmed());
    }

    @Test
    public void setConfirmationDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final String stringReservation = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final Reservation reservation = new Reservation(stringReservation);
        //when
        reservation.setConfirmationDate(theDate);

        //then
        final Field field = reservation.getClass().getDeclaredField("confirmationDate");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(reservation), theDate);
    }

    @Test
    public void toJSONObject() throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject("{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}");
        Reservation reservation = new Reservation(jsonObject);

        assertEquals("Fields didn't match", reservation.getClient(),new User(jsonObject.getJSONObject("username")));
        assertEquals("Fields didn't match", reservation.getItinerary(), new Itinerary(jsonObject.getJSONObject("booked_itinerary")));
        assertEquals("Fields didn't match", reservation.getConfirmationDate(), new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("confirm_date")));
        assertEquals("Fields didn't match", reservation.getForwardingDate(), new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("forwading_date")));
        assertEquals("Fields didn't match", reservation.getRequestedDate() , new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("requested_date")));
        assertEquals("Fields didn't match", reservation.getNumberOfAdults(), jsonObject.getInt("number_of_adults"));
        assertEquals("Fields didn't match", reservation.getNumberOfChildren(), jsonObject.getInt("number_of_children"));
        assertEquals("Fields didn't match", reservation.getTotal(), Float.parseFloat(jsonObject.getString("total")),0);
    }

    @Test
    public void loadFromJSONObject() throws JSONException {
        final String string = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"booked_itinerary\":{\"itinerary_code\":5,\"username\":{\"username\":\"deleted_user\",\"tax_code\":\"\",\"name\":\"Deleted\",\"surname\":\"User\",\"password\":\"\",\"email\":\"\",\"user_type\":\"2\",\"cellphone\":\"\",\"birth_date\":\"0000-00-00\",\"sex\":\"other\",\"document\":{\"document_number\":\"dele123654\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-12-03\"},\"languages\":[]},\"title\":\"Itinerario di test\",\"description\":\"Testtest\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-23\",\"maximum_participants_number\":2,\"minimum_participants_number\":1,\"location\":\"Taranto\",\"repetitions_per_day\":1,\"duration\":\"12:00:00\",\"image_url\":\"https:\\/\\/fsc.altervista.org\\/images\\/1569059443-5d85f273504a7.jpg\",\"full_price\":\"20.00\",\"reduced_price\":\"10.00\"},\"number_of_children\":1,\"number_of_adults\":1,\"total\":\"30.00\",\"requested_date\":\"2019-09-23\",\"forwading_date\":\"2019-09-24\",\"confirm_date\":\"2019-09-30\"}";
        final JSONObject jsonObject = new JSONObject(string);
        final Reservation reservation = new Reservation(jsonObject);

        final SimpleDateFormat output = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US);

        final String TAG = "Fields not setted properly";
        assertEquals(TAG, reservation.getClient(),new User(jsonObject.getJSONObject("username")));
        assertEquals(TAG, reservation.getItinerary(), new Itinerary(jsonObject.getJSONObject("booked_itinerary")));
        assertEquals(TAG, output.format(reservation.getConfirmationDate()), jsonObject.getString("confirm_date"));
        assertEquals(TAG, output.format(reservation.getForwardingDate()), jsonObject.getString("forwading_date"));
        assertEquals(TAG, output.format(reservation.getRequestedDate()) , jsonObject.getString("requested_date"));
        assertEquals(TAG, reservation.getNumberOfAdults(), jsonObject.getInt("number_of_adults"));
        assertEquals(TAG, reservation.getNumberOfChildren(), jsonObject.getInt("number_of_children"));
        assertEquals(TAG, reservation.getTotal(), Float.parseFloat(jsonObject.getString("total")),0);
    }
}