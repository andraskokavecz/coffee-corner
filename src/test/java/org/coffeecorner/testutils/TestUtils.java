package org.coffeecorner.testutils;

import java.io.File;

public final class TestUtils {

    public static void cleanFolder(File folder) {
        for (File file : folder.listFiles()) {
            file.delete();
        }
    }
}
