package com.sevenstars.data.model.profile.info

import com.google.gson.annotations.SerializedName

data class ResponseProfileInfoDTO(
    @SerializedName("roomCountRanges")
    val roomCountRanges: List<CountRangeDTO>,
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