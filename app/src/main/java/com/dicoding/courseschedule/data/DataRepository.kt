package com.dicoding.courseschedule.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.QueryUtil
import com.dicoding.courseschedule.util.SortType
import com.dicoding.courseschedule.util.executeThread
import java.util.Calendar

//TODO 4 : Implement repository with appropriate dao
class DataRepository(private val dao: CourseDao) {

    fun getNearestSchedule(queryType: QueryType) : LiveData<Course?> {
//        throw NotImplementedError("needs implementation")
        return dao.getNearestSchedule(QueryUtil.nearestQuery(queryType))
    }

    fun getAllCourse(sortType: SortType): LiveData<PagingData<Course>> {
//        throw NotImplementedError("needs implementation")
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                dao.getAll(QueryUtil.sortedQuery(sortType))
            }
        ).flow.asLiveData()
    }

    fun getCourse(id: Int) : LiveData<Course> {
//        throw NotImplementedError("needs implementation")
        return dao.getCourse(id)
    }

    fun getTodaySchedule() : List<Course> {
//        throw NotImplementedError("needs implementation")
        return dao.getTodaySchedule(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
    }

    fun insert(course: Course) = executeThread {
        dao.insert(course)
    }

    fun delete(course: Course) = executeThread {
        dao.delete(course)
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        private const val PAGE_SIZE = 10

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = CourseDatabase.getInstance(context)
                    instance = DataRepository(database.courseDao())
                }
                return instance
            }
        }
    }
}