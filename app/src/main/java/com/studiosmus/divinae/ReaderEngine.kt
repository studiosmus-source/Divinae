package com.studiosmus.divinae

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SafeTextZone(
    val start: Dp,
    val end: Dp,
    val top: Dp,
    val bottom: Dp,
    val maxFontSp: Float = 15f,
    val minFontSp: Float = 12.5f
)

data class IllustratedPageTemplate(
    val id: String,
    val safeZone: SafeTextZone
)

data class SceneAudio(
    val id: String,
    val title: String,
    val sourceUrl: String
)

object DivinaeReaderEngine {
    // La tavola grafica non decide più dove va il testo: ogni template dichiara
    // una zona sicura. Il renderer usa solo quest'area.
    val cantoOne = IllustratedPageTemplate(
        id = "inferno_canto_01",
        safeZone = SafeTextZone(
            start = 112.dp,
            end = 72.dp,
            top = 52.dp,
            bottom = 128.dp
        )
    )

    // Mappa scena -> musica. Il contenuto può cambiare questa associazione
    // senza modificare il player o il renderer.
    val darkWood = SceneAudio(
        id = "dark_wood",
        title = "O frondens — Hildegard von Bingen",
        sourceUrl = "https://commons.wikimedia.org/wiki/Special:Redirect/file/O_frondens_2.ogg"
    )

    const val fireplaceUrl =
        "https://commons.wikimedia.org/wiki/Special:Redirect/file/Dry_grass_burning_in_open_fireplace.ogg"
}
