package vasilis.vasilis.general;

import java.util.HashMap;
import java.util.Map;

public class TranslationUtil {
    private static final Map<String, String> translations = new HashMap<>();

    static {
        translations.put("id", "ID");
        translations.put("date", "Ημερομηνία"); // Greek for "Date"
        translations.put("type", "Τύπος");
        translations.put("who", "Πελάτης");
        translations.put("area", "Περιοχή");
        translations.put("details", "Λεπτομέρειες");
        translations.put("fee", "Αμοιβή");
        translations.put("advancePayment", "Προκαταβολή");
        translations.put("remainingMoney", "Υπόλοιπο");
        translations.put("costs", "Έξοδα");
        translations.put("payout", "Πληρωμή");
        translations.put("filesCompleted", "Αρχεία Ολοκληρωμένα");
        translations.put("filesDelivered", "Αρχεία Παραδοθέντα");
        translations.put("comments", "Σχόλια");
    }

    public static String getTranslation(String key) {
        return translations.getOrDefault(key, key); // Return the translation or default to key if not found
    }
}
