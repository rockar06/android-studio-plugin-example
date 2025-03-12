package com.example.demoplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class GreetingAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        Messages.showMessageDialog(
            event.project,
            "Hello, world!",
            "My First Action",
            Messages.getInformationIcon()
        )
    }
}