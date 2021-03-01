package com.raywenderlich.codingcompanionfinder

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object CommonTestDataUtil {

  fun dispatch(request: RecordedRequest): MockResponse? {
    when (request.path) {
      else -> {
        return MockResponse()
          .setResponseCode(404)
          .setBody("{}")
      }
    }
  }
}
