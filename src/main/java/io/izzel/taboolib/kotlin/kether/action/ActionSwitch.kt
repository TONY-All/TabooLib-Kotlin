package io.izzel.taboolib.kotlin.kether.action

import io.izzel.kether.common.api.ParsedAction
import io.izzel.kether.common.api.QuestAction
import io.izzel.kether.common.api.QuestContext
import io.izzel.kether.common.loader.types.ArgTypes
import io.izzel.taboolib.kotlin.kether.ScriptContext
import io.izzel.taboolib.kotlin.kether.ScriptParser
import org.bukkit.Bukkit
import java.util.concurrent.CompletableFuture

/**
 * @author IzzelAliz
 */
class ActionSwitch(val sender: ParsedAction<*>) : QuestAction<Void>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Void> {
        return context.newFrame(sender).run<Any>().thenAccept {
            if (it.toString() == "console") {
                (context as ScriptContext).sender = Bukkit.getConsoleSender()
            } else {
                (context as ScriptContext).sender = Bukkit.getPlayerExact(it.toString())
            }
        }
    }

    override fun toString(): String {
        return "ActionSwitch(sender=$sender)"
    }

    companion object {

        @Suppress("UnstableApiUsage")
        fun parser() = ScriptParser.parser {
            ActionSwitch(it.next(ArgTypes.ACTION))
        }
    }
}