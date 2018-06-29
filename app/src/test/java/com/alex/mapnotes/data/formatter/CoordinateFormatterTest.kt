package com.alex.mapnotes.data.formatter

import org.junit.Assert.assertEquals
import org.junit.Test

class CoordinateFormatterTest {
    private val sydneyLat = -33.8651
    private val sydneyLon = 151.2099
    private val sydneyExpectedResult = "(-33.8651; 151.2099)"

    @Test
    fun `verify formatter with Sydney coordinates`() {
        val formatter: LatLonFormatter = CoordinateFormatter()

        val result = formatter.format(sydneyLat, sydneyLon)

        assertEquals(sydneyExpectedResult, result)
    }
}