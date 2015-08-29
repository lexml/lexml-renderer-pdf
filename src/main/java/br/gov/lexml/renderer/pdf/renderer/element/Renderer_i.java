
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.renderer.base.DecoratedPhrase;
import br.gov.lexml.renderer.pdf.renderer.base.Renderer_Phrase;

import com.itextpdf.text.Font;

public class Renderer_i extends Renderer_Phrase {

    @Override
    protected void formatPhrase(final Element el, final DecoratedPhrase p) throws Exception {
        int style = hasAncestor(el, "b") ? Font.BOLDITALIC : Font.ITALIC;
        p.setFont(ctx.getFont(style));
    }

}
