'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    Activity = mongoose.model('Activity');

/**
 * Record user's activity
 */
exports.record = function(req, res) {
	var activity = new Activity(req.body);
	activity.timestamp = new Date();
	activity.save(function(err) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to record user activity.'});
		} else {
			res.status(200).send({errorCode: 0, message: 'Successfully record user activity.'});
		}
	});
};
