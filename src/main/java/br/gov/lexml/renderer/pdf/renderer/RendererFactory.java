
package br.gov.lexml.renderer.pdf.renderer;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.RendererPDFContext;

public class RendererFactory {

    private static final Map<String , Class<Renderer>> map = new HashMap<String , Class<Renderer>>();

    private static final String PACKAGE_NAME = RendererFactory.class.getPackage().getName() + ".element";

    @SuppressWarnings("unchecked")
    public static final Renderer createRenderer(final Element el, final RendererPDFContext ctx) throws Exception {

        String elName = el.getName();
        String clazzName = PACKAGE_NAME + ".Renderer_" + elName;

        if (!map.containsKey(elName)) {
            try {
                Class< ? > loadedClazz = Class.forName(clazzName);
                if (Renderer.class.isAssignableFrom(loadedClazz)) {
                    map.put(elName, (Class<Renderer>) loadedClazz);
                }
                else {
                    throw new RuntimeException("Classe " + clazzName + " não implementa "
                        + Renderer.class.getName());
                }
            }
            catch (ClassNotFoundException e) {
                map.put(elName, null);
            }
        }

        Class<Renderer> clazz = map.get(elName);
        if (clazz != null) {
            Renderer renderer = clazz.newInstance();
            renderer.setContext(ctx);
            return renderer;
        }

        return null;
    }
}
