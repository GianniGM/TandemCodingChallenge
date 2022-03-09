package com.giannig.tandemlite.paging

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giannig.tandemlite.api.TandemNetworkService
import com.giannig.tandemlite.api.db.TandemDao
import com.giannig.tandemlite.api.dto.TandemUser
import retrofit2.HttpException
import java.io.IOException

/**
 * Paging source that manage the paging from the data we receive from the db and the data we receive from the apis
 */
class TandemPagingSource(private val tandemDao: TandemDao) : PagingSource<Int, TandemUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TandemUser> {
        val position = params.key ?: STARTING_PAGE_INDEX
        val usersFromDb = tandemDao.getTandemUsers()

        return try {
            val response = TandemNetworkService.getTandemUserFromApis(position)
            val (usersFromApi, errorCode) = response

            if (errorCode == null && usersFromApi.isNotEmpty()) {

                val users = usersFromDb.putLikedUsersInto(usersFromApi)
                tandemDao.insertAll(users)

                LoadResult.Page(
                    data = users,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (users.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Page(
                    data = usersFromDb,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (usersFromDb.isEmpty()) null else position + 1
                )
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
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
        fun List<TandemUser>.putLikedUsersInto(users: List<TandemUser>): List<TandemUser> {
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