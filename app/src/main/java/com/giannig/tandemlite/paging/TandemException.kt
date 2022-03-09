package com.giannig.tandemlite.paging

import java.lang.Exception
import com.giannig.tandemlite.api.TandemApi

/**
 * Exception handled in case we receive an error from the [TandemApi]
 */
data class TandemException(val errorCode: String, val errorMessage: String) : Exception()