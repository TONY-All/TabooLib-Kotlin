package io.izzel.taboolib.kotlin.kether.action

import io.izzel.kether.common.api.QuestAction
import io.izzel.kether.common.api.QuestContext
import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import java.util.concurrent.CompletableFuture

/**
 * @author IzzelAliz
 */
class ActionVariables() : QuestAction<List<String>>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<List<String>> {
        return CompletableFuture.completedFuture(context.variables().keys().toList())
    }

    override fun toString(): String {
        return "ActionVariables()"
    }

    companion object {

        @KetherParser(["vars", "variables"])
        fun parser() = ScriptParser.parser {
            ActionVariables()
        }
    }
}