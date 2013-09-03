
package br.gov.lexml.renderer.pdf.renderer.decorator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.RendererPDFContext;

public class PDFDecoratorList implements PDFDecorator {

    private final List<PDFDecorator> decorators = new ArrayList<PDFDecorator>();

    public PDFDecoratorList() {
        // 
    }

    public PDFDecoratorList(final String classNames) throws Exception {

        if (StringUtils.isEmpty(classNames)) {
            return;
        }

        String[] classNameArray = classNames.split("[\\s,]+");
        for (String className : classNameArray) {
            Class< ? > clazz = Class.forName(className);
            if (!PDFDecorator.class.isAssignableFrom(clazz)) {
                throw new Exception("Classe " + className + " não implementa " + PDFDecorator.class.getName());
            }
            add((PDFDecorator) clazz.newInstance());
        }
    }

    public void add(final PDFDecorator decorator) {
        decorators.add(decorator);
    }

    @Override
    public void init(final RendererPDFContext ctx, final Element root) throws Exception {
        for (PDFDecorator decorator : decorators) {
            decorator.init(ctx, root);
        }
    }

    @Override
    public void beforeContent(final RendererPDFContext ctx, final Element root) throws Exception {
        for (PDFDecorator decorator : decorators) {
            decorator.beforeContent(ctx, root);
        }
    }

    @Override
    public void onEndPage(final RendererPDFContext ctx, final Element root) throws Exception {
        for (PDFDecorator decorator : decorators) {
            decorator.onEndPage(ctx, root);
        }
    }

    @Override
    public void afterContent(final RendererPDFContext ctx, final Element root) throws Exception {
        for (PDFDecorator decorator : decorators) {
            decorator.afterContent(ctx, root);
        }
    }

}
