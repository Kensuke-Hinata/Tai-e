/*
 * Bamboo - A Program Analysis Framework for Java
 *
 * Copyright (C) 2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Bamboo is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package bamboo.pta.analysis.context;

public enum DefaultContext implements Context {
    INSTANCE,
    ;

    @Override
    public int depth() {
        return 0;
    }

    @Override
    public Object element(int k) {
        throw new IllegalArgumentException(
                "Context " + this + " doesn't have " + k + "-th element");
    }

    @Override
    public String toString() {
        return "[]";
    }
}
