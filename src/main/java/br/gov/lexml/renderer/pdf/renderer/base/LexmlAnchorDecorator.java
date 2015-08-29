package br.gov.lexml.renderer.pdf.renderer.base;

import java.net.MalformedURLException;
import java.net.URL;

public class LexmlAnchorDecorator extends AnchorDecorator {

	private final String urn;

	public LexmlAnchorDecorator(String urn) throws MalformedURLException {
		super(new URL("http","www.lexml.gov.br", "/urn/" + urn));
		this.urn = urn;
	}
	
	@Override
	public String toString() {
		return "[LexmlAnchorDecorator urn: " + urn + "]";
	}
	
}
