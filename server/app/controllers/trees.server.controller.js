'use strict';

var mongoose = require('mongoose'),
    Tree = mongoose.model('Tree');

	
/**
 * Create a Tree
 */
exports.add = function(req, res) {
	Tree.findOne({tree_id: req.body.sensor_id}, function(err, result) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to query tree data.'});
		} else {
			if (result) {
				res.status(400).send({errorCode: 1, message: 'Tree data already exists.'});
			} else {
				var tree = new Tree(req.body);
				tree.deployment_date = new Date();
				tree.save(function(err) {
					if (err) {
						res.status(400).send({errorCode: 1, message: 'Failed to add tree information.'});
					} else {
						res.status(200).send({errorCode: 0, message: 'Tree added successfully.'});
					}
				});

			}
		}

	});
};

/**
 * Show the current Tree
 */
exports.show = function(req, res) {
	var id = req.params.id;
	Tree.findOne({tree_id: id}, function(err, doc) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to query tree data.'});
		} else {
			if (!doc) {
				res.status(400).send({errorCode: 1, message: 'No tree information available.'});
			} else {
				res.status(200).send({errorCode: 0, message: doc});
			}	
		}

	});
};

/**
 * Update a Tree
 */
exports.update = function(req, res) {
	var id = req.params.id;
	var tree = req.body;
	Tree.update({tree_id: id}, tree, {safe:true}, function(err, doc) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to update tree data.'});
		} else {	
			res.status(200).send({errorCode: 0, message: 'Update tree data successfully.'});
		}

	});

};

/**
 * Delete an Tree
 */
exports.deleteTree = function(req, res) {
	var id = req.params.id;
	Tree.findOneAndRemove({tree_id: id}, {safe: true}, function(err, result) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to delete tree information.'});
		} else {
			res.status(200).send({errorCode: 0, message: 'Tree data is deleted.'});
		}

	});
};

/**
 * List of Trees
 */
exports.list = function(req, res) {
	var query = Tree.find({}).select('-_id -__v');
	query.exec(function(err, result) {
		if (err) {	
			res.status(400).send({errorCode: 1, message: 'Failed to fetch tree data from database.'});
		} else {
			res.status(200).send({errorCode: 0, message: result});
		}
	});
};
