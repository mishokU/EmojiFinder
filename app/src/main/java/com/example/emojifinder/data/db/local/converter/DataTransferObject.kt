package com.example.emojifinder.data.db.local.converter

import com.example.emojifinder.data.db.local.models.EmojiShopModelLocal
import com.example.emojifinder.data.db.local.models.SmallLevelModelLocal
import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import com.example.emojifinder.ui.categories.SmallLevelModel


fun List<EmojiShopModelLocal>.asEmojiShopModel() : List<EmojiShopModel> {
    return map {
        EmojiShopModel(
            title = it.levelTitle,
            unicode = it.unicode,
            x = it.x,
            y = it.y,
            order = it.order
        )
    }
}

fun List<EmojiShopModel>.asLocalModel(): List<EmojiShopModelLocal> {
    return map{
        EmojiShopModelLocal(
            levelTitle = it.title,
            x = it.x,
            y = it.y,
            order = it.order,
            unicode = it.unicode,
            id = 0
        )
    }
}

fun asLocalModel(levelModel: SmallLevelModel): SmallLevelModelLocal {
    return SmallLevelModelLocal(
        title = levelModel.title,
        time = levelModel.time,
        status = LevelStatus.WAITING,
        id = levelModel.id
    )
}

fun List<SmallLevelModelLocal>.asSmallLevelUI() : List<SmallLevelModel> {
    return map {
        SmallLevelModel(
            status = it.status,
            id = it.id,
            title = it.title,
            time = it.time

        )
    }
}