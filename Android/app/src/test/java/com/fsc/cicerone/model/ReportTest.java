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

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ReportTest {

    @Test
    public void loadFromJSONObject() throws JSONException {
        final String string = "{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}";

        final JSONObject jsonObject = new JSONObject(string);
        final Report report = new Report(jsonObject);

        final String TAG = "Fields not setted properly";
        assertEquals(TAG, report.getCode(), jsonObject.getInt("report_code"));
        assertEquals(TAG, report.getBody(), jsonObject.getString("report_body"));
        assertEquals(TAG, report.getObject(), jsonObject.getString("object"));
        assertEquals(TAG, report.getStatus().toInt().intValue(), jsonObject.getInt("state"));
        assertEquals(TAG, report.getAuthor(),  new User(jsonObject.getJSONObject("username")));
        assertEquals(TAG, report.getReportedUser(),  new User(jsonObject.getJSONObject("reported_user")));
    }

    @Test
    public void getCode() throws NoSuchFieldException, IllegalAccessException {
        final String string1 = "{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}";
        final Report report = new Report(string1);
        final Field field = report.getClass().getDeclaredField("code");
        field.setAccessible(true);
        field.set(report, 5);

        //when
        final int result = report.getCode();

        //then
        assertEquals("field wasn't retrieved properly", result, 5);
    }

    @Test
    public void getAuthor() throws NoSuchFieldException, IllegalAccessException {
        final String string1 = "{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}";
        final Report report = new Report(string1);
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]}";
        final User reporter = new User(string);
        final Field field = report.getClass().getDeclaredField("author");
        field.setAccessible(true);
        field.set(report, reporter);

        //when
        final User result = report.getAuthor();

        //then
        assertEquals("field wasn't retrieved properly", result, reporter);
    }

    @Test
    public void getReportedUser() throws NoSuchFieldException, IllegalAccessException {
        final String string1 = "{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}";
        final Report report = new Report(string1);
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$nXW4njlKyYR4EmQoiQg3iee5xw9RfC7VeD\\/Z5t\\/UnX0xWAasnurq.\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"Identity Card\",\"expiry_date\":\"2022-02-25\"},\"languages\":[]}";
        final User reported = new User(string);
        final Field field = report.getClass().getDeclaredField("reportedUser");
        field.setAccessible(true);
        field.set(report, reported);

        //when
        final User result = report.getReportedUser();

        //then
        assertEquals("field wasn't retrieved properly", result, reported);
    }

    @Test
    public void getObject() throws NoSuchFieldException, IllegalAccessException {
        final String string1 = "{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}";
        final Report report = new Report(string1);
        final Field field = report.getClass().getDeclaredField("object");
        field.setAccessible(true);
        field.set(report, "object_test");

        //when
        final String result = report.getObject();

        //then
        assertEquals("field wasn't retrieved properly", result, "object_test");
    }

    @Test
    public void getBody() throws NoSuchFieldException, IllegalAccessException {
        final String string1 = "{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}";
        final Report report = new Report(string1);
        final Field field = report.getClass().getDeclaredField("body");
        field.setAccessible(true);
        field.set(report, "body_test");

        //when
        final String result = report.getBody();

        //then
        assertEquals("field wasn't retrieved properly", result, "body_test");
    }

    @Test
    public void getStatus() throws NoSuchFieldException, IllegalAccessException {
        final String string1 = "{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}";
        final Report report = new Report(string1);
        final Field field = report.getClass().getDeclaredField("status");
        field.setAccessible(true);
        field.set(report, ReportStatus.CANCELED);

        //when
        final ReportStatus result = report.getStatus();

        //then
        assertEquals("field wasn't retrieved properly", result,  ReportStatus.CANCELED);
    }

    @Test
    public void setStatus() throws NoSuchFieldException, IllegalAccessException {
        //given
        final String string1 = "{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}";
        final Report report = new Report(string1);

        //when
        report.setStatus(ReportStatus.CANCELED);

        //then
        final Field field = report.getClass().getDeclaredField("status");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(report), ReportStatus.CANCELED);
    }

    @Test
    public void toJSONObject()  throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"report_code\":93,\"username\":{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc\\/zhzmM.zhgx9ptLNFmG0\\/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25\\/02\\/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]},\"reported_user\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"report_body\":\"Funghi?\",\"state\":3,\"object\":\"ciao\"}");
        Report report = new Report(jsonObject);
        JSONObject obj = report.toJSONObject();

        assertEquals("Fields didn't match", obj.getInt("report_code"), jsonObject.getInt("report_code"));
        assertEquals("Fields didn't match", obj.getString("object"), jsonObject.getString("object"));
        assertEquals("Fields didn't match", obj.getString("report_body"), jsonObject.getString("report_body"));
        assertEquals("Fields didn't match", obj.getString("state"), jsonObject.getString("state"));
        assertEquals("Fields didn't match", new User(obj.getJSONObject("username")),new User(jsonObject.getJSONObject("username")));
        assertEquals("Fields didn't match", new User(obj.getJSONObject("reported_user")), new User(jsonObject.getJSONObject("reported_user")));
    }
}