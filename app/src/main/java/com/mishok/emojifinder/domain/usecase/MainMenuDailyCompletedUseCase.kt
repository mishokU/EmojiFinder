package com.mishok.emojifinder.domain.usecase

import com.mishok.emojifinder.domain.UseCase
import com.mishok.emojifinder.domain.prefs.DailyWinningsPrefs
import javax.inject.Inject

open class MainMenuDailyCompletedUseCase @Inject constructor(
    private val daily : DailyWinningsPrefs
) : UseCase<Unit, Boolean>() {
    override fun execute(parameters: Unit): Boolean = daily.isNextDay()
}