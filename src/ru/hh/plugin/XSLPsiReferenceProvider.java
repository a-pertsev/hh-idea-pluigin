package ru.hh.plugin;


import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.diagnostic.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class XSLPsiReferenceProvider extends PsiReferenceProvider {
  private final ArrayList<VirtualFile> xslPlatforms = new ArrayList<VirtualFile>();
  private Boolean xslDirsInited = Boolean.FALSE;
  private VirtualFile appDir;
  private final Logger log = Logger.getInstance("MyLogger");


  private void InitXslPlatforms(Project project){
    xslDirsInited = Boolean.TRUE;

    PropertiesComponent properties = PropertiesComponent.getInstance(project);

    String dirName = properties.getValue("xslPath", "xhh/xsl/");

    appDir = project.getBaseDir().findFileByRelativePath(dirName);

    if (appDir == null) {
      return;
    }

    VirtualFile[] children = appDir.getChildren();

    for (VirtualFile file : children) {
      if (file.isDirectory()) {
        xslPlatforms.add(file);
      }
    }
  }


  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull final ProcessingContext context) {

    Project project = element.getProject();

    if (!xslDirsInited){
      InitXslPlatforms(project);
    }

    Class elementClass = element.getClass();
    String className = elementClass.getName();

    if (appDir == null || !className.endsWith("PyStringLiteralExpressionImpl")) {
      return PsiReference.EMPTY_ARRAY;
    }

    try {
      Method getStringMethod = elementClass.getMethod("getStringValue");
      Method getTextRangeMethod = elementClass.getMethod("getStringValueTextRange");

      String text = (String) getStringMethod.invoke(element);
      TextRange textRange = (TextRange) getTextRangeMethod.invoke(element);

      if (text.endsWith(".xsl"))  {
        int start = textRange.getStartOffset();
        int len = textRange.getLength();

        PsiReference ref = new XSLReference(text, element, new TextRange(start, start + len), project, appDir);
        PsiElement resolvedReference = ref.resolve();

        if (resolvedReference == null) {
          if (!xslPlatforms.isEmpty()) {
            ArrayList<PsiReference> platformReferences = new ArrayList<PsiReference>();

            for (VirtualFile platform : xslPlatforms) {
              PsiReference platformRef = new XSLReference(text, element, new TextRange(start, start + len), project, platform);
              PsiElement platformRefResolved = platformRef.resolve();
              if (platformRefResolved != null) {
                platformReferences.add(platformRef);
              }
            }
            if (!platformReferences.isEmpty()) {
              PsiReference[] links = new PsiReference[platformReferences.size()];
              return platformReferences.toArray(links);
            }
          }
        } else {
          return new PsiReference[]{ref};
        }
      }

    } catch (Exception e) {
      log.warn(e);
    }

    return PsiReference.EMPTY_ARRAY;
  }
}
