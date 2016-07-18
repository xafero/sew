package com.xafero.sew;

import java.awt.Desktop;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xafero.sew.api.Weblet;
import com.xafero.sew.impl.Resource;

public class ManualTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		Weblet r = new Resource(ManualTest.class, "help");
		Wiki wiki = new Wiki(r);
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.submit(wiki);
		Thread.sleep(2 * 1000);
		Desktop.getDesktop().browse(wiki.getEndpoint());
		System.out.println("Try it!");
	}
}