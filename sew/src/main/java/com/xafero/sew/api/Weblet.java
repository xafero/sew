package com.xafero.sew.api;

import java.io.Closeable;
import java.io.IOException;

public interface Weblet extends Closeable {

	String getTextContent(String uri, String query) throws IOException;

}