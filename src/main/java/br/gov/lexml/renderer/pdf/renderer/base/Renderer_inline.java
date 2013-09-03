
package br.gov.lexml.renderer.pdf.renderer.base;

import org.dom4j.Element;

public abstract class Renderer_inline extends AbstractRenderer {

    @Override
    public boolean isMixed() {
        return true;
    }

    @Override
    public boolean render(final Element el) throws Exception {
        return NAO_ACABOU;
    }

}
