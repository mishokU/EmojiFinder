package com.mishok.emojifinder.data.db.local.converter

import com.mishok.emojifinder.data.db.local.models.EmojiShopModelLocal
import com.mishok.emojifinder.data.db.local.models.SmallLevelModelLocal
import com.mishok.emojifinder.data.db.remote.models.EmojiShopModel
import com.mishok.emojifinder.ui.categories.SmallLevelModel


fun List<EmojiShopModelLocal>.asEmojiShopModel(): List<EmojiShopModel> {
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
    return map {
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
        status = LevelStatus.SAVED,
        id = levelModel.id,
        url = levelModel.url
    )
}

fun List<SmallLevelModelLocal>.asSmallLevelUI(): List<SmallLevelModel> {
    return map {
        SmallLevelModel(
            status = it.status,
            id = it.id,
            title = it.title,
            time = it.time,
            url = it.url
        )
    }
}

fun List<CharSequence>.toEmojiShopModel(): List<EmojiShopModel> {
    return map {
        EmojiShopModel(
            unicode = it.toString(),
            title = "",
            order = 0,
            x = 0,
            y = 0
        )
    }
}