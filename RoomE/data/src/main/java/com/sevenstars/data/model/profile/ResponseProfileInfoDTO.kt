package com.sevenstars.data.model.profile

import com.google.gson.annotations.SerializedName
import com.sevenstars.data.model.profile.info.ActivitiesDTO
import com.sevenstars.data.model.profile.info.ColorsDTO
import com.sevenstars.data.model.profile.info.DeviceLockPreferencesDTO
import com.sevenstars.data.model.profile.info.DislikedFactorsDTO
import com.sevenstars.data.model.profile.info.GenresDTO
import com.sevenstars.data.model.profile.info.HintUsagePreferencesDTO
import com.sevenstars.data.model.profile.info.HorrorThemePositionsDTO
import com.sevenstars.data.model.profile.info.ImportantFactorsDTO
import com.sevenstars.data.model.profile.info.StrengthsDTO

data class ResponseProfileInfoDTO(
    @SerializedName("genres")
    val genres: List<GenresDTO>,
    @SerializedName("strengths")
    val strengths: List<StrengthsDTO>,
    @SerializedName("importantFactors")
    val importantFactors: List<ImportantFactorsDTO>,
    @SerializedName("horrorThemePositions")
    val horrorThemePositions: List<HorrorThemePositionsDTO>,
    @SerializedName("hintUsagePreferences")
    val hintUsagePreferences: List<HintUsagePreferencesDTO>,
    @SerializedName("deviceLockPreferences")
    val deviceLockPreferences: List<DeviceLockPreferencesDTO>,
    @SerializedName("activities")
    val activities: List<ActivitiesDTO>,
    @SerializedName("dislikedFactors")
    val dislikedFactors: List<DislikedFactorsDTO>,
    @SerializedName("colors")
    val colors: List<ColorsDTO>
)