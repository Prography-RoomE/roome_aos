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


data class ResponseProfileDTO(
    @SerializedName("activity") val activity: ActivitiesDTO,
    @SerializedName("color") val color: ColorsDTO,
    @SerializedName("count") val count: String,
    @SerializedName("deviceLockPreference") val deviceLockPreference: DeviceLockPreferencesDTO,
    @SerializedName("hintUsagePreference") val hintUsagePreference: HintUsagePreferencesDTO,
    @SerializedName("horrorThemePosition") val horrorThemePosition: HorrorThemePositionsDTO,
    @SerializedName("id") val id: Int,
    @SerializedName("isPlusEnabled") val isPlusEnabled: Boolean,
    @SerializedName("mbti") val mbti: String,
    @SerializedName("preferredGenres") val preferredGenres: List<GenresDTO>,
    @SerializedName("state") val state: String,
    @SerializedName("themeDislikedFactors") val themeDislikedFactors: List<DislikedFactorsDTO>,
    @SerializedName("themeImportantFactors") val themeImportantFactors: List<ImportantFactorsDTO>,
    @SerializedName("userStrengths") val userStrengths: List<StrengthsDTO>
)