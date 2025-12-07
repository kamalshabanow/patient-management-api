#!/bin/bash
set -e # Stops the script if any command fails

# 1. Export the dummy credentials as environment variables
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
# IMPORTANT: Explicitly set a region, LocalStack works better when this is defined
export AWS_DEFAULT_REGION=us-east-1

# Define the common endpoint URL for LocalStack
LOCALSTACK_ENDPOINT="--endpoint-url=http://localhost:4566"

## 1. Delete and Wait for Clean Slate
echo "Deleting existing stack (if present)..."
# The 'delete-stack' command runs asynchronously
aws $LOCALSTACK_ENDPOINT cloudformation delete-stack \
    --stack-name patient-management 2> /dev/null || true

# Wait for the deletion to complete. This is crucial for a clean deployment.
echo "Waiting for stack deletion to complete (This may take a moment)..."
aws $LOCALSTACK_ENDPOINT cloudformation wait stack-delete-complete \
    --stack-name patient-management 2> /dev/null || true


## 2. Deploy Stack
echo "Deploying new stack..."
# Ensure you are using the correct template path: ./cdk.out/localstack.template.json
aws $LOCALSTACK_ENDPOINT cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/localstack.template.json"


## 3. Describe Load Balancer
echo "Fetching Load Balancer DNS Name..."
# Query the ELB resource
aws $LOCALSTACK_ENDPOINT elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text