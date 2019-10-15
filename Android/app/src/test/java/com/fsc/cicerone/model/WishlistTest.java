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
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)

public class WishlistTest {

    @Test
    public void loadFromJSONObject() throws JSONException {
        final String string ="{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"itinerary_in_wishlist\":{\"itinerary_code\":6,\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"title\":\"dfefef\",\"description\":\"feffef\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-21\",\"maximum_participants_number\":1,\"minimum_participants_number\":1,\"location\":\"Lizzano\",\"repetitions_per_day\":1,\"duration\":\"00:04:00\",\"image_url\":\"https:\\/\\/media.internazionale.it\\/images\\/2019\\/02\\/27\\/148022-hd.jpg\",\"full_price\":\"1.00\",\"reduced_price\":\"1.00\"}}";

        final Map<String, Object> jsonObject = new JSONObject(string);
        final Wishlist wishlist = new Wishlist(jsonObject);

        final String TAG = "Fields not setted properly";
        assertEquals(TAG, wishlist.getUser(), new User(jsonObject.getJSONObject("username")));
        assertEquals(TAG, wishlist.getItinerary(), new Itinerary(jsonObject.getJSONObject("itinerary_in_wishlist")));
    }

    @Test
    public void getUser() throws NoSuchFieldException, IllegalAccessException {
        final String stringWishlist ="{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"itinerary_in_wishlist\":{\"itinerary_code\":6,\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"title\":\"dfefef\",\"description\":\"feffef\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-21\",\"maximum_participants_number\":1,\"minimum_participants_number\":1,\"location\":\"Lizzano\",\"repetitions_per_day\":1,\"duration\":\"00:04:00\",\"image_url\":\"https:\\/\\/media.internazionale.it\\/images\\/2019\\/02\\/27\\/148022-hd.jpg\",\"full_price\":\"1.00\",\"reduced_price\":\"1.00\"}}";
        final Wishlist wishlist = new Wishlist(stringWishlist);
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]}";
        final User user = new User(string);
        final Field field = wishlist.getClass().getDeclaredField("user");
        field.setAccessible(true);
        field.set(wishlist, user);

        //when
        final User result = wishlist.getUser();

        //then
        assertEquals("field wasn't retrieved properly", result, user);
    }

    @Test
    public void getItinerary() throws NoSuchFieldException, IllegalAccessException {
        final String stringWishlist = "{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"itinerary_in_wishlist\":{\"itinerary_code\":6,\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"title\":\"dfefef\",\"description\":\"feffef\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-21\",\"maximum_participants_number\":1,\"minimum_participants_number\":1,\"location\":\"Lizzano\",\"repetitions_per_day\":1,\"duration\":\"00:04:00\",\"image_url\":\"https:\\/\\/media.internazionale.it\\/images\\/2019\\/02\\/27\\/148022-hd.jpg\",\"full_price\":\"1.00\",\"reduced_price\":\"1.00\"}}";
        final Wishlist wishlist = new Wishlist(stringWishlist);
        final String string = "{\"itinerary_code\":6,\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"title\":\"dfefef\",\"description\":\"feffef\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-21\",\"maximum_participants_number\":1,\"minimum_participants_number\":1,\"location\":\"Lizzano\",\"repetitions_per_day\":1,\"duration\":\"00:04:00\",\"image_url\":\"https:\\/\\/media.internazionale.it\\/images\\/2019\\/02\\/27\\/148022-hd.jpg\",\"full_price\":\"1.00\",\"reduced_price\":\"1.00\"}";
        final Itinerary itinerary = new Itinerary(string);
        final Field field = wishlist.getClass().getDeclaredField("itinerary");
        field.setAccessible(true);
        field.set(wishlist, itinerary);

        //when
        final Itinerary result = wishlist.getItinerary();

        //then
        assertEquals("field wasn't retrieved properly", result, itinerary);
    }

    @Test
    public void toJSONObject() throws JSONException{
        Map<String, Object> jsonObject = new JSONObject("{\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"itinerary_in_wishlist\":{\"itinerary_code\":6,\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"title\":\"dfefef\",\"description\":\"feffef\",\"beginning_date\":\"2019-09-21\",\"ending_date\":\"2019-09-30\",\"end_reservations_date\":\"2019-09-21\",\"maximum_participants_number\":1,\"minimum_participants_number\":1,\"location\":\"Lizzano\",\"repetitions_per_day\":1,\"duration\":\"00:04:00\",\"image_url\":\"https:\\/\\/media.internazionale.it\\/images\\/2019\\/02\\/27\\/148022-hd.jpg\",\"full_price\":\"1.00\",\"reduced_price\":\"1.00\"}}");
        Wishlist wishlist= new Wishlist(jsonObject);
        JSONObject obj = wishlist.toJSONObject();

        assertEquals("Fields didn't match", new User(obj.getJSONObject("username")), new User(jsonObject.getJSONObject("username")));
        assertEquals("Fields didn't match", new Itinerary(obj.getJSONObject("itinerary_in_wishlist")), new Itinerary(jsonObject.getJSONObject("itinerary_in_wishlist")));
    }
}