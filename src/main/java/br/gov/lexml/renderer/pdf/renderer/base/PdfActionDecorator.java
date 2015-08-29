package br.gov.lexml.renderer.pdf.renderer.base;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfAction;

public class PdfActionDecorator implements ChunkDecorator {

	private final PdfAction action;
		
	public PdfActionDecorator(PdfAction action) {
		super();
		this.action = action;
	}

	@Override
	public Chunk decorate(Chunk c) {
		c.setAction(action);
		return c;
	}

	public String toString() {
		return "[PdfActionDecorator action: " + action + "]";
	}
}
