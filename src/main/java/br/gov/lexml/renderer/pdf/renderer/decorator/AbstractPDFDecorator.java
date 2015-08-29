
package br.gov.lexml.renderer.pdf.renderer.decorator;

import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.RendererPDFContext;

public abstract class AbstractPDFDecorator implements PDFDecorator {

    @Override
    public void init(final RendererPDFContext ctx, final Element root) throws Exception {
        // Não faz nada
    }

    @Override
    public void beforeContent(final RendererPDFContext ctx, final Element root) throws Exception {
        // Não faz nada
    }

    @Override
    public void onEndPage(final RendererPDFContext ctx, final Element root) throws Exception {
        // Não faz nada
    }

    @Override
    public void afterContent(final RendererPDFContext ctx, final Element root) throws Exception {
        // Não faz nada
    }

}
