# Spring refactor auto-deploy (Hetzner)

This pipeline deploys only the Spring container when code is pushed to branch `refactor`.

## Workflow file

- `.github/workflows/deploy-spring-refactor.yml`

## Required GitHub repository secrets

- `HETZNER_HOST`: server IP or hostname
- `HETZNER_USER`: SSH user
- `HETZNER_SSH_KEY`: private key content for the SSH user
- `HETZNER_APP_PATH`: absolute path on server where `docker-compose.yml` exists

## What the workflow runs on the server

1. `git fetch origin`
2. `git checkout refactor`
3. `git pull origin refactor`
4. `docker compose up -d --build spring`
5. `docker compose ps spring`

## One-time server prep

- Repo must already be cloned in `HETZNER_APP_PATH`
- Docker and Docker Compose must be installed
- The SSH user must be able to run `docker compose`
- `.env` values for compose must exist on the server

## Manual trigger

The workflow also supports manual run from GitHub Actions (`workflow_dispatch`).


