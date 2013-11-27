package com.github.rholder.jvm.loopunswitching;

import java.io.Serializable;

interface Bean {
    public int getVersion();
}

interface Bean2 extends Bean {
}

public class TestBean implements Bean2, Cloneable, Serializable {

    private static final long serialVersionUID = -3869795591041535538L;

    public int version = 0;
    public String url = null;

    public TestBean(int version, String url) {
        this.version = version;
        this.url = url;
    }

    public int getVersion(){
        return version;
    }
}
