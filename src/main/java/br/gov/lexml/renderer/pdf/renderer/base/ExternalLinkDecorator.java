package br.gov.lexml.renderer.pdf.renderer.base;

import com.itextpdf.text.pdf.PdfAction;

public class ExternalLinkDecorator extends PdfActionDecorator {

	private final String url;
	
	public ExternalLinkDecorator(String url) {
		super(new PdfAction(url));
		this.url = url;
	}
	public String toString() {
		return "[ExternalLinkDecorator url: " + url + "]";
	}


}
