package com.sevenstars.data.mapper.profile

import com.sevenstars.data.model.profile.ResponseProfileInfoDTO
import com.sevenstars.data.model.profile.info.ActivitiesDTO
import com.sevenstars.data.model.profile.info.ColorsDTO
import com.sevenstars.data.model.profile.info.DeviceLockPreferencesDTO
import com.sevenstars.data.model.profile.info.DislikedFactorsDTO
import com.sevenstars.data.model.profile.info.GenresDTO
import com.sevenstars.data.model.profile.info.HintUsagePreferencesDTO
import com.sevenstars.data.model.profile.info.HorrorThemePositionsDTO
import com.sevenstars.data.model.profile.info.ImportantFactorsDTO
import com.sevenstars.data.model.profile.info.StrengthsDTO
import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.model.profile.info.Activities
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.domain.model.profile.info.DeviceLockPreferences
import com.sevenstars.domain.model.profile.info.DislikedFactors
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.domain.model.profile.info.Strengths


object ProfileInfoMapper {
    fun mapperToResponseEntity(item: ResponseProfileInfoDTO): ProfileInfoEntity {
        return item.run {
            ProfileInfoEntity(
                activities = activitiesMapper(this.activities),
                colors = colorsMapper(this.colors),
                genres = genresMapper(this.genres),
                strengths = strengthsMapper(this.strengths),
                importantFactors = importantFactorsMapper(this.importantFactors),
                horrorThemePositions = horrorThemeMapper(this.horrorThemePositions),
                hintUsagePreferences = hintUsagePreferencesMapper(this.hintUsagePreferences),
                deviceLockPreferences = deviceLockPreferencesMapper(this.deviceLockPreferences),
                dislikedFactors = dislikedFactorsMapper(this.dislikedFactors)
            )
        }
    }

    private fun activitiesMapper(item: List<ActivitiesDTO>): List<Activities> {
        return item.map {
            Activities(it.id, it.title, it.description)
        }
    }

    private fun colorsMapper(item: List<ColorsDTO>): List<Colors> {
        return item.map {
            Colors(it.id, it.title)
        }
    }

    private fun genresMapper(item: List<GenresDTO>): List<Genres> {
        return item.map {
            Genres(it.id, it.title)
        }
    }

    private fun strengthsMapper(item: List<StrengthsDTO>): List<Strengths> {
        return item.map {
            Strengths(it.id, it.title)
        }
    }

    private fun importantFactorsMapper(item: List<ImportantFactorsDTO>): List<ImportantFactors> {
        return item.map {
            ImportantFactors(it.id, it.title)
        }
    }

    private fun horrorThemeMapper(item: List<HorrorThemePositionsDTO>): List<HorrorThemePositions> {
        return item.map {
            HorrorThemePositions(it.id, it.title, it.description)
        }
    }

    private fun hintUsagePreferencesMapper(item: List<HintUsagePreferencesDTO>): List<HintUsagePreferences> {
        return item.map {
            HintUsagePreferences(it.id, it.title, it.description)
        }
    }

    private fun deviceLockPreferencesMapper(item: List<DeviceLockPreferencesDTO>): List<DeviceLockPreferences> {
        return item.map {
            DeviceLockPreferences(it.id, it.title)
        }
    }

    private fun dislikedFactorsMapper(item: List<DislikedFactorsDTO>): List<DislikedFactors> {
        return item.map {
            DislikedFactors(it.id, it.title)
        }
    }
}