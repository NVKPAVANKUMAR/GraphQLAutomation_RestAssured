package com.graphql.tests;

import com.qa.pojos.GraphQLQuery;
import com.qa.pojos.QueryVariable;
import com.qa.pojos.SpaceXUser;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SpaceXGraphqlTest {

    @Test
    public void getCompanyData_checkCeo_shouldBeElonMusk() {
        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("{ company { name ceo coo } }");

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("https://api.spacex.land/graphql/")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("data.company.ceo", equalTo("Elon Musk"));
    }

    @Test(description = "Query With Included Limit")
    public void getLaunches_checkMissionName_shouldBeFalconSat() {
        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("query getLaunches { launches(limit: 5) { mission_name } }");

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("https://api.spacex.land/graphql/")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("data.launches[3].mission_name", equalTo("FalconSat"));
    }

    @Test
    public void getLaunches_checkMissionName_shouldBeThaicom6_usingPOJO() {
        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("query getLaunches($limit:Int!) { launches(limit: $limit) { mission_name } }");

        QueryVariable queryVariable = new QueryVariable();
        queryVariable.setLimit(5);
        query.setVariables(queryVariable);

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("https://api.spacex.land/graphql/")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("data.launches[0].mission_name", equalTo("Thaicom 6"));
    }

    @Test
    public void getLaunches_checkMissionName_shouldBeAsiaSat6_usingJSONObject() {
        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("query getLaunches($limit:Int!) { launches(limit: $limit) { mission_name } }");

        JSONObject variables = new JSONObject();
        variables.put("limit", 10);

        query.setVariables(variables.toJSONString());

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("https://api.spacex.land/graphql/")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("data.launches[1].mission_name", equalTo("AsiaSat 6"));
    }

    /*mutation insert_users($id: uuid!, $name: String!, $rocket: String!) {
        insert_users(objects: {id: $id, name: $name, rocket: $rocket}) {
            returning {
                id
                name
                rocket
            }
        }
    }*/

    @Test
    public void addUser_checkReturnedData_shouldCorrespondToDataSent() {
        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("mutation insert_users ($id: uuid!, $name: String!, $rocket: String!) { insert_users(objects: {id: $id, name: $name, rocket: $rocket}) { returning { id name rocket } } }");

        SpaceXUser user = new SpaceXUser(
                UUID.randomUUID(),
                "Pavan",
                "My Awesome Rocket"
        );

        query.setVariables(user);
        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("https://api.spacex.land/graphql/")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("data.insert_users.returning[0].id", equalTo(user.getId().toString()))
                .body("data.insert_users.returning[0].name", equalTo(user.getName()))
                .body("data.insert_users.returning[0].rocket", equalTo(user.getRocket()));
    }
}
