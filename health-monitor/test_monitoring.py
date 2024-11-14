import pytest
from app import create_app
from flask import json

@pytest.fixture
def client():
    app = create_app()
    app.config['TESTING'] = True
    with app.test_client() as client:
        yield client

def test_get_all_microservices(client):
    response = client.get('/microservices')
    assert response.status_code == 200

def test_register_microservice(client):
    new_microservice = {
        "name": "test-service",
        "endpoint": "http://localhost:5000/health",
        "frequency": 5,
        "email": "test@example.com"
    }
    response = client.post('/microservices', data=json.dumps(new_microservice), content_type='application/json')
    assert response.status_code == 201

def test_health_check(client):
    response = client.get('/microservices/test-service')
    assert response.status_code == 200
