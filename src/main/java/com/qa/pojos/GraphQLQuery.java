package com.qa.pojos;

import lombok.Data;

/**
 * This is the main POJO class for complete query and json variable
 * @author nvkpavankumar
 *
 */

@Data
public class GraphQLQuery {
	private String query;
	private Object variables;
}
