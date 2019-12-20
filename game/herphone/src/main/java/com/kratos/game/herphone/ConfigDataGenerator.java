package com.kratos.game.herphone;

import com.kratos.engine.framework.scheme.tools.excel2code.BaseExcel2code;

import java.io.File;
import java.io.IOException;


public class ConfigDataGenerator extends BaseExcel2code {

    @org.junit.Test
    public void generateConfigData() throws IOException {
        File file = new File(System.getProperty("user.dir"));
        System.setProperty("user.dir", file.getParentFile().getParentFile().getPath());
        new ConfigDataGenerator().readExcel();
    }

    @Override
    protected String game() {
        return "herphone";
    }
}
