Feature: remove transfer and promo
  Scenario: delete Transaction, rollback transaction and promo
    Given: Account with a balance of 2300
    When: Trying delete deposit 2000
    Then: Account balance should be 0

  Scenario: delete Transaction, rollback transaction without promo.
    Given: Account with a balance of 2300
    When:  Trying delete deposit 1000
    Then: Account balance should be 1300


  Scenario: delete Transaction, rollback transaction withdraw.
  Given: Account with a balance of 2300
  When:  Trying delete withdraw 1000
  Then: Account balance should be 3300