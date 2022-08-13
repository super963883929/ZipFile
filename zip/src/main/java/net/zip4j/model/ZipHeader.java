package net.zip4j.model;

import net.zip4j.headers.HeaderSignature;

public abstract class ZipHeader {

  private HeaderSignature signature;

  public HeaderSignature getSignature() {
    return signature;
  }

  public void setSignature(HeaderSignature signature) {
    this.signature = signature;
  }
}
