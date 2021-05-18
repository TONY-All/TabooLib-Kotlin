package io.izzel.taboolib.kotlin.kether.action.bukkit

import io.izzel.kether.common.api.ParsedAction
import io.izzel.kether.common.api.QuestAction
import io.izzel.kether.common.api.QuestContext
import io.izzel.kether.common.loader.types.ArgTypes
import io.izzel.taboolib.kotlin.kether.*
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * @author IzzelAliz
 */
class ActionPlayer(val operator: PlayerOperator, val symbol: Symbol, val value: ParsedAction<*>?) : QuestAction<Any?>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Any?> {
        val viewer = (context.context() as ScriptContext).sender as? Player ?: throw RuntimeException("No player selected.")
        return if (value != null) {
            context.newFrame(value).run<Any>().thenApplyAsync({
                try {
                    operator.write?.invoke(viewer, symbol, it)
                } catch (ex: NoSuchMethodError) {
                    viewer.sendMessage("§cOperator not supported. (${ex.localizedMessage})")
                } catch (ex: NoSuchFieldError) {
                    viewer.sendMessage("§cOperator not supported. (${ex.localizedMessage})")
                }
            }, context.context().executor)
        } else {
            CompletableFuture.completedFuture(operator.read?.invoke(viewer))
        }
    }

    override fun toString(): String {
        return "ActionPlayer(operator=$operator, action=$symbol, value=$value)"
    }

    companion object {

        init {
            PlayerOperators.values().forEach {
                Kether.addPlayerOperator(it.name, it.operator)
            }
        }

        @KetherParser(["player"])
        fun parser() = ScriptParser.parser {
            it.mark()
            val tokens = arrayListOf(it.nextToken())
            val structure = Kether.operatorsPlayer.entries.firstOrNull { e ->
                val args = e.key.toLowerCase().split("_")
                var i = 0
                args.all { a ->
                    if (tokens.size < ++i) {
                        tokens.add(it.nextToken())
                    }
                    tokens[i - 1] == a
                }
            } ?: throw KetherError.NOT_PLAYER_OPERATOR.create(tokens.joinToString(" "))
            it.reset()
            structure.key.split("_").forEach { _ ->
                it.nextToken()
            }
            it.mark()
            val action = if (it.hasNext()) {
                when (it.nextToken()) {
                    "to" -> Symbol.SET
                    "add", "increase" -> Symbol.ADD
                    else -> {
                        it.reset()
                        Symbol.NONE
                    }
                }
            } else {
                Symbol.NONE
            }
            if (action != Symbol.NONE) {
                ActionPlayer(structure.value, action, it.next(ArgTypes.ACTION))
            } else {
                ActionPlayer(structure.value, action, null)
            }
        }
    }
}