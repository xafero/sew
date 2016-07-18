package com.xafero.sew.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import com.xafero.sew.api.Weblet;
import com.xafero.sew.impl.Markdown;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class WebServer extends NanoHTTPD implements Closeable {

	private final Weblet container;
	private final URI endpoint;

	public WebServer(String hostname, int port, Weblet source) {
		super(hostname, port);
		endpoint = URI.create(String.format("http://%s:%s", hostname, port));
		container = new Markdown(source);
	}

	public URI getEndpoint() {
		return endpoint;
	}

	public void start() throws IOException {
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}

	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		String query = session.getQueryParameterString();
		if (uri.equals("/"))
			uri = "/" + Defaults.START_PAGE;
		if (query == null)
			query = "";
		return serve(session, uri, query);
	}

	protected Response serve(IHTTPSession session, String uri, String query) {
		String content;
		IStatus status = null;
		try {
			content = container.getTextContent(uri, query);
		} catch (Exception e) {
			status = Status.INTERNAL_ERROR;
			try (StringWriter w = new StringWriter()) {
				e.printStackTrace(new PrintWriter(w));
				content = w.toString();
			} catch (IOException e1) {
				content = e.getMessage();
			}
		}
		Response resp = newFixedLengthResponse(content);
		if (status != null)
			resp.setStatus(status);
		return resp;
	}

	@Override
	public void close() throws IOException {
		closeAllConnections();
		stop();
	}
}