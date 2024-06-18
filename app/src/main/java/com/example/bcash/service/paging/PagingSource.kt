package com.example.bcash.service.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bcash.service.api.ApiService
import com.example.bcash.service.response.data.ProductItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PagingSource(private val apiService: ApiService) : PagingSource<Int, ProductItem>() {

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = withContext(Dispatchers.IO) {
                apiService.getAllProduct(page, params.loadSize)
            }
            if (responseData.isSuccessful) {
                Log.e(TAG, "Data loaded successfully")

                val responseBody = responseData.body()
                Log.e(TAG, "responseBody: $responseBody")

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
        } catch (e: CancellationException) {
            Log.e(TAG, "Job was cancelled", e)
            LoadResult.Error(e)
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
