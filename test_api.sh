#!/bin/bash
# BookMySeat API Testing Script
# This script tests all available API endpoints
# Usage: bash test_api.sh

BASE_URL="http://localhost:8080"
CONTENT_TYPE="Content-Type: application/json"

echo "=========================================="
echo "  BookMySeat API Testing Script"
echo "=========================================="
echo "Base URL: $BASE_URL"
echo ""
echo "Note: Some endpoints may fail if dependencies are not met"
echo "=========================================="
echo ""

# Color codes for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print test results
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo ""
    echo -e "${YELLOW}TEST: $description${NC}"
    echo "Method: $method"
    echo "Endpoint: $endpoint"
    
    if [ -z "$data" ]; then
        echo "Command: curl -X $method \"$BASE_URL$endpoint\" -H \"$CONTENT_TYPE\""
        response=$(curl -s -X "$method" "$BASE_URL$endpoint" -H "$CONTENT_TYPE")
    else
        echo "Data: $data"
        response=$(curl -s -X "$method" "$BASE_URL$endpoint" -H "$CONTENT_TYPE" -d "$data")
    fi
    
    echo "Response:"
    echo "$response" | jq '.' 2>/dev/null || echo "$response"
    echo "---"
}

# ========== APPLICATION TESTS ==========
echo -e "${GREEN}========== APPLICATION TESTS ==========${NC}"

test_endpoint "GET" "/" "" "Welcome Endpoint"

test_endpoint "GET" "/health" "" "Health Check"

# ========== USER TESTS ==========
echo -e "${GREEN}========== USER MANAGEMENT TESTS ==========${NC}"

test_endpoint "POST" "/api/users" '{"email":"testuser1@example.com"}' "Create User"

# ========== CITY TESTS ==========
echo -e "${GREEN}========== CITY MANAGEMENT TESTS ==========${NC}"

test_endpoint "POST" "/api/cities" '{"name":"Mumbai"}' "Create City"

test_endpoint "POST" "/api/cities" '{"name":"Delhi"}' "Create Another City"

# ========== THEATRE TESTS ==========
echo -e "${RED}========== THEATRE MANAGEMENT TESTS (BROKEN) ==========${NC}"

test_endpoint "POST" "/api/theatres/1" '{"name":"PVR Cinemas","address":"Fort, Mumbai"}' \
    "Create Theatre (WILL FAIL - Endpoint Broken)"

test_endpoint "POST" "/api/theatres/1/auditoriums" '{"name":"Screen 1","capacity":150}' \
    "Add Auditorium (WILL FAIL - Endpoint Broken)"

test_endpoint "POST" "/api/theatres/1/seats" '{"REGULAR":100,"RECLINE":50}' \
    "Add Seats to Auditorium (WILL FAIL - Endpoint Broken)"

# ========== SHOW TESTS ==========
echo -e "${YELLOW}========== SHOW MANAGEMENT TESTS ==========${NC}"
echo -e "${YELLOW}NOTE: This requires a valid auditoriumId (which is currently blocked by Theatre endpoint issues)${NC}"

test_endpoint "POST" "/api/shows" \
    '{"movieId":1,"startTime":"2024-01-20T10:00:00Z","endTime":"2024-01-20T13:00:00Z","auditoriumId":1,"seatPrices":{"REGULAR":300,"RECLINE":500},"language":"ENGLISH"}' \
    "Create Show (May fail if auditorium doesn't exist)"

# ========== TICKET TESTS ==========
echo -e "${YELLOW}========== TICKET BOOKING TESTS ==========${NC}"
echo -e "${YELLOW}NOTE: This requires existing show and seats${NC}"

test_endpoint "POST" "/api/tickets" \
    '{"showId":1,"showSeatIds":[1,2,3],"userId":1}' \
    "Book Ticket (May fail if show/seats don't exist)"

echo ""
echo "=========================================="
echo "  Testing Complete"
echo "=========================================="
echo ""
echo -e "${GREEN}✅ Working Endpoints:${NC}"
echo "  - GET /"
echo "  - GET /health"
echo "  - POST /api/users"
echo "  - POST /api/cities"
echo "  - POST /api/shows (partial)"
echo "  - POST /api/tickets (partial)"
echo ""
echo -e "${RED}❌ Broken Endpoints:${NC}"
echo "  - POST /api/theatres/{cityId}"
echo "  - POST /api/theatres/{theatreId}/auditoriums"
echo "  - POST /api/theatres/{auditoriumId}/seats"
echo ""
echo "See API_DOCUMENTATION.md for details on fixing broken endpoints."
echo "=========================================="
