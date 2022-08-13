package net.zip4j.io.inputstream;

import net.zip4j.crypto.StandardDecrypter;
import net.zip4j.model.LocalFileHeader;

import java.io.IOException;

import net.zip4j.util.InternalZipConstants;

class ZipStandardCipherInputStream extends CipherInputStream<StandardDecrypter> {

  public ZipStandardCipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader,
                                      char[] password, int bufferSize, boolean useUtf8ForPassword) throws IOException {
    super(zipEntryInputStream, localFileHeader, password, bufferSize, useUtf8ForPassword);
  }

  @Override
  protected StandardDecrypter initializeDecrypter(LocalFileHeader localFileHeader, char[] password,
                                                  boolean useUtf8ForPassword) throws IOException {
    return new StandardDecrypter(password, localFileHeader.getCrc(), localFileHeader.getLastModifiedTime(),
        getStandardDecrypterHeaderBytes(), useUtf8ForPassword);
  }

  private byte[] getStandardDecrypterHeaderBytes() throws IOException {
    byte[] headerBytes = new byte[InternalZipConstants.STD_DEC_HDR_SIZE];
    readRaw(headerBytes);
    return headerBytes;
  }
}
