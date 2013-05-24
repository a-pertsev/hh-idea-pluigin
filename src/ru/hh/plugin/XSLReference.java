package ru.hh.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nullable;


public class XSLReference implements PsiReference {
    protected PsiElement element;
    protected TextRange textRange;
    protected Project project;
    protected String path;
    protected VirtualFile appDir;

    public XSLReference(String path, PsiElement element, TextRange textRange, Project project, VirtualFile appDir ) {
        this.element = element;
        this.project = project;
        this.path = path;
        this.appDir = appDir;
        this.textRange = textRange;
    }

    @Override
    public String toString() {
        return getCanonicalText();
    }

    public PsiElement getElement() {
        return this.element ;
    }

    public TextRange getRangeInElement() {
        return textRange;
    }

    public PsiElement handleElementRename(String newElementName)
            throws IncorrectOperationException {
        // TODO: Implement this method
        throw new IncorrectOperationException();
    }

    public PsiElement bindToElement(PsiElement element) throws IncorrectOperationException {
        // TODO: Implement this method
        throw new IncorrectOperationException();
    }

    public boolean isReferenceTo(PsiElement element) {
        return resolve() == element;
    }

    public Object[] getVariants() {
        // TODO: Implement this method
        return new Object[0];
    }

    public boolean isSoft() {
        return false;
    }

    @Nullable
    public PsiElement resolve() {
        VirtualFile targetFile = appDir.findFileByRelativePath(path);

        if ( targetFile != null ){
            return  PsiManager.getInstance(project).findFile(targetFile);
        }

        return null;
    }

    @Override
    public String getCanonicalText() {
        return path;
    }

}
