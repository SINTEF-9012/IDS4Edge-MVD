#!/bin/bash

PROVIDER_MANUFACTURING_MNG="http://localhost:8291"

# add asset "asset3"
curl -X POST "${PROVIDER_MANUFACTURING_MNG}/api/management/v3/assets" \
-H "Content-Type: application/json" \
-H "X-Api-Key: password" \
-d @deployment/ids4edge/json/asset3.json

echo
# add policy "require-actor-supplier"
curl -X POST "${PROVIDER_MANUFACTURING_MNG}/api/management/v3/policydefinitions" \
-H "Content-Type: application/json" \
-H "X-Api-Key: password" \
-d @deployment/ids4edge/json/supplier-policy.json


echo
# add contract for asset "asset3"
curl -X POST "${PROVIDER_MANUFACTURING_MNG}/api/management/v3/contractdefinitions" \
-H "Content-Type: application/json" \
-H "X-Api-Key: password" \
-d @deployment/ids4edge/json/asset3-contract.json