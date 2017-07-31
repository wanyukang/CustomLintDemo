package com.ljfxyj2008.CustomLint.lintrules.detectors;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.*;
import lombok.ast.AstVisitor;
import lombok.ast.MethodInvocation;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class ShouldUseCustomCommonUtilsLogDetector extends Detector implements Detector.JavaPsiScanner {

    private static final String ISSUE_ID = "CUSTOMLOGUSAGE";
    private static final String ISSUE_DESCRIPTION = "使用xx罗格";
    private static final String ISSUE_EXPLANATION = "在xx上班就得用xx罗格！";
    private static final Category ISSUE_CATEGORY = Category.CORRECTNESS;
    private static final int ISSUE_PRIORITY = 8;
    private static final Severity ISSUE_SEVERITY = Severity.WARNING;
    private static final Class<? extends Detector> DETECTOR_CLASS = ShouldUseCustomCommonUtilsLogDetector.class;
    private static final EnumSet<Scope> DETECTOR_SCOPE = Scope.JAVA_FILE_SCOPE;
    private static final Implementation IMPLEMENTATION = new Implementation(
            DETECTOR_CLASS,
            DETECTOR_SCOPE
    );

    public static final Issue LOGISSUE = Issue.create(
            ISSUE_ID,
            ISSUE_DESCRIPTION,
            ISSUE_EXPLANATION,
            ISSUE_CATEGORY,
            ISSUE_PRIORITY,
            ISSUE_SEVERITY,
            IMPLEMENTATION);

    private static final String IS_LOGGABLE = "isLoggable";       //$NON-NLS-1$
    private static final String LOG_CLS = "android.util.Log";     //$NON-NLS-1$
    private static final String PRINTLN = "println";              //$NON-NLS-1$


    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("v", "d", "i", "w", "e", PRINTLN, IS_LOGGABLE);
    }

    @Override
    public void visitMethod(@NonNull JavaContext context, @Nullable AstVisitor visitor, @NonNull MethodInvocation node) {

        JavaParser.ResolvedNode resolved = context.resolve(node);
        JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod) resolved;

        if (method.getContainingClass().matches(LOG_CLS)) {
            context.report(
                    LOGISSUE,
                    node,
                    context.getLocation(node),
                    ISSUE_EXPLANATION);
        }
    }
}