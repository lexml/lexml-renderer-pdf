
package br.gov.lexml.renderer.pdf.renderer.font;

public class GentiumFontFactory extends LexmlFontFactory {

    public GentiumFontFactory(boolean allowUnderline) throws Exception {
        super("/fonts/GenBasR.ttf", "/fonts/GenBasB.ttf", "/fonts/GenBasI.ttf", "/fonts/GenBasBI.ttf",allowUnderline);
    }

}
