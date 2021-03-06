'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

/**
 * Trees Schema
 */
var TreesSchema = new Schema({
	// Trees model fields   
	tree_id: {
		type: String
	},
	sensor_id: {
		type: String
	},
	tree_location: {
		type: String
	},
	tree_status: {
		type: String
	},
	deployment_date: {
		type: Date
	}
});

mongoose.model('Tree', TreesSchema);
