package com.baldystudios.androidjetpackmviadvanced.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.baldystudios.androidjetpackmviadvanced.models.BlogPost

@Dao
interface BlogPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogPost: BlogPost): Long

}