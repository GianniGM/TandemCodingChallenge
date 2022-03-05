package com.giannig.tandemlite.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giannig.tandemlite.api.TandemNetworkService
import com.giannig.tandemlite.api.dto.TandemUser
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

/**
 * todo
 */
data class TandemException(val errorCode: String, val errorMessage:String): Exception()

/**
 * todo
 */
class TandemPagingSource : PagingSource<Int, TandemUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TandemUser> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val dto = TandemNetworkService.getTandemUserFromApis(position)
            val users = dto.response
            if(dto.errorCode == null){
                LoadResult.Page(
                    data = users,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                    nextKey = if (users.isEmpty()) null else position + 1
                )
            }else{
               throw TandemException(dto.errorCode, dto.type)
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: TandemException) {
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
    }

}