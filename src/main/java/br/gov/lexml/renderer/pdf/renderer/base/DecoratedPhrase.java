package br.gov.lexml.renderer.pdf.renderer.base;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;

public class DecoratedPhrase extends Phrase {

	private static final long serialVersionUID = 1L;

	private ChunkDecorator decorator = new IdentityDecorator();
	
	public DecoratedPhrase() { 
	}

	public DecoratedPhrase(Phrase phrase) {
		super(phrase);
	}

	public DecoratedPhrase(float leading) {
		super(leading);
	}

	public DecoratedPhrase(Chunk chunk) {
		super(chunk);
	}

	public DecoratedPhrase(String string) {
		super(string);
	}

	public DecoratedPhrase(float leading, Chunk chunk) {
		super(leading, chunk);
	}

	public DecoratedPhrase(String string, Font font) {
		super(string, font);
	}

	public DecoratedPhrase(float leading, String string) {
		super(leading, string);
	}

	public DecoratedPhrase(float leading, String string, Font font) {
		super(leading, string, font);
	}
	
	public DecoratedPhrase addDecorator(ChunkDecorator decorator) {		
		this.decorator = new ChainDecorator(decorator,this.decorator);
		return this;
	}
	
	@Override
	protected boolean addChunk(Chunk chunk) {	
		//System.out.println("Decorated phrase: add chunk: decorator = " + decorator + ", chunk = " + chunk);
		return super.addChunk(decorator.decorate(chunk));
	}
}
