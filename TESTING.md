# Testing Guide (Web)

## Unit tests we keep as critical
- `controller/TestControllerTest.java`: `/api/v1/scan` endpoint envelope + force_cloud parsing.
- `service/ScanServiceTest.java`: Tier-2 trigger rules and final response fields from service layer.
- `BinItRightApplicationTests.java`: Spring context smoke test.

## Local command
```bash
./mvnw -B test
```

## CI evidence
- Workflow: `.github/workflows/pr_validation.yml`
- Artifacts:
  - `java-unit-reports`
  - `java-lint-reports`
  - `java-security-testing-report`
