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
public class UserReviewTest {

    @Test
    public void loadFromJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"reviewed_user\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"feedback\":2,\"description\":\"ciao\"}");

        final String user = jsonObject.getJSONObject("username").getString("username");
        final String reviewed_user = jsonObject.getJSONObject("reviewed_user").getString("username");
        final int feedback = jsonObject.getInt("feedback");
        final String description = jsonObject.getString("description");

        UserReview userReview = new UserReview(jsonObject);

        assertEquals("field wasn't retrieved properly", user, userReview.getAuthor().getUsername());
        assertEquals("field wasn't retrieved properly", reviewed_user, userReview.getReviewedUser().getUsername());
        assertEquals("field wasn't retrieved properly", feedback, userReview.getFeedback());
        assertEquals("field wasn't retrieved properly", description, userReview.getDescription());

    }

    @Test
    public void getReviewedUser() {
        final User author = new User();
        final User reviewedUser = new User();
        final UserReview.Builder userReview = new UserReview.Builder(author, reviewedUser);

        //when
        final User reviewedUserReturned = userReview.build().getReviewedUser();

        //then
        assertEquals("field wasn't retrieved properly", reviewedUser, reviewedUserReturned);
    }

    @Test
    public void toJSONObject() throws JSONException{
        JSONObject jsonObject = new JSONObject("{\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"reviewed_user\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"feedback\":2,\"description\":\"ciao\"}");

        UserReview userReview = new UserReview(jsonObject);
        JSONObject obj = userReview.toJSONObject();

        assertEquals("Fields didn't match", new User(obj.getJSONObject("reviewed_user")), new User(jsonObject.getJSONObject("reviewed_user")));
        assertEquals("Fields didn't match", new User(obj.getJSONObject("username")), new User(jsonObject.getJSONObject("username")));
        assertEquals("Fields didn't match", obj.getInt("feedback"), jsonObject.getInt("feedback"));
        assertEquals("Fields didn't match", obj.getString("description"), jsonObject.getString("description"));
    }

    @Test
    public void setFeedback() throws JSONException, NoSuchFieldException, IllegalAccessException {
        JSONObject jsonObject = new JSONObject("{\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"reviewed_user\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"feedback\":2,\"description\":\"ciao\"}");
        //given
        final UserReview userReview = new UserReview(jsonObject);

        //when
        userReview.setFeedback(4);

        //then
        final Field field = Objects.requireNonNull(userReview.getClass().getSuperclass()).getDeclaredField("feedback");
        field.setAccessible(true);
        Assert.assertEquals("field wasn't retrieved properly", field.get(userReview), 4);
    }

    @Test
    public void setDescription() throws JSONException, NoSuchFieldException, IllegalAccessException {
        JSONObject jsonObject = new JSONObject("{\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"reviewed_user\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"feedback\":2,\"description\":\"ciao\"}");
        //given
        final UserReview userReview = new UserReview(jsonObject);

        //when
        userReview.setDescription("test_description");

        //then
        final Field field = Objects.requireNonNull(userReview.getClass().getSuperclass()).getDeclaredField("description");
        field.setAccessible(true);
        Assert.assertEquals("field wasn't retrieved properly", field.get(userReview), "test_description");
    }
}