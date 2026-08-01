# Privacy notice for the current open-source build

This document describes the behavior of the repository as currently implemented. It is not a substitute for the legal privacy policy or store disclosures required for a published application.

## Data stored

TakaKoi stores the information a user enters for profiles, transactions, amounts, descriptions, savings goals, loans, and tasks in a local Room SQLite database. The app does not currently require an account and does not send these records to a TakaKoi server.

The Settings screen can clear the stored records and reset the local profile. The opt-in sample-data action creates fictional demo values only; it is not imported from a real person or service.

## Data leaving the device

The current app has no application analytics, advertising SDK, authentication service, cloud sync, or runtime HTTP calls. Build dependencies reserved for future work do not by themselves mean that data is transmitted. Any future network feature must be reviewed, documented, consented to where required, and reflected in Play Console/App Store disclosures.

## Backups and device security

Android backup is disabled in the manifest, and both Android backup-rule formats exclude the finance database. The iOS prototype stores its database in the app's Documents directory; iOS device/iCloud backup behavior is controlled by the operating system and must be explicitly reviewed before App Store release. The current database is not encrypted by TakaKoi; protect the device and do not treat this prototype as a replacement for a secure financial ledger.

## Demo assets and screenshots

Sample values in source and screenshots are fictional and exist only to demonstrate the UI. Contributors must not add real names, addresses, account numbers, employer details, or personal financial records to source, tests, screenshots, issues, or pull requests.

## Future store release checklist

Before publication, the maintainer must publish a jurisdiction-appropriate privacy policy, confirm backup/export/deletion behavior, add the required iOS privacy manifest, complete Play Data Safety and App Store privacy labels, and review every third-party SDK and permission in the signed release binary.
