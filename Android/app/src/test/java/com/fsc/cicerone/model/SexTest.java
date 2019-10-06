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

import com.fsc.cicerone.R;

import org.junit.Test;

import static org.junit.Assert.*;

public class SexTest {

    @Test
    public void getValue() {
        assertEquals("field wasn't retrieved properly", Sex.getValue("male"),Sex.MALE);
        assertEquals("field wasn't retrieved properly", Sex.getValue("female"),Sex.FEMALE);
        assertEquals("field wasn't retrieved properly", Sex.getValue("other"),Sex.OTHER);
        assertEquals("field wasn't retrieved properly", Sex.getValue("maschio"),Sex.MALE);
        assertEquals("field wasn't retrieved properly", Sex.getValue("femmina"),Sex.FEMALE);
        assertEquals("field wasn't retrieved properly", Sex.getValue("altro"),Sex.OTHER);
        assertNull("field wasn't retrieved properly", Sex.getValue("ciaone"));

    }

    @Test
    public void toString1() {
        assertEquals("field wasn't retrieved properly", Sex.MALE.toString(),"male");
        assertEquals("field wasn't retrieved properly", Sex.FEMALE.toString(),"female");
        assertEquals("field wasn't retrieved properly", Sex.OTHER.toString(),"other");
    }

    @Test
    public void getAvatarResource() {
        assertEquals("field wasn't retrieved properly", Sex.MALE.getAvatarResource(), R.drawable.avatar_male);
        assertEquals("field wasn't retrieved properly", Sex.FEMALE.getAvatarResource(), R.drawable.avatar_female);
        assertEquals("field wasn't retrieved properly", Sex.OTHER.getAvatarResource(), R.drawable.avatar_neutral);
    }
}