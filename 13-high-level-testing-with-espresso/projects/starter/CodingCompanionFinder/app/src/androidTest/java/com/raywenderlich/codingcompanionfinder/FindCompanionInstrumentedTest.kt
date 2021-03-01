/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.codingcompanionfinder

import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FindCompanionInstrumentedTest {

  lateinit var testScenario: ActivityScenario<MainActivity>

  companion object {

    private lateinit var startIntent: Intent

    val server = MockWebServer()

    val dispatcher: Dispatcher = object : Dispatcher() {

      @Throws(InterruptedException::class)
      override fun dispatch(
        request: RecordedRequest
      ): MockResponse {
        return CommonTestDataUtil.dispatch(request) ?: MockResponse().setResponseCode(404)
      }
    }

    @BeforeClass
    @JvmStatic
    fun setup() {
      server.setDispatcher(dispatcher)
      server.start()

      startIntent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
      startIntent.putExtra(MainActivity.PETFINDER_URI, server.url("").toString())
    }
  }

  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getTargetContext()
    assertEquals("com.raywenderlich.codingcompanionfinder", appContext.packageName)
  }

  @Test
  fun pressing_the_find_bottom_menu_item_takes_the_user_to_the_find_page() {
    testScenario = ActivityScenario.launch(startIntent)

    onView(withId(R.id.searchForCompanionFragment)).perform(click())
    onView(withId(R.id.searchButton)).check(matches(isDisplayed()))
    onView(withId(R.id.searchFieldText)).check(matches(isDisplayed()))

    testScenario.close()
  }
}
