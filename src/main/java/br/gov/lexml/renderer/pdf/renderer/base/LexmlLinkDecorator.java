package br.gov.lexml.renderer.pdf.renderer.base;

public class LexmlLinkDecorator extends ExternalLinkDecorator {
	private final String urn;

	public LexmlLinkDecorator(String urn) {
		super("http://www.lexml.gov.br/urn/" + urn);
		this.urn = urn;
	}
	
	public String toString() {
		return "[LexmlLinkDecorator urn: " + urn + "]";
	}
	
}
