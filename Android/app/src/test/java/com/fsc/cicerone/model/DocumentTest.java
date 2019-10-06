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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricTestRunner.class)
public class DocumentTest {

    @Test
    public void getNumber() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Document document = new Document();
        final Field field = document.getClass().getDeclaredField("number");
        field.setAccessible(true);
        field.set(document, "test_number");

        //when
        final String result = document.getNumber();

        //then
        assertEquals("field wasn't retrieved properly", result, "test_number");
    }

    @Test
    public void setNumber() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Document document = new Document();

        //when
        document.setNumber("test_number");

        //then
        final Field field = document.getClass().getDeclaredField("number");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(document), "test_number");
    }

    @Test
    public void getType() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Document document = new Document();
        final Field field = document.getClass().getDeclaredField("type");
        field.setAccessible(true);
        field.set(document, "test_type");

        //when
        final String result = document.getType();

        //then
        assertEquals("field wasn't retrieved properly", result, "test_type");
    }

    @Test
    public void setType() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Document document = new Document();

        //when
        document.setType("test_type");

        //then
        final Field field = document.getClass().getDeclaredField("type");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(document), "test_type");
    }

    @Test
    public void getExpirationDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final Document document = new Document();
        final Field field = document.getClass().getDeclaredField("expirationDate");
        field.setAccessible(true);
        field.set(document, theDate);

        //when
        final Date result = document.getExpirationDate();

        //then
        assertEquals("field wasn't retrieved properly", result, theDate);
    }

    @Test
    public void setExpirationDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Date theDate = new Date();
        final Document document = new Document();

        //when
        document.setExpirationDate(theDate);

        //then
        final Field field = document.getClass().getDeclaredField("expirationDate");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(document), theDate);
    }

    @Test
    public void toJSONObject() throws JSONException, ParseException {
        final String number = "123456";
        final String type = "identity card";
        final String date = "2019-01-15";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("document_number", number);
        jsonObject.put("document_type", type);
        jsonObject.put("expiry_date", date);


        Document document = new Document();
        document.setType(type);
        document.setExpirationDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        document.setNumber(number);

        assertEquals("Fields didn't match", document.toJSONObject().toString(), jsonObject.toString());
    }


    @Test
    public void equals1() {
        final String string = "{\"document_number\":\"at98\",\"document_type\":\"identit\\u00e0\",\"expiry_date\":\"2019-09-29\"}";
        final String string2 = "{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"}";

        Document d1 = new Document(string);
        Document d2 = new Document(string);
        Document d3 = new Document(string2);

        assertEquals("field wasn't retrieved properly", d1, d2);
        assertNotEquals("field wasn't retrieved properly", d1, d3);
        assertEquals("field wasn't retrieved properly", d1, d1);
    }

    @Test
    public void hashCode1() {
        final String string = "{\"document_number\":\"at98\",\"document_type\":\"identit\\u00e0\",\"expiry_date\":\"2019-09-29\"}";

        Document d1 = new Document(string);
        Document d2 = new Document(string);

        assertEquals("field wasn't retrieved properly", d1, d2);
        assertEquals("field wasn't retrieved properly", d1, d1);
    }
}