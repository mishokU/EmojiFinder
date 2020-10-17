package com.mishok.emojifinder.data.db.local.fake
import com.mishok.emojifinder.data.db.local.models.Daily
import com.mishok.emojifinder.data.db.local.models.DailyModel
import com.mishok.emojifinder.domain.result.Result

class FakeDailyItems {

    fun fetchFakeData(): Result<List<DailyModel>> {
        return try {
            val dailies = mutableListOf<DailyModel>()
            for(i : Int in 1..15){
                when {
                    i % 3 == 0 -> {
                        val daily = DailyModel(
                            id = i,
                            type = Daily.BOX,
                            cost = i / 3
                        )
                        dailies.add(daily)
                    }
                    i % 4 == 0 -> {
                        val daily = DailyModel(
                            id = i,
                            type = Daily.EMOJI,
                            cost = i / 4
                        )
                        dailies.add(daily)
                    }
                    else -> {
                        val daily = DailyModel(
                            id = i,
                            type = Daily.EMOS,
                            cost = i * 10
                        )
                        dailies.add(daily)
                    }
                }
            }
            Result.Success(dailies)
        }  catch (e : Exception){
            Result.Error(e)
        }
    }
}