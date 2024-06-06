package com.example.bcash.service.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bcash.service.api.ApiService
import com.example.bcash.service.response.ProductItem
import com.example.bcash.utils.session.SessionPreferences
import kotlinx.coroutines.flow.first

class PagingSource (private val preferences: SessionPreferences, private val apiService: ApiService) : PagingSource<Int, ProductItem>() {

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = preferences.getSession().first().token
            if (token.isEmpty()) {
                Log.e(TAG, "Token not found: $token")
                return LoadResult.Error(Exception("Token not found"))
            } else {
                val responseData = apiService.getAllProduct(token, page, params.loadSize)
                if (responseData.isSuccessful) {
                    val responseBody = responseData.body()
                    LoadResult.Page(
                        data = responseBody?.listProduct ?: emptyList(),
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (responseBody?.listProduct.isNullOrEmpty()) null else page + 1
                    )
                } else {
                    val message = responseData.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Error loading data: $message")
                    LoadResult.Error(Exception("Error loading data: $message"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading data", e)
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val TAG = "PagingSource"
        private const val INITIAL_PAGE_INDEX = 1
    }
}