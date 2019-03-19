package com.github.lsp4intellij.actions;

import com.github.lsp4intellij.IntellijLanguageClient;
import com.github.lsp4intellij.requests.ReformatHandler;
import com.intellij.codeInsight.actions.ReformatCodeAction;
import com.intellij.lang.LanguageFormatting;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

/**
 * Action overriding the default reformat action
 * Fallback to the default action if the language is already supported or not supported by any language server
 */
class LSPReformatAction extends ReformatCodeAction implements DumbAware {
    private Logger LOG = Logger.getInstance(LSPReformatAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null || project==null) {
            return;
        }
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (LanguageFormatting.INSTANCE.allForLanguage(file.getLanguage()).isEmpty() && IntellijLanguageClient
                .isExtensionSupported(file.getVirtualFile().getExtension())) {
            ReformatHandler.reformatFile(editor);
        } else {
            super.actionPerformed(e);
        }
    }

    @Override
    public void update(AnActionEvent event) {
        super.update(event);
    }
}