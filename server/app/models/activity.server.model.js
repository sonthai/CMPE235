'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

/**
 * Activity Schema
 */
var ActivitySchema = new Schema({
	// Activity model fields   
	tree_id: {
		type: String
	},
	activity: {
		type: String
	},
	userName: {
		type: String
	},
	timestamp: {
		type: Date
	}
});

mongoose.model('Activity', ActivitySchema);
