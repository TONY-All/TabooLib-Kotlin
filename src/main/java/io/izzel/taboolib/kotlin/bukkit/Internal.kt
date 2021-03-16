package io.izzel.taboolib.kotlin.bukkit

import io.izzel.taboolib.module.inject.TInject
import org.bukkit.entity.Player

abstract class Internal {

    val uniqueColors = listOf(
        "§黒",
        "§黓",
        "§黔",
        "§黕",
        "§黖",
        "§黗",
        "§默",
        "§黙",
        "§黚",
        "§黛",
        "§黜",
        "§黝",
        "§點",
        "§黟",
        "§黠",
        "§黡",
        "§黢",
        "§黣",
        "§黤",
        "§黥",
        "§黦",
        "§黧",
        "§黨",
        "§黩",
        "§黪",
        "§黫",
        "§黬",
        "§黭",
    )

    abstract fun setupScoreboard(player: Player, remove: Boolean)

    abstract fun setDisplayName(player: Player, title: String)

    abstract fun changeContent(player: Player, content: List<String>, lastContent: Map<Int, String>)

    abstract fun display(player: Player)

    companion object {

        @TInject(asm = "io.izzel.taboolib.kotlin.bukkit.InternalImpl")
        lateinit var INSTANCE: Internal
            private set
    }
}