package br.gov.lexml.renderer.pdf.renderer.base;

import java.net.URL;

import com.itextpdf.text.Chunk;

public class AnchorDecorator implements ChunkDecorator {

	private final URL url;
			
	public AnchorDecorator(URL url) {
		super();
		this.url = url;
	}

	@Override
	public Chunk decorate(Chunk c) {
		c.setAnchor(url);
		return c;
	}
	
	@Override
	public String toString() {
		return "[AnchorDecorator url: " + url + "]";
	}

}
