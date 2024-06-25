package com.sevenstars.domain.model.profile

import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.info.Activities
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.domain.model.profile.info.DeviceLockPreferences
import com.sevenstars.domain.model.profile.info.DislikedFactors
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.domain.model.profile.info.Strengths

data class SavedProfileData(
    val activity: Activities?,
    val color: Colors?,
    val count: String,
    val deviceLockPreference: DeviceLockPreferences?,
    val hintUsagePreference: HintUsagePreferences?,
    val horrorThemePosition: HorrorThemePositions?,
    val nickname: String,
    val isPlusEnabled: Boolean,
    val mbti: String,
    val preferredGenres: List<Genres>,
    val state: ProfileState,
    val themeDislikedFactors: List<DislikedFactors>,
    val themeImportantFactors: List<ImportantFactors>,
    val userStrengths: List<Strengths>
)
