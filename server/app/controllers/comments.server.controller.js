'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    Comment = mongoose.model('Comment');

/**
 * Create a Comment
 */
exports.add_comment = function(req, res) {
	var comment = new Comment(req.body);
	comment.date = new Date();
	comment.save(function(err) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to add comment.'});
		} else {
			res.status(200).send({errorCode: 0, message: 'Comment added successfully.'});
		}
	});
};

/**
 * List of Comments
 */
exports.list_comments = function(req, res) {
	var query = Comment.find({}).select('-_id -__v');
	query.exec(function(err, result) {
		if (err) {	
			res.status(400).send({errorCode: 1, message: 'Failed to fetch data from database.'});
		} else {
			res.status(200).send({errorCode: 0, message: JSON.stringify(result)});
		}
	});
};
