
package br.gov.lexml.renderer.pdf.renderer.decorator;

import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.RendererPDFContext;

public interface PDFDecorator {

    void init(RendererPDFContext ctx, Element root) throws Exception;

    void beforeContent(RendererPDFContext ctx, Element root) throws Exception;

    void onEndPage(RendererPDFContext ctx, Element root) throws Exception;

    void afterContent(RendererPDFContext ctx, Element root) throws Exception;

}
