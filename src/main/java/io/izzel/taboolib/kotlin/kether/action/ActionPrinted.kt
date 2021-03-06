package io.izzel.taboolib.kotlin.kether.action

import io.izzel.kether.common.api.ParsedAction
import io.izzel.kether.common.api.QuestAction
import io.izzel.kether.common.api.QuestContext
import io.izzel.kether.common.loader.types.ArgTypes
import io.izzel.taboolib.kotlin.kether.Kether.expects
import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.toPrinted
import java.util.concurrent.CompletableFuture


/**
 * @author IzzelAliz
 */
class ActionPrinted(val date: ParsedAction<*>, val separator: String) : QuestAction<List<String>>() {

    override fun process(frame: QuestContext.Frame): CompletableFuture<List<String>> {
        return frame.newFrame(date).run<Any>().thenApply {
            it.toString().toPrinted(separator)
        }
    }

    override fun toString(): String {
        return "ActionPrinted(date=$date, separator='$separator')"
    }

    companion object {

        /**
         * printed *xxx by "_"
         */
        @KetherParser(["printed"])
        fun parser() = ScriptParser.parser {
            ActionPrinted(it.next(ArgTypes.ACTION), try {
                it.mark()
                it.expects("by", "with")
                it.nextToken()
            } catch (ignored: Exception) {
                it.reset()
                "_"
            })
        }
    }
}