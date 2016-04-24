'use strict';

var mongoose = require('mongoose'),
    Sensor = mongoose.model('Sensor');

	
/**
 * Create a sensor
 */
exports.add = function(req, res) {
	Sensor.findOne({sensor_id: req.body.sensor_id}, function(err, result) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to query sensor data.'});
		} else {
			if (result) {
				res.status(400).send({errorCode: 1, message: 'The sensor already exists.'});
			} else {
				var sensor = new Sensor(req.body);
				sensor.deployment_date = new Date();
				sensor.save(function(err) {
					if (err) {
						res.status(400).send({errorCode: 1, message: 'Failed to add sensor information.'});
					} else {
						res.status(200).send({errorCode: 0, message: 'Sensor added successfully.'});
					}
				});
			}
		}
	});
};

/**
 * Show the current sensor
 */
exports.show = function(req, res) {
	var id = req.params.id;
	Sensor.findOne({sensor_id: id}, function(err, doc) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to query sensor data.'});
		} else {
			if (!doc) {
				res.status(400).send({errorCode: 1, message: 'No sensor information available.'});
			} else {
				res.status(200).send({errorCode: 0, message: doc});
			}	
		}

	});
};

/**
 * Update a sensor
 */
exports.update = function(req, res) {
	var id = req.params.id;
	var sensor = req.body;
	Sensor.update({sensor_id: id}, sensor, {safe:true}, function(err, doc) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to update sensor data.'});
		} else {	
			res.status(200).send({errorCode: 0, message: 'Update sensor data successfully.'});
		}

	});

};

/**
 * Delete a sensor
 */
exports.deleteSensor = function(req, res) {
	var id = req.params.id;
	Sensor.findOneAndRemove({sensor_id: id}, {safe: true}, function(err, result) {
		if (err) {
			res.status(400).send({errorCode: 1, message: 'Failed to delete sensor information.'});
		} else {
			res.status(200).send({errorCode: 0, message: 'Sensor data is deleted.'});
		}

	});
};

/**
 * List of sensor
 */
exports.list = function(req, res) {
	var query = Sensor.find({}).select('-_id -__v');
	query.exec(function(err, result) {
		if (err) {	
			res.status(400).send({errorCode: 1, message: 'Failed to fetch sensor data from database.'});
		} else {
			res.status(200).send({errorCode: 0, message: result});
		}
	});
};
