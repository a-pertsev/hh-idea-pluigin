package ru.hh.plugin;


import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;


public class XSLPsiReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        XSLPsiReferenceProvider provider = new XSLPsiReferenceProvider();

        registrar.registerReferenceProvider(StandardPatterns.instanceOf(PsiElement.class), provider);
    }
}

