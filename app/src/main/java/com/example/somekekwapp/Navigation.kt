package com.example.somekekwapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

interface Screen {
     val tag: String

     val bundle: Bundle?

     fun create(): Fragment
}

sealed interface RouterCommand {
    data class Forward(val screen: Screen) : RouterCommand
    data class ForwardChain(val screens: List<Screen>) : RouterCommand

    data class Replace(val screen: Screen) : RouterCommand

    object Back : RouterCommand
    data class BackTo(val screen: Screen?) : RouterCommand
}

abstract class Router {

    open fun navigateTo(vararg screens: Screen) {
        if (screens.size == 1) {
            applyCommand(RouterCommand.Forward(screens.first()))
        } else {
            applyCommand(RouterCommand.ForwardChain(screens.toList()))
        }
    }

    open fun replace(screen: Screen) = applyCommand(RouterCommand.Replace(screen))
    open fun exit() = applyCommand(RouterCommand.Back)
    open fun backTo(screen: Screen?) = applyCommand(RouterCommand.BackTo(screen))

    protected abstract fun applyCommand(command: RouterCommand)

}

class RouterImpl : Router() {

    private val commandsChannel = Channel<RouterCommand>(capacity = BUFFER_CAPACITY)
    val commandsFlow = commandsChannel.receiveAsFlow()

    override fun applyCommand(command: RouterCommand) {
        commandsChannel.trySend(command)
    }

    companion object {
        private const val BUFFER_CAPACITY = 64
    }

}

class Navigator(
    private val activity: FragmentActivity,
    private val containerId: Int
) {

    private val fm = activity.supportFragmentManager

    fun applyCommand(command: RouterCommand) {
        when (command) {
            RouterCommand.Back -> back()
            is RouterCommand.BackTo -> backTo(command.screen)
            is RouterCommand.Forward -> forward(command.screen)
            is RouterCommand.ForwardChain -> forwardChain(command.screens)
            is RouterCommand.Replace -> replace(command.screen)
        }
    }

    private fun back() {
        if (fm.backStackEntryCount <= 1) {
            activity.finish()
            return
        }
        fm.popBackStack()
    }

    private fun backTo(screen: Screen?) {
        if (fm.backStackEntryCount == 1) {
            activity.finish()
            return
        }
        if (screen == null) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            fm.popBackStackImmediate(screen.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun forward(screen: Screen) {
        fm.commit {
            setReorderingAllowed(true)
            replace(containerId, screen.create(), screen.tag)
            addToBackStack(screen.tag)
        }
    }

    private fun forwardChain(screens: List<Screen>) {
        fm.commit {
            setReorderingAllowed(true)
            screens.forEach { screen ->
                replace(containerId, screen.create(), screen.tag)
                addToBackStack(screen.tag)
            }
        }
    }

    private fun replace(screen: Screen) {
        fm.popBackStack()
        fm.commit {
            setReorderingAllowed(true)
            replace(containerId, screen.create(), screen.tag)
            addToBackStack(screen.tag)
        }
    }

    private inline fun FragmentManager.commit(
        action: FragmentTransaction.() -> Unit
    ) {
        try {
            beginTransaction().apply(action).commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}