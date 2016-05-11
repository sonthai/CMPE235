'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

/**
 * Comments Schema
 */
var CommentsSchema = new Schema({
	// Comments model fields   
	userName: {
		type: String,
		trim: true
	},
	comment: {
		type: String
	},
	rating: {
		type: Number
	},
	date: {
		type: String
	}
});

mongoose.model('Comment', CommentsSchema);
