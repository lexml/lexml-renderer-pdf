package br.gov.lexml.renderer.pdf.renderer.base;

import com.itextpdf.text.Chunk;

public class IdentityDecorator implements ChunkDecorator {

	@Override
	public Chunk decorate(Chunk c) {
		return c;
	}
	
	public String toString() {
		return "[IdentityDecorator]";
	}

}
