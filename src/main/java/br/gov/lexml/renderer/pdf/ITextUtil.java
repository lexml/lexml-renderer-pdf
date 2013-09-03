
package br.gov.lexml.renderer.pdf;

public class ITextUtil {

    // (1 in = 2.54 cm = 72 points)
    private static final float FATOR_CONVERSAO_CM_POINT = 72 / 2.54f;

    /**
     * Conversão de cm para point
     */
    public static float cm2point(final float cm) {
        return cm * FATOR_CONVERSAO_CM_POINT;
    }

    /**
     * Conversão de point para cm
     */
    public static float point2cm(final float point) {
        return point / FATOR_CONVERSAO_CM_POINT;
    }

    public static String normalizeSpaces(final String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\\s{2,}", " ");
    }

}
