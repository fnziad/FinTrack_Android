# Security policy

## Supported versions

Only the latest commit on `main` is supported. Development builds and old tags are provided for testing and may contain unfinished behavior.

## Reporting a vulnerability

Please report security issues privately through [GitHub Security Advisories](https://github.com/fnziad/TakaKoi/security/advisories/new). Include the affected version or commit, the affected platform, reproduction steps, and the potential impact. Do not open a public issue with credentials, personal data, or an exploit before a fix is available.

If private advisories are unavailable, open a minimal public issue that contains no sensitive details and asks the maintainer to enable a private channel.

## Public-repository security rules

- Credentials, signing keys, provisioning profiles, service-account files, local IDE state, and private certificates must never be committed.
- CI uses read-only repository permissions and does not receive production signing secrets.
- The Android CI APK is a debug test artifact. It is not a signed Play Store release.
- The iOS CI framework and app container are unsigned integration artifacts. TestFlight/App Store builds require a separate protected signing workflow.
- Sample records and screenshots must be fictional and must not contain real names, locations, account details, or financial records.

## Product security boundary

The current app is local-first. User-entered financial data is stored in the platform app container using Room/SQLite. There is no account system, cloud sync, telemetry, or application network endpoint in the current release. Any future network, sync, export, authentication, or analytics feature must update this policy, [PRIVACY.md](PRIVACY.md), tests, and store disclosures before merge.

## Scope and severity

Report credential exposure, unintended data transmission, unsafe file or database handling, authentication/authorization flaws, malicious CI changes, and vulnerabilities that can affect another user's device or data. Purely cosmetic issues, expected local-only data access by the device owner, and vulnerabilities requiring a compromised operating system are generally out of scope unless they cross an additional trust boundary.
