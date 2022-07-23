package com.example.nkustplatformassistant

import android.graphics.BitmapFactory
import org.junit.Test
import android.content.res.Resources

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val bitmap = BitmapFactory.decodeResource(Resources, R.drawable.validate_code)

        print(bitmap)
    }
}