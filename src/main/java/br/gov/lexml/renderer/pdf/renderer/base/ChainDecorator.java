package br.gov.lexml.renderer.pdf.renderer.base;

import com.itextpdf.text.Chunk;

public class ChainDecorator implements ChunkDecorator {

	private final ChunkDecorator inner;
	private final ChunkDecorator outer;
		
	public ChainDecorator(ChunkDecorator inner, ChunkDecorator outer) {
		super();
		this.inner = inner;
		this.outer = outer;
	}



	@Override
	public Chunk decorate(Chunk c) {
		return outer.decorate(inner.decorate(c));
	}

	public String toString() {
		return inner + " -> " + outer;
	}
}
