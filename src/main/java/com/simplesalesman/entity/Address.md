# 📄 Address.java

**Paket:** `com.simplesalesman.entity`

## Beschreibung
Stellt eine Adresse dar, die einer Region zugeordnet ist und mit mehreren Projekten und Notizen verknüpft sein kann.

## Felder (typisch)
- Straße, PLZ, Ort
- Region (ManyToOne)
- Projekte (OneToMany)
- Notizen (OneToMany)

## Kommentar für DEV
> Schreibe einen Test, der eine Adresse mit zugehöriger Region, Projekt und Notiz persistiert und anschließend wieder korrekt lädt.