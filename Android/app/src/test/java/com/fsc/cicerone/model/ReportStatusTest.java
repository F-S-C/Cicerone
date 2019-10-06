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

import static org.junit.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class)
public class ReportStatusTest {

    @Test
    public void getValue() {

        assertEquals("field wasn't retrieved properly", ReportStatus.getValue(0),ReportStatus.OPEN);
        assertEquals("field wasn't retrieved properly", ReportStatus.getValue(1),ReportStatus.PENDING);
        assertEquals("field wasn't retrieved properly", ReportStatus.getValue(2),ReportStatus.CLOSED);
        assertEquals("field wasn't retrieved properly", ReportStatus.getValue(3),ReportStatus.CANCELED);

    }

    @Test
    public void toInt() {

        assertEquals("field wasn't retrieved properly", ReportStatus.OPEN.toInt().intValue(),0);
        assertEquals("field wasn't retrieved properly", ReportStatus.PENDING.toInt().intValue(),1);
        assertEquals("field wasn't retrieved properly", ReportStatus.CLOSED.toInt().intValue(),2);
        assertEquals("field wasn't retrieved properly", ReportStatus.CANCELED.toInt().intValue(),3);

    }

    @Test
    public void toString1() {
        assertEquals("field wasn't retrieved properly", ReportStatus.OPEN.toString(),"Open");
        assertEquals("field wasn't retrieved properly", ReportStatus.PENDING.toString(),"Pending");
        assertEquals("field wasn't retrieved properly", ReportStatus.CLOSED.toString(),"Closed");
        assertEquals("field wasn't retrieved properly", ReportStatus.CANCELED.toString(),"Canceled");

    }
}