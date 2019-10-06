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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class UserTest {

    @Test
    public void equals1() {
    }

    @Test
    public void hashCode1() {
    }

    @Test
    public void loadFromJSONObject() {
    }

    @Test
    public void getName() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("name");
        field.setAccessible(true);
        field.set(user, "name_test");

        //when
        final String result = user.getName();

        //then
        assertEquals("field wasn't retrieved properly", result, "name_test");
    }

    @Test
    public void setName() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();

        //when
        user.setName("name_test");

        //then
        final Field field = user.getClass().getDeclaredField("name");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user), "name_test");
    }

    @Test
    public void getSurname() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("surname");
        field.setAccessible(true);
        field.set(user, "surname_test");

        //when
        final String result = user.getSurname();

        //then
        assertEquals("field wasn't retrieved properly", result, "surname_test");
    }

    @Test
    public void setSurname() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();

        //when
        user.setSurname("surname_test");

        //then
        final Field field = user.getClass().getDeclaredField("surname");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user), "surname_test");
    }

    @Test
    public void getEmail() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("email");
        field.setAccessible(true);
        field.set(user, "email_test");

        //when
        final String result = user.getEmail();

        //then
        assertEquals("field wasn't retrieved properly", result, "email_test");
    }

    @Test
    public void setEmail() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();

        //when
        user.setEmail("email_test");

        //then
        final Field field = user.getClass().getDeclaredField("email");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user), "email_test");
    }

    @Test
    public void getPassword() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("password");
        field.setAccessible(true);
        field.set(user, "password_test");

        //when
        final String result = user.getPassword();

        //then
        assertEquals("field wasn't retrieved properly", result, "password_test");
    }

    @Test
    public void setPassword() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();

        //when
        user.setPassword("password_test");

        //then
        final Field field = user.getClass().getDeclaredField("password");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user), "password_test");
    }

    @Test
    public void getSex() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("sex");
        field.setAccessible(true);
        field.set(user, Sex.MALE);

        //when
        final Sex result = user.getSex();

        //then
        assertEquals("field wasn't retrieved properly", result, Sex.MALE);
    }

    @Test
    public void setSex() throws IllegalAccessException, NoSuchFieldException {
        //given
        final User user = new User();

        //when
        user.setSex(Sex.MALE);

        //then
        final Field field = user.getClass().getDeclaredField("sex");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user), Sex.MALE);
    }

    @Test
    public void getTaxCode() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("taxCode");
        field.setAccessible(true);
        field.set(user, "taxCode_test");

        //when
        final String result = user.getTaxCode();

        //then
        assertEquals("field wasn't retrieved properly", result, "taxCode_test");
    }

    @Test
    public void setTaxCode() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();

        //when
        user.setTaxCode("taxCode_test");

        //then
        final Field field = user.getClass().getDeclaredField("taxCode");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user), "taxCode_test");
    }

    @Test
    public void getUsername() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("username");
        field.setAccessible(true);
        field.set(user, "username_test");

        //when
        final String result = user.getUsername();

        //then
        assertEquals("field wasn't retrieved properly", result, "username_test");
    }

    @Test
    public void getUserType() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("userType");
        field.setAccessible(true);
        field.set(user, UserType.CICERONE);

        //when
        final UserType result = user.getUserType();

        //then
        assertEquals("field wasn't retrieved properly", result, UserType.CICERONE);
    }

    @Test
    public void setUserType() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();

        //when
        user.setUserType(UserType.CICERONE);

        //then
        final Field field = user.getClass().getDeclaredField("userType");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user), UserType.CICERONE);
    }

    @Test
    public void getCellphone() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("cellphone");
        field.setAccessible(true);
        field.set(user, "cellphone_test");

        //when
        final String result = user.getCellphone();

        //then
        assertEquals("field wasn't retrieved properly", result, "cellphone_test");
    }

    @Test
    public void setCellphone() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();

        //when
        user.setCellphone("cellphone_test");

        //then
        final Field field = user.getClass().getDeclaredField("cellphone");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user), "cellphone_test");
    }

    @Test
    public void getBirthDate() throws NoSuchFieldException, IllegalAccessException {
        final Date testDate = new Date();
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("birthDate");
        field.setAccessible(true);
        field.set(user, testDate);

        //when
        final Date result = user.getBirthDate();

        //then
        assertEquals("field wasn't retrieved properly", result,testDate);
    }

    @Test
    public void setBirthDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();
        final Date testDate = new Date();

        //when
        user.setBirthDate(testDate);

        //then
        final Field field = user.getClass().getDeclaredField("birthDate");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user),testDate);
    }

    @Test
    public void getDocument() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Document testDocument = new Document();
        final Field field = user.getClass().getDeclaredField("document");
        field.setAccessible(true);
        field.set(user, testDocument);

        //when
        final Document result = user.getDocument();

        //then
        assertEquals("field wasn't retrieved properly", result, testDocument);
    }

    @Test
    public void setDocument() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();
        final Document testDocument = new Document();

        //when
        user.setDocument(testDocument);

        //then
        final Field field = user.getClass().getDeclaredField("document");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(user),testDocument);
    }

    @Test
    public void getLanguages() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Set<Language> testSet = new HashSet<>();
        testSet.add(new Language("code","name"));
        final Field field = user.getClass().getDeclaredField("languages");
        field.setAccessible(true);
        field.set(user, testSet);

        //when
        final Set<Language> result = user.getLanguages();

        //then
        assertEquals("field wasn't retrieved properly", result, testSet);
    }

    @Test
    public void addLanguage() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();
        final Set<Language> testSetLanguage = new HashSet<>(1);
        testSetLanguage.add(new Language("test_code","test_name"));
        final Field field = user.getClass().getDeclaredField("languages");
        field.setAccessible(true);
        //when
        field.set(user,testSetLanguage);


        assertEquals("field wasn't retrieved properly", field.get(user),testSetLanguage);

    }

    @Test
    public void removeLanguage() throws NoSuchFieldException, IllegalAccessException {
        //given
        final User user = new User();
        final Set<Language> testSetLanguage = new HashSet<>(1);
        final Language testLanguage = new Language("test_code","test_name");
        testSetLanguage.add(testLanguage);
        final Field field = user.getClass().getDeclaredField("languages");
        field.setAccessible(true);
        //when
        field.set(user,testSetLanguage);

        assertEquals("field wasn't retrieved properly", field.get(user),testSetLanguage);

        user.removeLanguage(testLanguage);
        testSetLanguage.remove(testLanguage);
        assertEquals("field wasn't retrieved properly", field.get(user),testSetLanguage);

    }

    @Test
    public void toJSONObject() throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject("{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc/zhzmM.zhgx9ptLNFmG0/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25/02/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]}");

            User user = new User(jsonObject);

        assertEquals("Fields didn't match", user.getName(), jsonObject.getString("name"));
        assertEquals("Fields didn't match", user.getSurname(), jsonObject.getString("surname"));
        assertEquals("Fields didn't match", user.getUsername(),jsonObject.getString("username"));
        assertEquals("Fields didn't match", user.getPassword(), jsonObject.getString("password"));
        assertEquals("Fields didn't match", user.getEmail(), jsonObject.getString("email"));
        assertEquals("Fields didn't match", user.getUserType().toInt().intValue(), jsonObject.getInt("user_type"));
        assertEquals("Fields didn't match", user.getCellphone() , jsonObject.getString("cellphone"));
        assertEquals("Fields didn't match", user.getBirthDate(), new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("birth_date")));
        assertEquals("Fields didn't match", user.getSex().toString(), jsonObject.getString("sex"));
        assertEquals("Fields didn't match", user.getTaxCode(), jsonObject.getString("tax_code"));
        assertEquals("Fields didn't match", user.getDocument().toString(), jsonObject.getString("document"));
        assertEquals("Fields didn't match", user.getLanguages().toString(), jsonObject.getString("languages"));

    }

    @Test
    public void getCredentials() throws NoSuchFieldException, IllegalAccessException {
        final User user = new User();
        final Field field = user.getClass().getDeclaredField("username");
        field.setAccessible(true);
        field.set(user, "test_username");

        final Field field2 = user.getClass().getDeclaredField("password");
        field2.setAccessible(true);
        field2.set(user, "test_password");


        //when
        final String resultUsername = user.getCredentials().getUsername();
        final String resultPassword = user.getCredentials().getPassword();

        //then
        assertEquals("field wasn't retrieved properly", resultUsername, "test_username");
        assertEquals("field wasn't retrieved properly", resultPassword, "test_password");

    }

    @Test
    public void validateUsername()  {
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc/zhzmM.zhgx9ptLNFmG0/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25/02/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]}";
        final User user = new User(string);


        //then
        assertTrue( user.validateUsername(user.getUsername()));
        assertFalse( user.validateUsername("test?username"));

    }

    @Test
    public void validateEmail() {
        final String string = "{\"username\":\"test\",\"tax_code\":\"IT0000000\",\"name\":\"test\",\"surname\":\"testsurname\",\"password\":\"$2y$10$KIkp6WTmsHLhiHc/zhzmM.zhgx9ptLNFmG0/48CHjtKSARv9nNKiS\",\"email\":\"graziano.montanaro98@gmail.com\",\"user_type\":\"1\",\"cellphone\":\"0999561111\",\"birth_date\":\"1998-09-28\",\"sex\":\"male\",\"document\":{\"document_number\":\"test0000\",\"document_type\":\"25/02/2022\",\"expiry_date\":\"2019-10-20\"},\"languages\":[]}";
        final User user = new User(string);


        //then
        assertTrue( user.validateEmail(user.getEmail()));
        assertFalse( user.validateEmail("test_email"));

    }
}

