# Flyway migrations

This project now uses Flyway to apply SQL schema migrations at startup.

## What is configured

- Dependency: `org.flywaydb:flyway-core`
- Dependency: `org.flywaydb:flyway-mysql`
- Flyway enabled via `src/main/resources/application.properties`
- Migration folder: `src/main/resources/db/migration`

## Current migration

- `V1__init_schema.sql`
  - Creates `users`, `business`, `invoice`, and `comment` tables
  - Adds foreign keys for `invoice.business_id` and `comment.author_id`

## Existing database behavior

- `spring.flyway.baseline-on-migrate=true`
- `spring.flyway.baseline-version=1`

This allows an already populated schema (without `flyway_schema_history`) to be marked as baseline version `1` and continue from `V2+` migrations.

## Add a new migration

Create a new file in `src/main/resources/db/migration` with versioned naming:

- `V2__add_invoice_status.sql`
- `V3__some_other_change.sql`

Rules:
- Increment version number.
- Do not edit old migration files after they are applied.
- Put only forward SQL changes in each file.

## Run

```powershell
.\mvnw.cmd spring-boot:run
```

Flyway runs automatically before Hibernate session startup.

