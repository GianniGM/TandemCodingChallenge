package com.giannig.tandemlite.api.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.giannig.tandemlite.paging.TandemPagingSource
import com.giannig.tandemlite.paging.TandemPagingSource.Companion.NETWORK_PAGE_SIZE
import com.giannig.tandemlite.api.db.TandemDao
import com.giannig.tandemlite.api.dto.TandemUser


/**
 * Repository for [TandemUser]
 */
class TandemRepository(
    private val tandemDao: TandemDao
) {
    fun getSearchResultStream(): Pager<Int, TandemUser> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TandemPagingSource(tandemDao) }
        )
    }

    suspend fun likeUser(user: TandemUser, liked: Boolean){
        val updatedUser = user.copy(liked = liked)
        tandemDao.update(updatedUser)
    }
}
