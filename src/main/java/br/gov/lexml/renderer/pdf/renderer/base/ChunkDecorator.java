package br.gov.lexml.renderer.pdf.renderer.base;

import com.itextpdf.text.Chunk;

public interface ChunkDecorator {
	Chunk decorate(Chunk c);
}
