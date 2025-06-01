# 📄 Note.java

**Paket:** `com.simplesalesman.entity`

## Beschreibung
Speichert einen Textkommentar mit Zeitstempel und Referenz zur Adresse und zum Ersteller.

## Felder (typisch)
- Text, timestamp, createdBy
- Verknüpfung zu Address (ManyToOne)
- Verknüpfung zu AppUser (ManyToOne)

## Kommentar für DEV
> Erzeuge und speichere eine Notiz mit Text und Nutzer, verlinkt zu einer Adresse. Lade sie wieder und prüfe die Inhalte.