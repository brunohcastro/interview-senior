Feature: save a new hero
  Scenario: client makes a call to POST /api/v1/heroes
    When the client calls /api/v1/heroes
    Then the client receives status code of 201
    And the client receives a Location Header