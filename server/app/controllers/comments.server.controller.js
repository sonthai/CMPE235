'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    _ = require('lodash'),
    Comment = mongoose.model('Comment');

/**
 * Create a Comment
 */
exports.add_comment = function(req, res) {
	res.status(200).send({message: 'Add Commnet'});
};

/**
 * List of Comments
 */
exports.list_comments = function(req, res) {
	res.status(200).send({message: 'List comments'});
};
