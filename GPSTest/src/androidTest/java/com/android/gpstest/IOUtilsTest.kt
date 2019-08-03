/*
 * Copyright (C) 2019 Sean J. Barbeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.gpstest

import android.content.Intent
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test IO utilities.  This class has to be under the Android test runner because tested code
 * uses the Application class.
 */
@RunWith(AndroidJUnit4::class)
class IOUtilsTest {

    /**
     * Test if an intent has the SHOW_RADAR action
     */
    @Test
    fun testIsShowRadarIntent() {
        // SHOW_RADAR intent
        val intent = Intent("com.google.android.radar.SHOW_RADAR")
        assertTrue(IOUtils.isShowRadarIntent(intent))

        // Not SHOW_RADAR intents
        assertFalse(IOUtils.isShowRadarIntent(Intent("not.show.radar.intent")))
        assertFalse(IOUtils.isShowRadarIntent(null));
    }

    /**
     * Test parsing a location from SHOW_RADAR intents. Latitude, longitude, and altitude oculd
     * be floats or doubles, so we have to test both (including with and without altitude).
     */
    @Test
    fun testGetLocationFromIntent() {
        // Comparison delta to allow rounding tolerance from float to double
        val delta = 0.00001

        // float values for lat and lon
        val intent = Intent("com.google.android.radar.SHOW_RADAR")
        intent.putExtra("latitude", 28.0527222f)
        intent.putExtra("longitude", -82.4331001f)

        val location = IOUtils.getLocationFromIntent(intent)
        assertEquals(28.0527222, location.latitude, delta)
        assertEquals(-82.433100, location.longitude, delta)
        assertFalse(location.hasAltitude())

        // With float altitude
        val intentWithAltitude = Intent("com.google.android.radar.SHOW_RADAR")
        intentWithAltitude.putExtra("latitude", 28.0527222f)
        intentWithAltitude.putExtra("longitude", -82.4331001f)
        intentWithAltitude.putExtra("altitude", 20.3f)

        val locationWithAltitude = IOUtils.getLocationFromIntent(intentWithAltitude)
        assertEquals(28.0527222, locationWithAltitude.latitude, delta)
        assertEquals(-82.433100, locationWithAltitude.longitude, delta)
        assertEquals(20.3, locationWithAltitude.altitude, delta)

        // double values for lat and lon
        val intentDouble = Intent("com.google.android.radar.SHOW_RADAR")
        intentDouble.putExtra("latitude", 28.0527222)
        intentDouble.putExtra("longitude", -82.4331001)

        val locationDouble = IOUtils.getLocationFromIntent(intentDouble)
        assertEquals(28.0527222, locationDouble.latitude, delta)
        assertEquals(-82.433100, locationDouble.longitude, delta)
        assertFalse(locationDouble.hasAltitude())

        // With double altitude
        val intentDoubleWithAltitude = Intent("com.google.android.radar.SHOW_RADAR")
        intentDoubleWithAltitude.putExtra("latitude", 28.0527222)
        intentDoubleWithAltitude.putExtra("longitude", -82.4331001)
        intentDoubleWithAltitude.putExtra("altitude", 20.3)

        val locationDoubleWithAltitude = IOUtils.getLocationFromIntent(intentDoubleWithAltitude)
        assertEquals(28.0527222, locationDoubleWithAltitude.latitude, delta)
        assertEquals(-82.433100, locationDoubleWithAltitude.longitude, delta)
        assertEquals(20.3, locationDoubleWithAltitude.altitude, delta)

        // NaN value for altitude
        val intentNullAltitude = Intent("com.google.android.radar.SHOW_RADAR")
        intentNullAltitude.putExtra("latitude", 28.0527222)
        intentNullAltitude.putExtra("longitude", -82.4331001)
        intentNullAltitude.putExtra("altitude", Double.NaN)

        val locationNullAltitude = IOUtils.getLocationFromIntent(intentNullAltitude)
        assertEquals(28.0527222, locationNullAltitude.latitude, delta)
        assertEquals(-82.433100, locationNullAltitude.longitude, delta)
        assertFalse(locationNullAltitude.hasAltitude())

        // double values for lat and lon, float for altitude (BenchMap config as of July 31, 2019)
        val intentDoubleWithFloatAltitude = Intent("com.google.android.radar.SHOW_RADAR")
        intentDoubleWithFloatAltitude.putExtra("latitude", 28.0527222)
        intentDoubleWithFloatAltitude.putExtra("longitude", -82.4331001)
        intentDoubleWithFloatAltitude.putExtra("altitude", 20.3f)

        val locationDoubleWithFloatAltitude = IOUtils.getLocationFromIntent(intentDoubleWithFloatAltitude)
        assertEquals(28.0527222, locationDoubleWithFloatAltitude.latitude, delta)
        assertEquals(-82.433100, locationDoubleWithFloatAltitude.longitude, delta)
        assertEquals(20.3, locationDoubleWithFloatAltitude.altitude, delta)
    }
}