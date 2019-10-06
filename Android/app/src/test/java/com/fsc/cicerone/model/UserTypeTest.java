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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class UserTypeTest {

    @Test
    public void getValue() {
        assertEquals("field wasn't retrieved properly", UserType.getValue(0),UserType.GLOBETROTTER);
        assertEquals("field wasn't retrieved properly", UserType.getValue(1),UserType.CICERONE);
        assertEquals("field wasn't retrieved properly", UserType.getValue(2),UserType.ADMIN);
    }

    @Test
    public void toInt() {
        assertEquals("field wasn't retrieved properly", UserType.GLOBETROTTER.toInt().intValue(),0);
        assertEquals("field wasn't retrieved properly", UserType.CICERONE.toInt().intValue(),1);
        assertEquals("field wasn't retrieved properly", UserType.ADMIN.toInt().intValue(),2);
    }
}