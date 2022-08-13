package net.zip4j.model;

import java.io.File;

public interface ExcludeFileFilter {

    boolean isExcluded(File file);

}
