package com.xafero.sew;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

import com.xafero.sew.api.Weblet;
import com.xafero.sew.net.WebServer;

public class Wiki implements Runnable, Closeable {

	private WebServer web;

	public Wiki(Weblet source) {
		this.web = new WebServer("localhost", 8080, source);
	}

	public URI getEndpoint() {
		return web.getEndpoint();
	}

	@Override
	public void run() {
		try {
			web.start();
		} catch (IOException e) {
			throw new RuntimeException("Could not start webserver!", e);
		}
	}

	@Override
	public void close() throws IOException {
		web.close();
	}
}