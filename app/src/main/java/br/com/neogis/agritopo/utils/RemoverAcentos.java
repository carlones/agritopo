package br.com.neogis.agritopo.utils;

// Tem também a opção de usar << Pattern.compile("\\p{InCombiningDiacriticalMarks}+") >>
// só que é mais lerdo: pesquisando por "w" na lista de MG, leva 136ms, e o método abaixo demora 12 ms

// http://www.guj.com.br/t/acentos-no-java/41869/3
//
public class RemoverAcentos {
    static String acentuado = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
    static String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";
    static char[] tabela;
    static {
        tabela = new char[256];
        for (int i = 0; i < tabela.length; ++i) {
            tabela [i] = (char) i;
        }
        for (int i = 0; i < acentuado.length(); ++i) {
            tabela [acentuado.charAt(i)] = semAcento.charAt(i);
        }
    }
    public static String remover (final String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt (i);
            if (ch < 256) {
                sb.append (tabela [ch]);
            } else {
                sb.append (ch);
            }
        }
        return sb.toString();
    }
}