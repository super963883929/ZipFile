package net.zip4j.util;

import net.zip4j.exception.ZipException;
import net.zip4j.io.inputstream.NumberedSplitFileInputStream;
import net.zip4j.io.inputstream.SplitFileInputStream;
import net.zip4j.io.inputstream.ZipInputStream;
import net.zip4j.io.inputstream.ZipStandardSplitFileInputStream;
import net.zip4j.model.FileHeader;
import net.zip4j.model.ZipModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class UnzipUtil {

  public static ZipInputStream createZipInputStream(ZipModel zipModel, FileHeader fileHeader, char[] password)
      throws IOException {

    SplitFileInputStream splitInputStream = null;
    try {
      splitInputStream = createSplitInputStream(zipModel);
      splitInputStream.prepareExtractionForFileHeader(fileHeader);

      ZipInputStream zipInputStream = new ZipInputStream(splitInputStream, password);
      if (zipInputStream.getNextEntry(fileHeader, false) == null) {
        throw new ZipException("Could not locate local file header for corresponding file header");
      }

      return zipInputStream;
    } catch (IOException e) {
      if (splitInputStream != null) {
        splitInputStream.close();
      }
      throw e;
    }
  }

  public static void applyFileAttributes(FileHeader fileHeader, File file) {

    try {
      Path path = file.toPath();
      FileUtils.setFileAttributes(path, fileHeader.getExternalFileAttributes());
      FileUtils.setFileLastModifiedTime(path, fileHeader.getLastModifiedTime());
    } catch (NoSuchMethodError e) {
      FileUtils.setFileLastModifiedTimeWithoutNio(file, fileHeader.getLastModifiedTime());
    }
  }

  public static SplitFileInputStream createSplitInputStream(ZipModel zipModel) throws IOException {
    File zipFile = zipModel.getZipFile();

    if (zipFile.getName().endsWith(InternalZipConstants.SEVEN_ZIP_SPLIT_FILE_EXTENSION_PATTERN)) {
      return new NumberedSplitFileInputStream(zipModel.getZipFile());
    }

    return new ZipStandardSplitFileInputStream(zipModel.getZipFile(), zipModel.isSplitArchive(),
        zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
  }

}
