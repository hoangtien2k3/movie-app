package com.hoangtien2k3.themovie.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hoangtien2k3.themovie.utils.Constants.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
data class MoviesEntity (
    @PrimaryKey
    var id:Int =0,
    var poster : String ="",
    var title : String ="",
    var rate : String ="",
    var lang : String ="",
    var year : String =""


)