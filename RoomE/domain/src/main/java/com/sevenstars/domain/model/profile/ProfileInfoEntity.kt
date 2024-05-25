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

data class ProfileInfoEntity(
    val genres: List<Genres>,
    val strengths: List<Strengths>,
    val importantFactors: List<ImportantFactors>,
    val horrorThemePositions: List<HorrorThemePositions>,
    val hintUsagePreferences: List<HintUsagePreferences>,
    val deviceLockPreferences: List<DeviceLockPreferences>,
    val activities: List<Activities>,
    val dislikedFactors: List<DislikedFactors>,
    val colors: List<Colors>
)
