package com.xafero.sew.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.xafero.sew.api.Weblet;

public class Resource implements Weblet {

	private final ClassLoader loader;
	private final String prefix;

	public Resource(Class<?> type, String prefix) {
		this.loader = type.getClassLoader();
		this.prefix = prefix;
	}

	@Override
	public String getTextContent(String uri, String query) throws IOException {
		String path = prefix + uri;
		InputStream in = loader.getResourceAsStream(path);
		if (in == null)
			in = loader.getResourceAsStream(path + ".md");
		if (in == null)
			in = loader.getResourceAsStream(prefix + "/" + "error.md");
		return IOUtils.toString(in, "UTF8");
	}

	@Override
	public void close() throws IOException {
	}
}