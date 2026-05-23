# Parametric values API

Base path: `/parametric-values`

## CRUD endpoints

- `GET /list?type=...`
- `POST /save`
- `PUT /edit/{id}`
- `DELETE /delete/{id}`

Request body for create/update:

```json
{
  "description": "Example value",
  "type": "invoice_category"
}
```

## Textarea-based bulk operations

- `PUT /replace-from-textarea`
- `GET /textarea-values?type=...`

Replace from textarea request body:

```json
{
  "type": "invoice_category",
  "valuesText": "First value\nSecond value\nThird value"
}
```

Behavior:
- Clears existing rows for the given `type`
- Inserts one row per non-empty textarea line
- Removes duplicate lines

