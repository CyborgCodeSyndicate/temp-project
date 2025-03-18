package com.theairebellion.zeus.ai.metadata.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class SignatureGenerator {

    private SignatureGenerator() {
    }


    public static String generateMethodSignature(Method method) {
        StringBuilder signature = new StringBuilder();

        String className = method.getDeclaringClass().getName();
        signature.append(className).append(".");

        signature.append(method.getName()).append("(");

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            String paramType = parameters[i].getType().getTypeName();
            signature.append(paramType);
            if (i < parameters.length - 1) {
                signature.append(", ");
            }
        }

        signature.append(")");
        return signature.toString();
    }

}
