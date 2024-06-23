package com.sevenstars.data.mapper.profile

import com.sevenstars.data.model.profile.ResponseProfileDTO
import com.sevenstars.data.model.profile.info.ActivitiesDTO
import com.sevenstars.data.model.profile.info.ColorsDTO
import com.sevenstars.data.model.profile.info.DeviceLockPreferencesDTO
import com.sevenstars.data.model.profile.info.HintUsagePreferencesDTO
import com.sevenstars.data.model.profile.info.HorrorThemePositionsDTO
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.model.profile.info.Activities
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.domain.model.profile.info.DeviceLockPreferences
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.domain.model.profile.info.HorrorThemePositions


object ProfileMapper {
    fun mapperToResponseEntity(item: ResponseProfileDTO): SavedProfileData {
        return item.run {
            SavedProfileData(
                activity = activityMapper(this.activity),
                color = colorMapper(this.color) ,
                count = this.count ,
                deviceLockPreference = deviceLockPreferenceMapper(this.deviceLockPreference) ,
                hintUsagePreference = hintUsagePreferenceMapper(this.hintUsagePreference) ,
                horrorThemePosition = horrorThemePositionMapper(this.horrorThemePosition) ,
                id = id ,
                isPlusEnabled = isPlusEnabled,
                mbti = mbti,
                preferredGenres = ProfileInfoMapper.genresMapper(this.preferredGenres),
                state = ProfileState.entries.find { it.desc == this.state } ?: ProfileState.ROOM_COUNT,
                themeDislikedFactors = ProfileInfoMapper.dislikedFactorsMapper(this.themeDislikedFactors),
                themeImportantFactors = ProfileInfoMapper.importantFactorsMapper(this.themeImportantFactors),
                userStrengths = ProfileInfoMapper.strengthsMapper(this.userStrengths)
            )
        }
    }

    private fun activityMapper(item: ActivitiesDTO?): Activities?{
        if(item == null) return null

        return item.run {
            Activities(id, title, description, text)
        }
    }

    private fun colorMapper(item: ColorsDTO?): Colors?{
        if(item == null) return null

        return item.run {
            Colors(direction, endColor, id, mode, shape, startColor, title)
        }
    }

    private fun hintUsagePreferenceMapper(item: HintUsagePreferencesDTO?): HintUsagePreferences?{
        if(item == null) return null

        return item.run {
            HintUsagePreferences(id, title, description, text)
        }
    }

    private fun deviceLockPreferenceMapper(item: DeviceLockPreferencesDTO?): DeviceLockPreferences?{
        if(item == null) return null

        return item.run {
            DeviceLockPreferences(id, title, description, text)
        }
    }

    private fun horrorThemePositionMapper(item: HorrorThemePositionsDTO?): HorrorThemePositions?{
        if(item == null) return null

        return item.run {
            HorrorThemePositions(id, title, description, text)
        }
    }
}
