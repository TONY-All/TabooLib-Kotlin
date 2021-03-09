package io.izzel.taboolib.kotlin.kether.action

import io.izzel.kether.common.api.ParsedAction
import io.izzel.kether.common.api.QuestAction
import io.izzel.kether.common.api.QuestContext
import io.izzel.kether.common.loader.types.ArgTypes
import io.izzel.taboolib.kotlin.kether.Kether.expects
import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import java.util.concurrent.CompletableFuture

/**
 * @author IzzelAliz
 */
class ActionJoin(val source: List<ParsedAction<*>>, val separator: String) : QuestAction<String>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val array = ArrayList<Any>()
        fun process(cur: Int) {
            if (cur < source.size) {
                context.newFrame(source[cur]).run<Any>().thenApply {
                    array.add(it)
                    process(cur + 1)
                }
            } else {
                future.complete(array.joinToString(separator))
            }
        }
        process(0)
        return future;
    }

    override fun toString(): String {
        return "ActionJoin(source='$source')"
    }

    companion object {

        /**
         * join [ *1 *2 *3 ] by -
         * join [ *a *b *c ] with -
         */
        @KetherParser(["join"])
        fun parser() = ScriptParser.parser {
            val source = it.next(ArgTypes.listOf(ArgTypes.ACTION))
            ActionJoin(
                source, try {
                    it.mark()
                    it.expects("by", "with")
                    it.nextToken()
                } catch (ignored: Exception) {
                    it.reset()
                    " "
                }
            )
        }
    }
}