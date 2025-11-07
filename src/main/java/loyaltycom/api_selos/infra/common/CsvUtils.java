package loyaltycom.api_selos.infra.common;

import java.util.Locale;

public class CsvUtils {
    public static String normalizarData(String raw) {
        String s = sanitizeDate(raw);
        if (s == null) {
            throw new IllegalArgumentException("Formato de data inválido: " + raw);
        }

        int space = s.indexOf(' ');
        int tpos = s.indexOf('T');
        int cut = (space >= 0 && tpos >= 0) ? Math.min(space, tpos)
                : (space >= 0 ? space : tpos);
        if (cut >= 0) s = s.substring(0, cut);

        s = s.replace('-', '/').replace('.', '/');

        s = stripWrappingQuotes(s);

        if (isDigits(s)) {
            if (s.length() == 8) {
                int a = parseInt(s, 0, 4);
                int b = parseInt(s, 4, 6);
                int c = parseInt(s, 6, 8);
                if (a >= 1900 && a <= 2100) {
                    return fmtYMD(a, b, c);
                } else {
                    int dd = parseInt(s, 0, 2);
                    int MM = parseInt(s, 2, 4);
                    int yy = parseInt(s, 4, 8);
                    return fmtYMD(yy, MM, dd);
                }
            }

            throw new IllegalArgumentException("Formato de data inválido: " + raw);
        }

        String[] p = s.split("/", -1);
        if (p.length == 3 && allDigits(p[0], p[1], p[2])) {
            int x = Integer.parseInt(p[0]);
            int y = Integer.parseInt(p[1]);
            int z = Integer.parseInt(p[2]);

            if (p[0].length() == 4 && x >= 1900 && x <= 2100) {
                return fmtYMD(x, y, z);
            }

            if (x > 12) {
                return fmtYMD(z, y, x);
            } else if (y > 12) {
                return fmtYMD(z, x, y);
            } else {
                return fmtYMD(z, y, x);
            }
        }

        throw new IllegalArgumentException("Formato de data inválido: " + raw);
    }

    private static String sanitizeDate(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;

        s = stripWrappingQuotes(s);

        s = s.replace('\u00A0', ' ').trim();

        String low = s.toLowerCase(Locale.ROOT);
        if (low.equals("null") || low.equals("n/a") || low.equals("--") || low.equals("-")) return null;

        return s;
    }

    private static String stripWrappingQuotes(String s) {
        while (s.length() >= 2) {
            char first = s.charAt(0);
            char last = s.charAt(s.length() - 1);
            if (isQuote(first) && isQuote(last)) {
                s = s.substring(1, s.length() - 1).trim();
            } else {
                break;
            }
        }
        return s;
    }

    private static boolean isQuote(char c) {
        return c == '"' || c == '“' || c == '”' || c == '´' || c == '`' || c == '\'';
    }

    private static boolean isDigits(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    private static boolean allDigits(String... arr) {
        for (String s : arr) {
            if (s == null || s.isEmpty()) return false;
            if (!isDigits(s)) return false;
        }
        return true;
    }

    private static int parseInt(String s, int start, int end) {
        return Integer.parseInt(s.substring(start, end));
    }

    private static String fmtYMD(int yyyy, int MM, int dd) {
        java.time.LocalDate d = java.time.LocalDate.of(yyyy, MM, dd);
        return d.toString();
    }

    public static String stripQuotes(String s) {
        if (s == null) return null;
        s = s.trim();
        while (s.length() >= 2) {
            char first = s.charAt(0);
            char last = s.charAt(s.length() - 1);
            if ((first == '"' || first == '\'') && (last == '"' || last == '\'')) {
                s = s.substring(1, s.length() - 1).trim();
            } else {
                break;
            }
        }
        return s;
    }
}
