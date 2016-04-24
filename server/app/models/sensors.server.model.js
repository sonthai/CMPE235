'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

/**
 * Sensors Schema
 */
var SensorsSchema = new Schema({
	// Sensors model fields   
	sensor_id: {
		type: String
	},
	sensor_location: {
		type: String
	},
	sensor_status: {
		type: String
	},
	sensor_type: {
		type: String
	},
	deployment_date: {
		type: Date
	}

});

mongoose.model('Sensor', SensorsSchema);
