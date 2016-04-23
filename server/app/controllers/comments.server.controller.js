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
	var comment = new Comment(req.body);
	comment.date = new Date();
	comment.save(function(err) {
		if (err) {
			res.status(400).send({message: 'Failed to add comment'});
		} else {
			res.json(comment);
		}
	});
	console.log('Comment added' + JSON.stringify(comment));
	//res.status(200).send({message: JSON.stringify(comment)});
};

/**
 * List of Comments
 */
exports.list_comments = function(req, res) {
	var query = Comment.find({}).select('-_id -__v');
	query.exec(function(err, result) {
		console.log('Comment list ' + JSON.stringify(result) + ' Error :' + JSON.stringify(err));
		res.status(200).send({message: 'List Comments'});
	});
};
