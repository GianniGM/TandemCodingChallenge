package com.giannig.tandemlite.paging

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giannig.tandemlite.api.TandemNetworkService
import com.giannig.tandemlite.api.db.TandemDao
import com.giannig.tandemlite.api.dto.TandemUser
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

/**
 * todo
 */
data class TandemException(val errorCode: String, val errorMessage: String) : Exception()

/**
 * todo
 */
class TandemPagingSource(private val tandemDao: TandemDao) : PagingSource<Int, TandemUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TandemUser> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val dto = TandemNetworkService.getTandemUserFromApis(position)
            val users = joinByLikes(dto.response)

            if (dto.errorCode == null) {
                LoadResult.Page(
                    data = users.sortedBy { it.referenceCnt },
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                    nextKey = if (users.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(TandemException(dto.errorCode, dto.type))
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            val users = tandemDao.getTandemUsers()
            return if (users.isEmpty()) {
                LoadResult.Error(exception)
            } else {
                LoadResult.Page(
                    data = users,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                    nextKey = if (users.isEmpty()) null else position + 1
                )
            }
        }
    }

    private suspend fun joinByLikes(users: List<TandemUser>): List<TandemUser> {
        val usersFromDB: List<TandemUser> = tandemDao.getTandemUsers()
        return usersFromDB.joinLikedUsersWith(users)
    }

    override fun getRefreshKey(state: PagingState<Int, TandemUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 20

        @VisibleForTesting
        fun List<TandemUser>.joinLikedUsersWith(users: List<TandemUser>): List<TandemUser> {
            val likedUsers = this
                .filter { it.liked }
                .groupBy { it.id }

            return users.map { user ->
                if (user.id in likedUsers) {
                    user.copy(liked = true)
                } else {
                    user.copy(liked = false)
                }
            }
        }

    }

}