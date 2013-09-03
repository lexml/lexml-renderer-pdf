
package br.gov.lexml.renderer.pdf.renderer.base;

import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.renderer.Renderer;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Phrase;

public abstract class Renderer_Phrase extends Renderer_inline {

    @Override
    public boolean render(final Element el) throws Exception {        	    	
        DecoratedPhrase p = new DecoratedPhrase();        
        formatPhrase(el, p);
       	ctx.pushContainer(p);
        return Renderer.NAO_ACABOU;
    }

    @Override
    public void close() throws Exception {
        Phrase p = (Phrase) ctx.popContainer();
        addToContainer(p);
    }

    protected abstract void formatPhrase(Element el, DecoratedPhrase p) throws Exception;

}
