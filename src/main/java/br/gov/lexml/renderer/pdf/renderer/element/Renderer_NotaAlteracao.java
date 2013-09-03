
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.renderer.base.Renderer_inline;

public class Renderer_NotaAlteracao extends Renderer_inline {

    private boolean failToLastAddedContainer;

    @Override
    public boolean render(final Element el) throws Exception {
        failToLastAddedContainer = ctx.isFailToLastAddedContainer();
        ctx.setFailToLastAddedContainer(true);
        return super.render(el);
    }

    @Override
    public void close() throws Exception {
        ctx.setFailToLastAddedContainer(failToLastAddedContainer);
        super.close();
    }

}
