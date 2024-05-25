package com.sevenstars.domain.model.profile

import com.sevenstars.domain.model.profile.info.Activities
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.domain.model.profile.info.DeviceLockPreferences
import com.sevenstars.domain.model.profile.info.DislikedFactors
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.domain.model.profile.info.Strengths

data class SelectedProfileData(
    var count: Int,
    var genres: List<Genres>,
    var mbti: List<Pair<Int, String>>?,
    var strengths: List<Strengths>,
    var important: List<ImportantFactors>,
    var horror: HorrorThemePositions?,
    var hint: HintUsagePreferences?,
    var deviceOrLock: DeviceLockPreferences?,
    var activity: Activities?,
    var dislike: List<DislikedFactors>,
    var color: Colors?
)
