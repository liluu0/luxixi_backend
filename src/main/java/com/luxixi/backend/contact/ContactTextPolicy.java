package com.luxixi.backend.contact;

import java.text.Normalizer;

public final class ContactTextPolicy {
    private ContactTextPolicy() {
    }

    public static String normalizeName(String value) {
        String normalized = normalize(value);
        rejectControls(normalized, false);
        return normalized;
    }

    public static String normalizeMessage(String value) {
        String normalized = normalize(value);
        rejectControls(normalized, true);
        return normalized;
    }

    private static String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFC).trim();
    }

    private static void rejectControls(String value, boolean allowLineBreaks) {
        boolean invalid = value.codePoints().anyMatch(codePoint -> {
            if (!Character.isISOControl(codePoint)) {
                return codePoint == 0x2028 || codePoint == 0x2029;
            }
            return !(allowLineBreaks && (codePoint == '\n' || codePoint == '\r' || codePoint == '\t'));
        });
        if (invalid) {
            throw new InvalidContactMessageException();
        }
    }
}
