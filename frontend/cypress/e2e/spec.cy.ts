describe("login_and_signup", () => {
  let id = 0;
  let cookie = "";
  before(() => {
      cy.request({
      method: "POST",
      url: "http://localhost:8080/api/user/signup",
      body: {"username": "user", "password": "user"}
    }).then((response) => {
      id = response.body["id"]
      cookie = response.body["jwt"]
    })
    cy.log(cookie)
  })
  after(() => {
    cy.setCookie("Token", cookie.toString())
    cy.request("DELETE", "localhost:8080/api/user/" + id)
  })
  it("frontend_should_be_accessible",() => {
    cy.visit("localhost:3000")
  })
 it("login_with_exisitng_user_should_redirect_to_exercises_page", () => {
    cy.visit("localhost:3000/login")
    cy.get("#username").type("user")
    cy.get("#password").type("user")
    cy.get("#submit").click()
    cy.url().should("include", "/exercises")
      }
  )
  it("submit_form_should_add_new_exercise", () => {
    cy.visit("localhost:3000")
    cy.get("#username").type("user")
    cy.get("#password").type("user")
    cy.get("#submit").click()
    cy.url().should("include", "/exercises")
    cy.get("#title").type("1")
    cy.get("#description").type("1")
    cy.get("#solution").type("1")
    cy.get("#difficulty").select("Leicht")
    cy.get("#language").select("Python")
    cy.get("#category").type("???")
    cy.get("#points").type("5")
      }
  )
  it("exercises_page_should_redirect_to_login_page_when_not_logged_in", () => {
    cy.visit("localhost:3000/exercises")
    cy.url().should("include", "/login")
      }
  )
  it("signup_should_response_with_bad_request_if_user_already_exists", () => {
    cy.request({
      method: "POST",
      url: "http://localhost:8080/api/user/signup",
      failOnStatusCode: false,
      body: {"username": "user", "password": "user"}
    }).then(response => {expect(response).property("status").to.equal(400)})
    })

})