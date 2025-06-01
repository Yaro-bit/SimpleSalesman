# 📄 NoteDto.java

**Paket:** `com.simplesalesman.dto`

## Beschreibung
Data Transfer Object (DTO) für Notizen.  
Dient dem Austausch von Notizdaten zwischen Frontend und Backend – zum Anzeigen, Erstellen und Bearbeiten von Notizen.

## Felder
- `Long id`: Eindeutige ID der Notiz  
- `String text`: Inhalt der Notiz  
- `LocalDateTime createdAt`: Zeitpunkt der Erstellung  
- `String createdBy`: Name des Erstellers  
- *Optional*: `Long addressId` – Zugehörige Adresse (falls benötigt)

## Kommentar für DEV
> Erzeuge ein DTO aus einer Notiz-Entität und prüfe insbesondere das **Format des Zeitstempels** (`createdAt`) sowie die Korrektheit des Benutzernamens (`createdBy`).  
> Bei JSON-Antworten auf die richtige Serialisierung von `LocalDateTime` achten (z.B. ISO-Format).

---

**Verwendung:**  
Das DTO wird für Anzeige, Erstellung und Bearbeitung von Notizen im gesamten System verwendet.
