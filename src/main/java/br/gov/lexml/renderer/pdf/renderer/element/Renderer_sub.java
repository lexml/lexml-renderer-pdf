
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Chunk;

import br.gov.lexml.renderer.pdf.renderer.base.AbstractRenderer;
import br.gov.lexml.renderer.pdf.renderer.base.ChunkDecorator;
import br.gov.lexml.renderer.pdf.renderer.base.DecoratedPhrase;
import br.gov.lexml.renderer.pdf.renderer.base.Renderer_Phrase;

public class Renderer_sub extends Renderer_Phrase {

    private static final Logger log = LoggerFactory.getLogger(Renderer_sub.class);

    @Override
	protected void formatPhrase(final Element el, final DecoratedPhrase p)
			throws Exception {
		p.addDecorator(new ChunkDecorator() {
			@Override
			public Chunk decorate(Chunk c) {
				c.setTextRise(-3f);
				return c;
			}			
		});
	}

}
