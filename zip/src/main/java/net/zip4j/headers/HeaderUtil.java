package net.zip4j.headers;

import net.zip4j.exception.ZipException;
import net.zip4j.model.FileHeader;
import net.zip4j.model.ZipModel;
import net.zip4j.util.InternalZipConstants;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.zip4j.util.Zip4jUtil;

public class HeaderUtil {

  public static FileHeader getFileHeader(ZipModel zipModel, String fileName) throws ZipException {
    FileHeader fileHeader = getFileHeaderWithExactMatch(zipModel, fileName);

    if (fileHeader == null) {
      fileName = fileName.replaceAll("\\\\", "/");
      fileHeader = getFileHeaderWithExactMatch(zipModel, fileName);

      if (fileHeader == null) {
        fileName = fileName.replaceAll("/", "\\\\");
        fileHeader = getFileHeaderWithExactMatch(zipModel, fileName);
      }
    }

    return fileHeader;
  }

  public static String decodeStringWithCharset(byte[] data, boolean isUtf8Encoded, Charset charset) {
    if (charset != null) {
      return new String(data, charset);
    }

    if (isUtf8Encoded) {
      return new String(data, InternalZipConstants.CHARSET_UTF_8);
    }

    try {
      return new String(data, InternalZipConstants.ZIP_STANDARD_CHARSET_NAME);
    } catch (UnsupportedEncodingException e) {
      return new String(data);
    }
  }

  public static byte[] getBytesFromString(String string, Charset charset) {
    if (charset == null) {
      return string.getBytes(InternalZipConstants.ZIP4J_DEFAULT_CHARSET);
    }

    return string.getBytes(charset);
  }

  public static long getOffsetStartOfCentralDirectory(ZipModel zipModel) {
    if (zipModel.isZip64Format()) {
      return zipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber();
    }

    return zipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
  }

  public static List<FileHeader> getFileHeadersUnderDirectory(List<FileHeader> allFileHeaders, String fileName) {
    List<FileHeader> fileHeadersUnderDirectory = new ArrayList<>();
    for (FileHeader fileHeader : allFileHeaders) {
      if (fileHeader.getFileName().startsWith(fileName)) {
        fileHeadersUnderDirectory.add(fileHeader);
      }
    }
    return fileHeadersUnderDirectory;
  }

  public static long getTotalUncompressedSizeOfAllFileHeaders(List<FileHeader> fileHeaders) {
    long totalUncompressedSize = 0;
    for (FileHeader fileHeader : fileHeaders) {
      if (fileHeader.getZip64ExtendedInfo() != null &&
          fileHeader.getZip64ExtendedInfo().getUncompressedSize() > 0) {
        totalUncompressedSize += fileHeader.getZip64ExtendedInfo().getUncompressedSize();
      } else {
        totalUncompressedSize += fileHeader.getUncompressedSize();
      }
    }
    return totalUncompressedSize;
  }

  private static FileHeader getFileHeaderWithExactMatch(ZipModel zipModel, String fileName) throws ZipException {
    if (zipModel == null) {
      throw new ZipException("zip model is null, cannot determine file header with exact match for fileName: "
          + fileName);
    }

    if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
      throw new ZipException("file name is null, cannot determine file header with exact match for fileName: "
          + fileName);
    }

    if (zipModel.getCentralDirectory() == null) {
      throw new ZipException("central directory is null, cannot determine file header with exact match for fileName: "
          + fileName);
    }

    if (zipModel.getCentralDirectory().getFileHeaders() == null) {
      throw new ZipException("file Headers are null, cannot determine file header with exact match for fileName: "
          + fileName);
    }

    if (zipModel.getCentralDirectory().getFileHeaders().size() == 0) {
      return null;
    }

    for (FileHeader fileHeader : zipModel.getCentralDirectory().getFileHeaders()) {
      String fileNameForHdr = fileHeader.getFileName();
      if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileNameForHdr)) {
        continue;
      }

      if (fileName.equals(fileNameForHdr)) {
        return fileHeader;
      }
    }

    return null;
  }
}
